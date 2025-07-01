package com.ms.ware.online.solution.school.service.utility;

import org.springframework.http.ResponseEntity;
import com.ms.ware.online.solution.school.entity.utility.AboutApp;

public interface AboutAppService {

    public ResponseEntity getAll();

    public ResponseEntity save(AboutApp obj);

   
}