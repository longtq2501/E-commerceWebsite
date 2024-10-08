package com.tql.indentity_service.configuration;

import com.nimbusds.jwt.SignedJWT;
import com.tql.indentity_service.service.AuthenticateService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import java.text.ParseException;

//@Configuration
@Component("customJwtDecoder")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CustomJwtDecoder implements JwtDecoder {

    AuthenticateService authenticateService;

    @NonFinal
    NimbusJwtDecoder nimbusJwtDecoder = null;

    @Value("${spring.jwt.signerKey}")
    @NonFinal
    String signerKey;


    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return new Jwt(token,
                    signedJWT.getJWTClaimsSet().getIssueTime().toInstant(),
                    signedJWT.getJWTClaimsSet().getExpirationTime().toInstant(),
                    signedJWT.getHeader().toJSONObject(),
                    signedJWT.getJWTClaimsSet().getClaims()
                    );
        } catch (ParseException e) {
            throw new JwtException("Invalid token", e);
        }
    }
}
