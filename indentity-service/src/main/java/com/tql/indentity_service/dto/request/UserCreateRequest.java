package com.tql.indentity_service.dto.request;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateRequest {
    String username;
    String name;
    String password;
    String confirmPassword;
    String firstName;
    String lastName;
    String city;
}
