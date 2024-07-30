package com.tql.indentity_service.enums;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppException extends RuntimeException{
    private ErrorCode errorCode;
}
