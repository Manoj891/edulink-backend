package com.ms.ware.online.solution.school.config.security;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticatedUser {
    private String userId;
    private String userName;
    private String userType;
    private String cashAccount;
    private String context;
    private  String role;
    private boolean status;

    @Override
    public String toString() {
        return "{\"context\":\"" + context + "\",\"userId\":\"" + userId + "\",\"userType\":\"" + userType + "\",\"cashAccount\":\"" + cashAccount + "\",\"userName\":\"" + userName + "\"}";
    }
}

