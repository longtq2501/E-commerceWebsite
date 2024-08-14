package com.tql.profile_service.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    USER_NOT_FOUND(1001, "User not found", HttpStatus.NOT_FOUND),
    USER_EXISTS(1002, "User exists", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(9999, "Unauthorized",HttpStatus.FORBIDDEN),
    NOT_CORRECT_PASSWORD(1004, "Uncorrect password", HttpStatus.BAD_REQUEST),
    NOT_CORRECT_USERNAME(1005, "Uncorrect username", HttpStatus.BAD_REQUEST),
    UNABLE_CREATE_TOKEN(1006, "Unable create token", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_TOKEN(1007, "Invalid token", HttpStatus.UNAUTHORIZED),
    USER_ALREADY_EXISTS(1008, "User already exists", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1009, "User not existed", HttpStatus.BAD_REQUEST),
    UNCATEGORIZED(1010, "Uncategorized", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHENTICATED(1011, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    PERMISSION_NOT_FOUND(1012, "Permission not found", HttpStatus.NOT_FOUND),
    PERMISSION_EXISTED(1013, "Permission existed", HttpStatus.BAD_REQUEST),
    ROLE_NOT_FOUND(1014, "Role not found", HttpStatus.NOT_FOUND),
    ROLE_EXISTED(1015, "Role existed", HttpStatus.BAD_REQUEST),
    ROLE_NOT_EXISTED(1017, "Role not existed", HttpStatus.BAD_REQUEST),
    PERMISSION_NOT_ALLOWED(1020, "Permission not allowed", HttpStatus.BAD_REQUEST),
    EXPIRED_TOKEN(1021, "", HttpStatus.BAD_REQUEST);

    private int code;
    private String message;
    private HttpStatusCode httpStatusCode;

    ErrorCode(int code, String message, HttpStatusCode httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }
}
