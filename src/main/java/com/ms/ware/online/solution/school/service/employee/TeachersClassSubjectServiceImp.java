/*    map = new com.fasterxml.jackson.databind.ObjectMapper().readValue(jsonData, new com.fasterxml.jackson.core.type.TypeReference<>() {});
 */
package com.ms.ware.online.solution.school.service.employee;


import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.employee.TeachersClassSubjectDao;
import com.ms.ware.online.solution.school.entity.employee.TeachersClassSubject;
import com.ms.ware.online.solution.school.entity.employee.TeachersClassSubjectPK;

import java.util.List;
import java.util.UUID;
import com.ms.ware.online.solution.school.config.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;

@Service
public class TeachersClassSubjectServiceImp implements TeachersClassSubjectService {

    @Autowired
    TeachersClassSubjectDao da;
    @Autowired
    private AuthenticationFacade facade;

    @Override
    public List<TeachersClassSubject> getAll(Long academicYear, Long program, Long classId) {
        return da.getAll("from TeachersClassSubject where academicYear=" + academicYear + " and program=IFNULL(" + program + ",program) and classId=IFNULL(" + classId + ",classId) ");
    }

    @Override
    public ResponseEntity save(TeachersClassSubject obj) {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return ResponseEntity.status(200).body(message.respondWithError("invalid token"));
        }
        String msg = "";
        try {
            obj.setPk(new TeachersClassSubjectPK(obj.getTeacher(), obj.getAcademicYear(), obj.getProgram(), obj.getClassId(), obj.getSubjectGroup(), obj.getSubject(), obj.getSection()));
            obj.setEntityId(UUID.randomUUID().toString());
            int row = da.save(obj);
            msg = da.getMsg();
            if (row > 0) {
                return ResponseEntity.status(200).body(message.respondWithMessage("Success"));
            } else if (msg.contains("Duplicate entry")) {
                msg = "This record already exist";
            }
            return ResponseEntity.status(200).body(message.respondWithError(msg));

        } catch (Exception e) {
            return ResponseEntity.status(200).body(message.respondWithError(e.getMessage()));
        }
    }

    @Override
    public ResponseEntity update(TeachersClassSubject obj, String id) {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return ResponseEntity.status(200).body(message.respondWithError("invalid token"));
        }
        String msg = "";
        try {
            String sql = "UPDATE teachers_class_subject SET SUBJECT_GROUP='" + obj.getSubjectGroup() + "',SUBJECT='" + obj.getSubject() + "',CLASS_ID='" + obj.getClassId() + "',PROGRAM='" + obj.getProgram() + "',ACADEMIC_YEAR='" + obj.getAcademicYear() + "',TEACHER='" + obj.getTeacher() + "',section='" + obj.getSection() + "' WHERE entity_id='" + id + "'";
            if (da.delete(sql) == 0) {
                return ResponseEntity.status(200).body(message.respondWithError(da.getMsg()));
            }
            obj.setPk(new TeachersClassSubjectPK(obj.getTeacher(), obj.getAcademicYear(), obj.getProgram(), obj.getClassId(), obj.getSubjectGroup(), obj.getSubject(), obj.getSection()));
            obj.setEntityId(id);
            int row = da.save(obj);
            msg = da.getMsg();
            if (row > 0) {
                return ResponseEntity.status(200).body(message.respondWithMessage("Success"));
            } else if (msg.contains("Duplicate entry")) {
                msg = "This record already exist";
            }
            return ResponseEntity.status(200).body(message.respondWithError(msg));

        } catch (Exception e) {
            return ResponseEntity.status(200).body(message.respondWithError(e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<String> delete(String id) {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return ResponseEntity.status(200).body(message.respondWithError("invalid token"));
        }
        da.delete("DELETE FROM teachers_class_subject  WHERE entity_id='" + id + "'");
        return ResponseEntity.status(200).body(message.respondWithMessage("Success"));
    }

}
