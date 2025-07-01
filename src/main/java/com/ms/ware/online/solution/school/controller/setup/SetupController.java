package com.ms.ware.online.solution.school.controller.setup;

import com.ms.ware.online.solution.school.config.DB;
import com.ms.ware.online.solution.school.config.DateConverted;
import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.model.DatabaseName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
public class SetupController {
    @Autowired
    private DB db;
    @Autowired
    private AuthenticationFacade facade;

    @GetMapping("api/setup/login-init")
    public ResponseEntity<Map<String, Object>> setup() {
        Map<String, Object> map = new HashMap<>();

        String sql;
        sql = "SELECT id,name FROM program_master ORDER BY ID";
        map.put("programMaster", db.getRecord(sql));
        sql = "SELECT id,name FROM class_master ORDER BY ID";
        map.put("classMaster", db.getRecord(sql));
        sql = "SELECT ID id,YEAR year FROM academic_year ORDER BY STATUS DESC,ID DESC";
        map.put("academicYear", db.getRecord(sql));
        sql = "SELECT ID id,NAME name FROM subject_group ORDER BY NAME";
        map.put("subjectGroup", db.getRecord(sql));
        sql = "SELECT ID id,YEAR year FROM fiscal_year ORDER BY STATUS DESC ,ID";
        map.put("fiscalYear", db.getRecord(sql));
        sql = "SELECT ID id,NAME name FROM bill_master WHERE ID>0 ORDER BY NAME";
        map.put("billMaster", db.getRecord(sql));
        sql = "SELECT ID id,NAME name FROM sundry_creditors WHERE ID >0";
        map.put("sundryCreditors", db.getRecord(sql));

        sql = "SELECT ID id,NAME name FROM section ";
        map.put("section", db.getRecord(sql));
        return ResponseEntity.status(200).body(map);
    }

    @PostMapping("api/setup/login-init/organization")
    public ResponseEntity<Map<String, Object>> initData() {
        AuthenticatedUser td = facade.getAuthentication();
        Map<String, Object> data = new HashMap<>();
        data.put("username", td.getUserName());
        data.put("userType", td.getUserType());
        String sql = "SELECT NAME organizationName,CONCAT(MUNICIPAL,'-',WARD_NO,', ',ADDRESS) organizationCity ,TEL AS tel FROM organization_master WHERE ID=1";
        List<Map<String, Object>> l = db.getRecord(sql);
        if (l.isEmpty()) {
            data.put("organizationName", "Please define Organization Name");
            data.put("organizationTel", "Please define Organization Name");
            data.put("organizationAddress", "Please define Organization Name");
        } else {
            Map<String, Object> m = l.get(0);
            data.put("organizationName", m.get("organizationName").toString());
            data.put("organizationTel", m.get("tel").toString());
            data.put("organizationAddress", m.get("organizationCity").toString());
        }
        if (!td.getUserType().equalsIgnoreCase("ADM")) {
            StringBuilder br = new StringBuilder();
            db.getRecord("SELECT URI uri FROM menu_master M,menu_user_access A WHERE M.id=A.MENU AND STATUS='Y' AND A.USER_ID='" + td.getUserId() + "'")
                    .forEach(map -> br.append(map.get("uri").toString()).append(","));
            data.put("menuUserAccess", br.toString());
        } else {
            data.put("menuUserAccess", "");
        }

        data.put("userType", td.getUserType());
        sql = "SELECT id,name FROM program_master ORDER BY ID";
        data.put("programMaster", db.getRecord(sql));
        sql = "SELECT id,name FROM class_master ORDER BY ID";
        data.put("classMaster", db.getRecord(sql));
        sql = "SELECT ID id,YEAR year FROM academic_year ORDER BY STATUS DESC,ID DESC";
        data.put("academicYear", db.getRecord(sql));
        sql = "SELECT ID id,NAME name FROM subject_group ORDER BY NAME";
        data.put("subjectGroup", db.getRecord(sql));
        sql = "SELECT ID id,YEAR year FROM fiscal_year ORDER BY STATUS DESC ,ID";
        data.put("fiscalYear", db.getRecord(sql));
        sql = "SELECT ID id,NAME name FROM bill_master WHERE ID>0 ORDER BY NAME";
        data.put("billMaster", db.getRecord(sql));
        sql = "SELECT ID id,NAME name FROM sundry_creditors WHERE ID >0";
        data.put("sundryCreditors", db.getRecord(sql));
        sql = "SELECT ID id,NAME name FROM section ";
        data.put("section", db.getRecord(sql));
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }


