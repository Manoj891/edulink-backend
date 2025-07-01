package com.ms.ware.online.solution.school.exception;

import lombok.Getter;

@Getter
public class PasswordChangeException extends RuntimeException {
    public PasswordChangeException() {
        super("You must change password.", null, false, false);
    }

    private final ErrorMessage dto = ErrorMessage.builder().message("You must change password.").code(426).build();

}
