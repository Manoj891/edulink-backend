package com.ms.ware.online.solution.school.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SmsSendingReq {

    private String mobileNo;
    private String message;
    private String email;
    private String id;

}
