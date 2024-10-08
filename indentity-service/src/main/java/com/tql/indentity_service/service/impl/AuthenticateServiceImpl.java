package com.tql.indentity_service.service.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.tql.indentity_service.dto.request.AuthenticateRequest;
import com.tql.indentity_service.dto.request.IntrospectRequest;
import com.tql.indentity_service.dto.request.LogoutRequest;
import com.tql.indentity_service.dto.request.RefreshRequest;
import com.tql.indentity_service.dto.response.AuthenticateResponse;
import com.tql.indentity_service.dto.response.IntrospectResponse;
import com.tql.indentity_service.entity.InvalidatedToken;
import com.tql.indentity_service.entity.User;
import com.tql.indentity_service.exception.AppException;
import com.tql.indentity_service.enums.ErrorCode;
import com.tql.indentity_service.repository.InvalidatedTokenRepository;
import com.tql.indentity_service.repository.UserRepository;
import com.tql.indentity_service.service.AuthenticateService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticateServiceImpl implements AuthenticateService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    InvalidatedTokenRepository invalidatedTokenRepository;

    @Value("${spring.jwt.signerKey}")
    @NonFinal
    private String SIGNER_KEY;

    @Override
    public AuthenticateResponse authenticate(AuthenticateRequest request) {
        var user = userRepository.findByUsername(request.getUsername());
        var authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticated) {
            throw new AppException(ErrorCode.NOT_CORRECT_PASSWORD);
        }
        else {
            var token = generateToken(user);
            return AuthenticateResponse.builder()
                    .token(token)
                    .result(true)
                    .build();
        }
    }

    @Override
    public String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("e-commerce.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(claims.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new AppException(ErrorCode.UNABLE_CREATE_TOKEN);
        }
    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest request)
            throws JOSEException, ParseException {
        var token = request.getToken();
        boolean isValid = true;

        try {
            verifyToken(token);
        } catch (AppException e) {
            isValid = false;
        }
        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier jwsVerifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);
        if (!signedJWT.verify(jwsVerifier)) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(jwsVerifier);

        if(!verified && expiryTime.after(new Date())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        if(invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        return signedJWT;
    }

    @Override
    public void logout(LogoutRequest request)
            throws JOSEException, ParseException {
        var signToken = verifyToken(request.getToken());
        String jti = signToken.getJWTClaimsSet().getJWTID();
        Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jti)
                .expiryTime(expiryTime)
                .build();

        invalidatedTokenRepository.save(invalidatedToken);
    }

    @Override
    public AuthenticateResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        var signToken = verifyToken(request.getToken());
        String jti = signToken.getJWTClaimsSet().getJWTID();
        Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jti)
                .expiryTime(expiryTime)
                .build();

        invalidatedTokenRepository.save(invalidatedToken);

        var username = signToken.getJWTClaimsSet().getSubject();

        var user = userRepository.findByUsername(username);

        var token = generateToken(user);

        return AuthenticateResponse.builder()
                .token(token)
                .result(true)
                .build();
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if(!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions()))
                    role.getPermissions().forEach(permission ->
                            stringJoiner.add(permission.getName()));
            });

        }
        return stringJoiner.toString();
    }
}
