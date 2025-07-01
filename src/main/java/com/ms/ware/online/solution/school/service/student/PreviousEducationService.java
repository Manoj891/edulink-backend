package com.ms.ware.online.solution.school.service.student;

import org.springframework.http.ResponseEntity;
import com.ms.ware.online.solution.school.entity.student.PreviousEducation;

import java.util.Map;

public interface PreviousEducationService {

    ResponseEntity<Map<String,Object>> getAll(Long regNo);

    ResponseEntity<Object> save(PreviousEducation obj);

    ResponseEntity<Object> delete(String id);

}
