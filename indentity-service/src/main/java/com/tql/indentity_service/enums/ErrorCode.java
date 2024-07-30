package com.tql.indentity_service.enums;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    USER_NOT_FOUND(1001, "User not found"),
    USER_EXISTS(1002, "User exists"),
    UNAUTHORIZED(9999, "Unauthorized"),
    NOT_CORRECT_PASSWORD(1004, "Uncorrect password"),
    NOT_CORRECT_USERNAME(1005, "Uncorrect username"),
    UNABLE_CREATE_TOKEN(1006, "Unable create token"),
    INVALID_TOKEN(1007, "Invalid token"),
    USER_ALREADY_EXISTS(1008, "User already exists"),
    USER_NOT_EXISTED(1009, "User not existed"),;

    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
