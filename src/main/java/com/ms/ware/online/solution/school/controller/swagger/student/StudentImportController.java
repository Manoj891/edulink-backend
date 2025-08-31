package com.ms.ware.online.solution.school.controller.swagger.student;

import com.ms.ware.online.solution.school.dao.student.StudentInfoDao;
import com.ms.ware.online.solution.school.exception.CustomException;
import com.ms.ware.online.solution.school.entity.student.StudentImport;
import com.ms.ware.online.solution.school.entity.student.StudentInfo;
import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.model.DatabaseName;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/Student/Import")
public class StudentImportController {
    @Autowired
    private Message message;
    @Autowired
    private StudentInfoDao da;

    @GetMapping("max")
    public Map<String, Object> getPending() {
        return da.getRecord("select max(id) as id from student_info").get(0);
    }

    @GetMapping
    public Map<String, Object> postPending() {

        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> classId = da.getRecord("SELECT class_name name FROM student_import where class_id is null group by class_name");
        List<Map<String, Object>> program = da.getRecord("SELECT program_name name FROM student_import where program is null group by program_name");
        List<Map<String, Object>> groupId = da.getRecord("SELECT group_name name FROM student_import where group_id is null group by group_name");
        map.put("class", classId);
        map.put("group", groupId);
        map.put("program", program);
        if (classId.isEmpty() && program.isEmpty() && groupId.isEmpty()) {
            map.put("data", this.da.getRecord("select i.id, i.students_name studentName, i.fathers_name fathersName, i.date_of_birth dateOfBirth, i.mobile_no mobileNo, p.NAME programName, c.NAME className,i.roll_no rollNo, g.NAME groupName, ifnull(s.ID, 'New') student from student_import i join program_master p on i.program = p.ID join class_master c on class_id = c.ID join subject_group g on i.group_id = g.ID left join student_info s on i.id = s.ID where approved = false"));
            map.put("academicYear", da.getRecord("select id,year name from academic_year order by id desc"));
        } else {
            map.put("classId", da.getRecord("SELECT id,name FROM class_master "));
            map.put("groupId", da.getRecord("SELECT id,name FROM subject_group "));
            map.put("programId", da.getRecord("SELECT id,name FROM program_master "));
            map.put("data", new ArrayList<>());
        }

        return map;
    }

    @PutMapping
    public Map<String, Object> updateId(@RequestBody String jsonData) throws IOException {
        Map<String, Object> map = new com.fasterxml.jackson.databind.ObjectMapper().readValue(jsonData, new com.fasterxml.jackson.core.type.TypeReference<>() {
        });
        String type = map.get("val").toString();
        String id = map.get("id").toString();
        String name = map.get("name").toString();
        switch (type) {
            case "CLASS":
                da.delete("update student_import set class_name=replace(class_name,' ','-') where class_name like '% %'");
                da.delete("update student_import set class_id='" + id + "' where class_name='" + name + "'");
                break;
            case "PROGRAM":
                da.delete("update student_import set program_name=replace(program_name,' ','-') where program_name like '% %'");
                da.delete("update student_import set program='" + id + "' where program_name='" + name + "'");
                break;
            case "GROUP":
                da.delete("update student_import set group_name=replace(group_name,' ','-') where group_name like '% %'");
                da.delete("update student_import set group_id='" + id + "' where group_name='" + name + "'");
                break;
        }
        return getPending();
    }

