package com.ms.ware.online.solution.school.controller.swagger.teacher;

import com.ms.ware.online.solution.school.config.DB;
import com.ms.ware.online.solution.school.dto.ExamMarkEntryReq;
import com.ms.ware.online.solution.school.service.exam.ExamMarkEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/TeacherPanel/MarkEntry")
public class MarkEntryParaController {
    @Autowired
    private DB db;
    @Autowired
    private ExamMarkEntryService service;

    @GetMapping
    public Map<String, Object> getMarkEntry() {
        Map<String, Object> map = new HashMap<>();
        map.put("academicYear", db.getMapRecord("select id,year as name from academic_year order by id desc limit 2"));
        map.put("exam", db.getMapRecord("select id,exam_name as name from exam_master order by id desc limit 10"));
        map.put("group", db.getMapRecord("select id,name as name from subject_group"));
        map.put("program", db.getMapRecord("select id,name as name from program_master"));
        map.put("class", db.getMapRecord("select id,name as name from class_master"));
        map.put("section", db.getMapRecord("select id,name as name from section"));
        return map;
    }

    @GetMapping("/Entry")
    public Object index(@RequestParam Long exam, @RequestParam Long program, @RequestParam Long classId, @RequestParam Long subjectGroup, @RequestParam Long subject, @RequestParam String section, @RequestParam String order) {
        return service.getAll(exam, program, classId, subjectGroup, subject, section, order);
    }

    @PostMapping("/Entry")
    public Object entryTeacher(@RequestBody ExamMarkEntryReq req) {
        return service.entryTeacher(req);
    }

}
