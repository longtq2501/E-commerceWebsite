package com.tql.indentity_service.controller;

import com.nimbusds.jose.JOSEException;
import com.tql.indentity_service.dto.request.ApiResponse;
import com.tql.indentity_service.dto.request.AuthenticateRequest;
import com.tql.indentity_service.dto.request.IntrospectRequest;
import com.tql.indentity_service.dto.request.LogoutRequest;
import com.tql.indentity_service.dto.response.AuthenticateResponse;
import com.tql.indentity_service.dto.response.IntrospectResponse;
import com.tql.indentity_service.service.AuthenticateService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticateController {
    AuthenticateService authenticateService;

    @PostMapping("/login")
    ApiResponse<AuthenticateResponse> login(@RequestBody AuthenticateRequest request) {
        var authenticate = authenticateService.authenticate(request);
        return ApiResponse.<AuthenticateResponse>builder()
                .result(authenticate)
                .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        var introspect = authenticateService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder()
                .result(introspect)
                .build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenticateService.logout(request);
        return ApiResponse.<Void>builder().build();
    }
}
