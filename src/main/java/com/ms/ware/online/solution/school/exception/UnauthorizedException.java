package com.ms.ware.online.solution.school.exception;

import lombok.Getter;


@Getter
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        super("The request has not been applied because it lacks valid authentication credentials.", null, false, false);
    }

    private final ErrorMessage dto = ErrorMessage.builder().message("The request has not been applied because it lacks valid authentication credentials.").code(401).build();
}