    @PostMapping
    public Map<String, Object> uploadExcel(HttpServletRequest request, @RequestParam MultipartFile excel) throws IOException {
        String location = message.getFilepath(DatabaseName.getDocumentUrl());
        File f = new File(location);
        try {
            if (!f.exists()) {
                f.mkdirs();
            }
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
        location = location + DatabaseName.getDocumentUrl() + "-student.xlsx";
        f = new File(location);
        System.out.println(f.getAbsolutePath());
        excel.transferTo(f);
        FileInputStream inputStream = new FileInputStream(f);
        List<StudentImport> list = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        new XSSFWorkbook(inputStream).getSheetAt(0).forEach(row -> {
            try {

                StudentImport obj = new StudentImport();
                long id = 0;
                try {
                    id = (long) row.getCell(0).getNumericCellValue();
                    System.out.println(id);
                } catch (Exception e) {
                }
                if (id > 0) {
                    obj.setId(id);
                    obj.setStudentsName(row.getCell(1).getStringCellValue());
                    obj.setProgramName(row.getCell(2).getStringCellValue());
                    obj.setClassName(row.getCell(3).getStringCellValue());
                    obj.setGroupName(row.getCell(4).getStringCellValue());
                    obj.setSection(row.getCell(5).getStringCellValue());
                    obj.setRollNo((int) row.getCell(6).getNumericCellValue());

                    try {
                        obj.setMobileNo(row.getCell(7).getStringCellValue());
                    } catch (Exception e) {
                        try {
                            obj.setMobileNo((long) row.getCell(7).getNumericCellValue() + "");
                        } catch (Exception ex) {
                        }
                    }

                    try {
                        obj.setDateOfBirth(row.getCell(8).getStringCellValue());
                    } catch (Exception e) {
                        try {
                            obj.setDateOfBirth(format.format(row.getCell(8).getDateCellValue()));
                        } catch (Exception ex) {
                        }
                    }
                    obj.setFathersName(row.getCell(9).getStringCellValue());
                    try {
                        obj.setFathersContactNo(row.getCell(10).getStringCellValue());
                    } catch (Exception e) {
                        try {
                            obj.setFathersContactNo((long) row.getCell(10).getNumericCellValue() + "");
                        } catch (Exception ex) {
                        }
                    }
                    try {
                        obj.setFathersOccupation(row.getCell(11).getStringCellValue());
                    } catch (Exception e) {
                    }
                    try {
                        obj.setMothersName(row.getCell(12).getStringCellValue());
                    } catch (Exception e) {
                    }
                    try {
                        obj.setMothersOccupation(row.getCell(13).getStringCellValue());
                    } catch (Exception e) {
                    }

                    try {
                        obj.setMothersContactNo(row.getCell(14).getStringCellValue());
                    } catch (Exception e) {
                        try {
                            obj.setMothersContactNo((long) row.getCell(14).getNumericCellValue() + "");
                        } catch (Exception ex) {
                        }
                    }
                    try {

                        obj.setPermanentAddress(row.getCell(15).getStringCellValue());
                    } catch (Exception e) {
                    }
                    try {
                        obj.setCorrespondingAddress(row.getCell(16).getStringCellValue());
                    } catch (Exception e) {
                    }
                    try {
                        obj.setGuardiansName(row.getCell(17).getStringCellValue());
                    } catch (Exception e) {
                    }
                    try {
                        obj.setGender(row.getCell(18).getStringCellValue());
                    } catch (Exception e) {
                    }

                    try {
                        obj.setRegistrationDate(row.getCell(19).getStringCellValue());
                    } catch (Exception e) {
                        try {
                            obj.setRegistrationDate(format.format(row.getCell(19).getDateCellValue()));
                        } catch (Exception ex) {
                        }
                    }

                    obj.setApproved(false);
                    try {
                        if (da.save(obj) == 1) {
                            list.add(obj);
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
//                System.out.println(e.getMessage());
            }
        });
        Map<String, Object> map = new HashMap<>();
        map.put("message", "Success");
        map.put("data", list);
        return map;

    }

    @PutMapping("/{academicYear}")
    public Map<String, Object> updateId(@RequestBody List<String> ids, @PathVariable Long academicYear) {
        List<String> id = new ArrayList<>();
        da.getStudentImport("from StudentImport where approved=false and id in(" + ids.toString().replace("[", "").replace("]", "") + ")").forEach(s -> {
            try {
                StudentInfo info = new StudentInfo();
                info.setId(s.getId());
                info.setAcademicYear(academicYear);
                info.setAdmissionYear(academicYear.toString());
                info.setProgram(s.getProgram());
                info.setClassId(s.getClassId());
                info.setSubjectGroup(s.getGroupId());
                info.setStuName(s.getStudentsName());
                info.setFathersName(s.getFathersName());
                info.setMothersName(s.getMothersName());
                info.setMobileNo(s.getMobileNo());
                info.setFathersOccupation(s.getFathersOccupation());
                info.setFathersMobile(s.getFathersContactNo());
                info.setMothersOccupation(s.getMothersOccupation());
                info.setMothersMobile(s.getMothersContactNo());
                info.setTol(s.getPermanentAddress());
                info.setTolt(s.getCorrespondingAddress());
                info.setGuardiansName(s.getGuardiansName());
                info.setDateOfBirth(s.getDateOfBirth());
                info.setSection(s.getSection());
                if (s.getGender().length() == 1)
                    info.setGender(s.getGender());
                else if (s.getGender().toLowerCase().contains("female")) {
                    info.setGender("F");
                } else info.setGender("M");
                info.setRollNo(s.getRollNo());
                if (da.save(info) == 1) {
                    id.add(info.getId().toString());
                    s.setApproved(true);
                    da.update(s);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });

        String sql = "INSERT INTO class_transfer(ACADEMIC_YEAR,STUDENT_ID,CLASS_ID,PROGRAM,ROLL_NO,SECTION,SUBJECT_GROUP) SELECT ACADEMIC_YEAR,ID,CLASS_ID,PROGRAM,ROLL_NO,SECTION,SUBJECT_GROUP FROM student_info WHERE ID in(" + id.toString().replace("[", "").replace("]", "") + ")";
        da.delete(sql);

        da.delete("update student_import set approved=true where approved=false and id in(select S.id from student_info S where ACADEMIC_YEAR=" + academicYear + ")");
        return getPending();
    }
}
