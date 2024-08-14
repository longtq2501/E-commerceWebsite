package com.tql.profile_service.service.impl;

import com.tql.profile_service.dto.request.IntrospectRequest;
import com.tql.profile_service.dto.response.IntrospectResponse;
import com.tql.profile_service.repository.httpclient.IdentityClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticateService {
    IdentityClient identityClient;

    public IntrospectResponse introspect(String token) {
        return identityClient.introspect(IntrospectRequest.builder()
                        .token(token)
                .build());
    }
}
