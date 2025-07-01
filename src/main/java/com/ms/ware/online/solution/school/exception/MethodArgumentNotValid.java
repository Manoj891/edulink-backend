package com.ms.ware.online.solution.school.exception;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MethodArgumentNotValid {
    private String message;
    private List<FieldError> fieldErrors;
}