    @PostMapping("/api/TeacherPanel/login-init")
    public ResponseEntity<Map<String, String>> initTeacherData() {

        AuthenticatedUser td = facade.getAuthentication();
        Map<String, String> map = new HashMap<>();
        map.put("teacherId", td.getUserId());
        map.put("teacherName", td.getUserName());
        String today = DateConverted.today();
        map.put("bsdate", DateConverted.adToBs(today));

        Map<String, Object> m;
        String sql = "SELECT ID id,GET_BS_DATE(START_DATE) fyStart FROM fiscal_year WHERE '" + today + "' BETWEEN START_DATE AND END_DATE;";
        try {
            m = db.getRecord(sql).get(0);
            map.put("fiscalYear", m.get("id").toString());
            map.put("fyStart", m.get("fyStart").toString());
        } catch (Exception e) {
            map.put("fiscalYear", "Please define curent fiscal year!");
        }
        sql = "SELECT NAME organizationName,CONCAT(MUNICIPAL,'-',WARD_NO,', ',ADDRESS) organizationCity ,TEL AS tel FROM organization_master WHERE ID=1";
        List<Map<String, Object>> l = db.getRecord(sql);
        if (l.isEmpty()) {
            map.put("organizationName", "Please define Organization Name");
            map.put("organizationCity", "Please define Organization Name");
            map.put("organizationTel", "Please define Organization Name");
        } else {
            m = l.get(0);
            map.put("organizationName", m.get("organizationName").toString());
            map.put("organizationCity", m.get("organizationCity").toString());
            map.put("organizationTel", m.get("tel").toString());
        }
        map.put("document", DatabaseName.getDocumentUrl() + "Document/");
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }


    @PostMapping("/api/panel/student/login-init")
    public ResponseEntity<Map<String, String>> initStudentData(HttpServletRequest request) {
        String today = DateConverted.today();
        AuthenticatedUser td = facade.getAuthentication();
        Map<String, String> map = new HashMap<>();
        map.put("stuId", td.getUserId());
        map.put("stuName", td.getUserName());
        map.put("bsdate", DateConverted.adToBs(today));
        Map<String, Object> m;
        String sql = "SELECT ID id,GET_BS_DATE(START_DATE) fyStart FROM fiscal_year WHERE '" + today + "' BETWEEN START_DATE AND END_DATE;";
        try {
            m = db.getRecord(sql).get(0);
            map.put("fiscalYear", m.get("id").toString());
            map.put("fyStart", m.get("fyStart").toString());
        } catch (Exception e) {
            map.put("fiscalYear", "Please define curent fiscal year!");
        }
        sql = "SELECT NAME organizationName,CONCAT(MUNICIPAL,'-',WARD_NO,', ',ADDRESS) organizationCity ,TEL AS tel FROM organization_master WHERE ID=1";
        List<Map<String, Object>> l = db.getRecord(sql);
        if (l.isEmpty()) {
            map.put("organizationName", "Please define Organization Name");
            map.put("organizationCity", "Please define Organization Name");
            map.put("organizationtel", "Please define Organization Name");
        } else {
            m = l.get(0);
            map.put("organizationName", m.get("organizationName").toString());
            map.put("organizationCity", m.get("organizationCity").toString());
            map.put("organizationTel", m.get("tel").toString());
        }
        map.put("document", DatabaseName.getDocumentUrl() + "Document/");
        sql = "SELECT ACADEMIC_YEAR academicYear,PROGRAM program,CLASS_ID classId,SUBJECT_GROUP subjectGroup FROM student_info WHERE ID=" + td.getUserId();
        l = db.getRecord(sql);
        m = l.get(0);
        map.put("academicYear", m.get("academicYear").toString());
        map.put("program", m.get("program").toString());
        map.put("classId", m.get("classId").toString());
        map.put("subjectGroup", m.get("subjectGroup").toString());
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }
}
