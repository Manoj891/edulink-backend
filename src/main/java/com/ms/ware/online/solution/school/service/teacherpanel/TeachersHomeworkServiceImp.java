/*    map = new com.fasterxml.jackson.databind.ObjectMapper().readValue(jsonData, new com.fasterxml.jackson.core.type.TypeReference<>() {});
 */
package com.ms.ware.online.solution.school.service.teacherpanel;


import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.teacherpanel.TeachersHomeworkDao;
import com.ms.ware.online.solution.school.entity.teacherpanel.TeachersHomework;
import com.ms.ware.online.solution.school.model.DatabaseName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.ms.ware.online.solution.school.config.Message;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;
import java.util.Map;

@Service
public class TeachersHomeworkServiceImp implements TeachersHomeworkService {
    @Autowired
    private AuthenticationFacade facade;
    @Autowired
    TeachersHomeworkDao da;

    @Override
    public ResponseEntity getAll(Long academicYear, Long program, Long classId, Long subjectGroup) {
        String sql = "SELECT GET_BS_DATE(HOMEWORK_DATE) homeworkDate,ACADEMIC_YEAR academicYear,H.ID id,H.CLASS_ID classId,H.PROGRAM program,H.SUBJECT_GROUP subjectGroup,H.SUBJECT subject,H.TEACHER teacher, GET_BS_DATE(H.ENTER_DATE) enterDate,IFNULL(H.FILE_URL,'') fileUrl,H.HOMEWORK_TITLE homeworkTitle,H.HOME_WORK homeWork,S.NAME subjectName FROM teachers_homework H,subject_master S,ad_bs_calender C WHERE H.HOMEWORK_DATE=C.AD_DATE AND H.SUBJECT=S.ID AND ACADEMIC_YEAR =" + academicYear + " AND H.ID AND H.CLASS_ID='" + classId + "' AND H.PROGRAM='" + program + "' AND H.SUBJECT_GROUP='" + subjectGroup + "'";
        return ResponseEntity.status(200).body(da.getRecord(sql));
    }

    @Override
    public Object save(HttpServletRequest request, TeachersHomework obj, MultipartFile photo) {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        String msg = "", sql;
        try {
            sql = "SELECT ifnull(MAX(ID),0)+1 AS id FROM teachers_homework";
            message.map = (Map) da.getRecord(sql).get(0);
            obj.setId(Long.parseLong(message.map.get("id").toString()));
            obj.setTeacher(Long.parseLong(td.getUserId()));
            obj.setEnterDate(new Date());
            try {
                if (photo.getSize() > 100) {
                    String location = "/home/tomcat/webapps/";
                    String filePath = DatabaseName.getDocumentUrl() + "Document/Homework/" + obj.getId() + "/";
                    File f = new File(location + filePath);
                    try {
                        if (!f.exists()) {
                            f.mkdirs();
                        }
                    } catch (Exception e) {
                        return message.respondWithError(e.getMessage());
                    }

                    String fileName = filePath + "Question-" + photo.getOriginalFilename();
                    f = new File(location + fileName);
                    try {
                        if (f.exists()) {
                            f.deleteOnExit();
                        }
                    } catch (Exception e) {
                    }
                    f = new File(location + fileName);
                    photo.transferTo(f);
                    obj.setFileUrl(fileName);
                }
            } catch (Exception e) {
            }
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
    public Object update(HttpServletRequest request, TeachersHomework obj, long id, MultipartFile photo) {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        int row;
        String msg;
        obj.setId(id);
        row = da.update(obj);
        msg = da.getMsg();
        if (row > 0) {

            try {
                if (photo.getSize() > 100) {
                    String location = message.getFilepath(DatabaseName.getDocumentUrl());
                    String filePath = DatabaseName.getDocumentUrl() + "Document/Homework/" + obj.getId() + "/";
                    File f = new File(location + filePath);
                    try {
                        if (!f.exists()) {
                            f.mkdirs();
                        }
                    } catch (Exception e) {
                        return message.respondWithError(e.getMessage());
                    }
                    String fileName = filePath + "Question-" + photo.getOriginalFilename();
                    f = new File(location + fileName);
                    try {
                        if (f.exists()) {
                            f.deleteOnExit();
                        }
                    } catch (Exception e) {
                    }
                    f = new File(location + fileName);
                    photo.transferTo(f);
                    String sql = "UPDATE teachers_homework SET FILE_URL='" + fileName + "' WHERE ID=" + obj.getId();
                    da.delete(sql);
                }
            } catch (Exception e) {
            }
            return ResponseEntity.status(200).body(message.respondWithMessage("Success"));
        } else if (msg.contains("Duplicate entry")) {
            msg = "This record already exist";
        } else if (msg.contains("foreign key")) {
            msg = "this record already used in reference tables, Cannot delete of this record";
        }
        return ResponseEntity.status(200).body(message.respondWithError(msg));
    }

    @Override
    public ResponseEntity delete(String id) {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();;
        
        int row;
        String msg = "", sql;
        sql = "DELETE FROM teachers_homework WHERE ID='" + id + "'";
        row = da.delete(sql);
        msg = da.getMsg();
        if (row > 0) {
            return ResponseEntity.status(200).body(message.respondWithMessage("Success"));
        } else if (msg.contains("foreign key")) {
            msg = "this record already used in reference tables, Cannot delete of this record";
        }
        return ResponseEntity.status(200).body(message.respondWithError(msg));
    }
}
