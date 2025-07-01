package com.ms.ware.online.solution.school.service.library;

import org.springframework.http.ResponseEntity;
import com.ms.ware.online.solution.school.entity.library.LibBookType;

public interface LibBookTypeService {

    public ResponseEntity getAll();

    public ResponseEntity save(LibBookType obj);

    public ResponseEntity update(LibBookType obj,long id);

    public ResponseEntity delete(String id);

}