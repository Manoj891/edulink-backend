package com.ms.ware.online.solution.school.service.utility;

import org.springframework.http.ResponseEntity;
import com.ms.ware.online.solution.school.entity.utility.EmailNotificationService;

public interface EmailNotificationServiceService {

    ResponseEntity getAll();

    ResponseEntity save(EmailNotificationService obj);

    ResponseEntity update(EmailNotificationService obj, String id);

    ResponseEntity delete(String id);

    void sendEmail();
}
