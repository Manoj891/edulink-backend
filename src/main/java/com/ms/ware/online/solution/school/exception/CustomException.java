package com.ms.ware.online.solution.school.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final ErrorMessage dto = ErrorMessage.builder().build();

    public CustomException(String message, int code) {
        super(message, null, false, false);
        dto.setMessage(message);
        dto.setCode(code);
    }

    public CustomException(String message) {
        super(message, null, false, false);
        dto.setMessage(message);
        dto.setCode(407);
    }

    public CustomException(String message, String fieldName, Object fieldValue) {
        super(message, null, false, false);
        dto.setMessage(message + " not found with " + fieldName + " : " + fieldValue);
        dto.setCode(408);
    }


}
