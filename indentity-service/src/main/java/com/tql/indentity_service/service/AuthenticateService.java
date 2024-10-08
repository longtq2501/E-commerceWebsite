package com.tql.indentity_service.service;

import com.nimbusds.jose.JOSEException;
import com.tql.indentity_service.dto.request.AuthenticateRequest;
import com.tql.indentity_service.dto.request.IntrospectRequest;
import com.tql.indentity_service.dto.request.LogoutRequest;
import com.tql.indentity_service.dto.request.RefreshRequest;
import com.tql.indentity_service.dto.response.AuthenticateResponse;
import com.tql.indentity_service.dto.response.IntrospectResponse;
import com.tql.indentity_service.entity.User;

import java.text.ParseException;

public interface AuthenticateService {
    AuthenticateResponse authenticate(AuthenticateRequest request);
    String generateToken(User user);

    IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException;

    void logout(LogoutRequest request) throws JOSEException, ParseException;

    AuthenticateResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException;
}
