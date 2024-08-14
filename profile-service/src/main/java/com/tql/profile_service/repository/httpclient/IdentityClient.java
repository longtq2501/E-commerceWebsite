package com.tql.profile_service.repository.httpclient;

import com.tql.profile_service.dto.request.IntrospectRequest;
import com.tql.profile_service.dto.response.IntrospectResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "identity-service", url = "http://localhost:8080/identity")
public interface IdentityClient {

    @PostMapping(value = "/auth/introspect", produces = MediaType.APPLICATION_JSON_VALUE)
    IntrospectResponse introspect(@RequestBody IntrospectRequest request);
}
