package com.ms.ware.online.solution.school.controller.utility;


import com.ms.ware.online.solution.school.entity.utility.AboutApp;
import com.ms.ware.online.solution.school.service.utility.AboutAppService;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping("api/Utility/AboutApp")
public class AboutAppRestController {

    @Autowired
    AboutAppService service;

    @GetMapping
    public ResponseEntity index() {

        return service.getAll();
    }

    @PostMapping
    public ResponseEntity doSave(@RequestBody AboutApp obj) throws IOException {
        return service.save(obj);
    }

    
}
