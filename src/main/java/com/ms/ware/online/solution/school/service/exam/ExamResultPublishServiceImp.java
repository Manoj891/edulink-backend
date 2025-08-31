/*    map = new com.fasterxml.jackson.databind.ObjectMapper().readValue(jsonData, new com.fasterxml.jackson.core.type.TypeReference<>() {});
 */
package com.ms.ware.online.solution.school.service.exam;

import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.exam.ExamResultPublishDao;
import com.ms.ware.online.solution.school.entity.exam.ExamResultPublish;
import com.ms.ware.online.solution.school.entity.exam.ExamResultPublishPK;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExamResultPublishServiceImp implements ExamResultPublishService {
    @Autowired
    private AuthenticationFacade facade;
    @Autowired
    ExamResultPublishDao da;
    Message message = new Message();
    String msg = "", sql;
    int row;

    @Override
    public Object getAll() {
        return da.getAll("from ExamResultPublish");
    }

    @Override
    public Object save(ExamResultPublish obj) {
        AuthenticatedUser td = facade.getAuthentication();;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        try {
            long exam = obj.getExam().getId();
            long program = obj.getProgram().getId();
            long classId = obj.getClassId().getId();
            obj.setPk(new ExamResultPublishPK(exam, program, classId));
            obj.setPublishBy(td.getUserName());
            row = da.save(obj);
            msg = da.getMsg();
            if (row > 0) {
                resultPublish(exam, program, classId);
                return message.respondWithMessage("Success");
            } else if (msg.contains("Duplicate entry")) {
                msg = "This record already exist";
            }
            return message.respondWithError(msg);

        } catch (Exception e) {
            return message.respondWithError(e.getMessage());
        }
    }

    @Override
    public Object update(ExamResultPublish obj) {
        AuthenticatedUser td = facade.getAuthentication();;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        long exam = obj.getExam().getId();
        long program = obj.getProgram().getId();
        long classId = obj.getClassId().getId();
        obj.setPk(new ExamResultPublishPK(exam, program, classId));
        obj.setPublishBy(td.getUserName());
        row = da.update(obj);
        msg = da.getMsg();
        if (row > 0) {
            resultPublish(exam, program, classId);
            return message.respondWithMessage("Success");
        } else if (msg.contains("Duplicate entry")) {
            msg = "This record already exist";
        } else if (msg.contains("foreign key")) {
            msg = "this record already used in reference tables, Cannot delete of this record";
        }
        return message.respondWithError(msg);
    }

    @Override
    public Object delete(String id) {

        AuthenticatedUser td = facade.getAuthentication();;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        String[] dd = id.split("-");
        String exam = dd[0];
        String program = dd[1];
        String classId = dd[2];
        sql = "DELETE FROM exam_result_publish_subject WHERE EXAM=" + exam + " and PROGRAM=" + program + " and CLASS_ID=" + classId;
        row = da.delete(sql);
        sql = "DELETE FROM exam_result_publish WHERE EXAM=" + exam + " and PROGRAM=" + program + " and CLASS_ID=" + classId;
        row = da.delete(sql);
        msg = da.getMsg();
        if (row > 0) {
            return message.respondWithMessage("Success");
        } else if (msg.contains("foreign key")) {
            msg = "this record already used in reference tables, Cannot delete of this record";
        }
        return message.respondWithError(msg);

    }

    void resultPublish(long exam, long program, long classId) {
        sql = "DELETE FROM exam_result_publish_subject WHERE EXAM=" + exam + " AND PROGRAM=" + program + " AND CLASS_ID=" + classId;
        da.delete(sql);
        sql = "SELECT ID subjectGroup FROM subject_group ";
        List list = da.getRecord(sql);
        Map map;
        String subjectGroup;
        for (int i = 0; i < list.size(); i++) {
            map = (Map) list.get(i);
            subjectGroup = map.get("subjectGroup").toString();
            sql = "INSERT INTO exam_result_publish_subject(SUBJECT,PROGRAM,CLASS_ID,SUBJECT_GROUP,EXAM,SUBJECT_CODE,SUBJECT_NAME,GROUP_NAME,TH_FM,TH_PM,PR_FM,PR_PM,CREDIT_HOUR,CREDIT_PH,SUBJECT_CODE_PH,in_order) "
                    + "SELECT D.SUBJECT,PROGRAM,CLASS_ID,SUBJECT_GROUP," + exam + " AS EXAM,D.SUBJECT_CODE subject_Code,S.NAME subject_Name,M.NAME GROUP_NAME,D.TH_FM,D.TH_PM,D.PR_FM,D.PR_PM,D.CREDIT,D.CREDIT_PH,D.SUBJECT_CODE_PH,D.in_order "
                    + " FROM subject_group_detail D,subject_group M,subject_master S WHERE M.ID=D.SUBJECT_GROUP AND D.SUBJECT=S.ID AND D.PROGRAM=" + program + " AND D.CLASS_ID=" + classId + " AND D.SUBJECT_GROUP=" + subjectGroup + " ORDER BY D.SUBJECT_CODE";
            da.delete(sql);
        }
    }
}
