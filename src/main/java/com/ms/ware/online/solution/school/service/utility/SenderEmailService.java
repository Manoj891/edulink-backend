package com.ms.ware.online.solution.school.service.utility;

import org.springframework.http.ResponseEntity;
import com.ms.ware.online.solution.school.entity.utility.SenderEmail;

public interface SenderEmailService {

    public ResponseEntity getAll();

    public ResponseEntity save(SenderEmail obj);

    public ResponseEntity update(SenderEmail obj, long id);

    public ResponseEntity delete(String id);

}