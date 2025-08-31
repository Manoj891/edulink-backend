package com.ms.ware.online.solution.school.controller.employee;

import com.ms.ware.online.solution.school.entity.employee.TeachersClassSubject;
import com.ms.ware.online.solution.school.service.employee.TeachersClassSubjectService;
import java.io.IOException;
import java.util.List;
import com.ms.ware.online.solution.school.config.DB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("api/Employee/")
public class TeachersClassSubjectRestController {

    @Autowired
    TeachersClassSubjectService service;
    @Autowired
    private DB db;
    @GetMapping("TeachersClassSubject")
    public ResponseEntity< List<TeachersClassSubject>> index(@RequestParam Long academicYear, @RequestParam Long program, @RequestParam Long classId) {

        return ResponseEntity.status(HttpStatus.OK).body(service.getAll(academicYear, program, classId));
    }

    @GetMapping("ClassTeacher")
    public Object getClassTeacher(@RequestParam Long academicYear, @RequestParam Long program, @RequestParam Long classId, @RequestParam Long subjectGroup) {
       
        String sql = "SELECT T.ID id,CONCAT(T.first_name,' ',T.last_name) name FROM teachers_class_subject C,employee_info T WHERE C.TEACHER=T.ID AND C.ACADEMIC_YEAR='" + academicYear + "' AND C.PROGRAM=" + program + " AND C.CLASS_ID=" + classId + " AND C.SUBJECT_GROUP='" + subjectGroup + "' AND C.IS_CLASS_TEACHER='Y' GROUP BY C.TEACHER ";
        return db.getRecord(sql);
    }

    @PostMapping("TeachersClassSubject")
    public ResponseEntity doSave(@RequestBody TeachersClassSubject obj) throws IOException {
        return service.save(obj);
    }

    @PutMapping("TeachersClassSubject/{id}")
    public ResponseEntity update(@PathVariable String id, @RequestBody TeachersClassSubject obj) throws IOException {
        return service.update(obj, id);
    }

    @DeleteMapping("TeachersClassSubject/{id}")
    public ResponseEntity<String> delete(@PathVariable String id) throws IOException {
        return service.delete(id);
    }

}
