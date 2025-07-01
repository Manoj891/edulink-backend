package com.ms.ware.online.solution.school.controller.swagger.setup;

import com.ms.ware.online.solution.school.entity.setup.SubjectGroup;
import com.ms.ware.online.solution.school.service.setup.SubjectGroupService;
import java.io.IOException;
import com.ms.ware.online.solution.school.config.DB;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("api/Setup/SubjectGroup")
public class SubjectGroupRestController {

    @Autowired
    SubjectGroupService service;

    @GetMapping
    public Object index() {

        return service.getAll();
    }

    @GetMapping("/ClassWise")
    public Object indexClassWise(@RequestParam long program, @RequestParam long classId, @RequestParam long subjectGroup) {
        return new DB().getRecord("select s.id id,concat(s.name,' (',d.subject_code,')') AS name from subject_group_detail d,subject_master s where s.id=d.subject and program=" + program + " AND class_id=" + classId+ " AND subject_group=" + subjectGroup);
    }

    @GetMapping("/ClassGroupWise")
    public Object indexClassGroupWise(@RequestParam Long subjectGroup, @RequestParam Long program, @RequestParam Long classId) {
        return new DB().getRecord("SELECT S.ID id,CONCAT(S.NAME,' (',D.SUBJECT_CODE,')') AS name FROM subject_group_detail D,subject_master S WHERE S.ID=D.SUBJECT  AND D.SUBJECT_GROUP='"+subjectGroup+"' AND PROGRAM=" + program + " AND CLASS_ID=" + classId);
    }

    @GetMapping("/{id}")
    public Object index(@PathVariable long id, @RequestParam(required = false) Long program, @RequestParam(required = false) Long classId) {

        return service.getAll(id, program, classId);
    }

    @PostMapping
    public Object doSave(@RequestBody SubjectGroup obj) throws IOException {
        return service.save(obj);
    }

    @PutMapping("/{id}")
    public Object doUpdate(@RequestBody SubjectGroup obj, @PathVariable long id) throws IOException {
        return service.update(obj, id);
    }

    @DeleteMapping("/{id}")
    public Object doDelete(@PathVariable String id) {
        return service.delete(id);
    }
}
