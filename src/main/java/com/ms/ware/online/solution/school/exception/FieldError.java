package com.ms.ware.online.solution.school.exception;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FieldError {
    private String name;
    private String message;
}
