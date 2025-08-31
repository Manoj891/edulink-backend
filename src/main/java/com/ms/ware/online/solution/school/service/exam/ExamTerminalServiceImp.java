/*    map = new com.fasterxml.jackson.databind.ObjectMapper().readValue(jsonData, new com.fasterxml.jackson.core.type.TypeReference<>() {});
 */

package com.ms.ware.online.solution.school.service.exam;

import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.exam.ExamTerminalDao;
import com.ms.ware.online.solution.school.entity.exam.ExamTerminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ExamTerminalServiceImp implements ExamTerminalService {
    @Autowired
    private AuthenticationFacade facade;
    @Autowired
    ExamTerminalDao da;
    @Autowired
    private Message message;
    String msg = "", sql;
    int row;

    @Override
    public Object getAll(Long academicYear) {
        return da.getRecord("SELECT M.ID examId,T.id terminalId,EXAM_NAME examName,T.final_terminal final,final_percent percent  FROM exam_terminal T,exam_master M where T.ID=M.TERMINAL and ACADEMIC_YEAR=" + academicYear);
    }

    @Override
    public Object getMark(long program, long classId, long finalExam) {
        sql = "SELECT TH_OM thOm, t1t, t2t, t3t, PR_OM prOm, t1p, t2p, t3p, S.NAME subject,I.STU_NAME stuName,I.ID regNo from exam_mark_entry M,   exam_student_registration R,   subject_master S,   student_info I where M.EXAM_REG_ID = R.ID and M.SUBJECT = S.ID and R.STUDENT_ID = I.ID and M.EXAM = " + finalExam + " AND R.PROGRAM = " + program + " and R.CLASS_ID = " + classId + " order by I.ID,S.NAME";
        return da.getRecord(sql);
    }

    @Override
    public Object markPush(long academicYear, long terminalExam, long program, long classId, long finalExam) {
        sql = "SELECT id,final_percent percent FROM exam_terminal where id=(SELECT TERMINAL FROM exam_master where exam_master.ID=" + terminalExam + ")";
        List<Map<String, Object>> list = da.getRecord(sql);
        if (list.isEmpty()) {
            message.respondWithError("Invalid Terminal Exam");
        }
        int pushTerminal = Integer.parseInt(list.get(0).get("id").toString());
        float pushPercent = Float.parseFloat(list.get(0).get("percent").toString());
        sql = "select (T.TH_OM * " + pushPercent + " / 100) th,(T.PR_OM * " + pushPercent + " / 100) pr,STUDENT_REG_NO stuId,SUBJECT subject from exam_mark_entry T,exam_student_registration R where R.ID =T.EXAM_REG_ID and T.EXAM=" + terminalExam + " and CLASS_ID=" + classId + " and R.PROGRAM=" + program;
        da.getRecord(sql).forEach(map -> da.delete("update exam_mark_entry set t" + pushTerminal + "t=" + map.get("th") + ",t" + pushTerminal + "p=" + map.get("pr") + " where STUDENT_REG_NO=" + map.get("stuId") + " and EXAM=" + finalExam + " and SUBJECT=" + map.get("subject")));
        return message.respondWithMessage("Success");
    }


    @Override
    public Object getAll() {
        return da.getAll("from ExamTerminal");
    }

    @Override
    public Object save(ExamTerminal obj) {
        AuthenticatedUser td = facade.getAuthentication();;

        if (!td.getUserId().equals("1")) return message.respondWithError("Permission Denied.");
        try {
            try {
                sql = "SELECT ifnull(MAX(ID),0)+1 AS id FROM exam_terminal";
                message.map =  da.getRecord(sql).get(0);
                obj.setId(Long.parseLong(message.map.get("id").toString()));
            } catch (Exception e) {
                return message.respondWithError("connection error or invalid table name");
            }
            row = da.save(obj);
            msg = da.getMsg();
            if (row > 0) {
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
    public Object update(ExamTerminal obj, long id) {
        AuthenticatedUser td = facade.getAuthentication();;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        obj.setId(id);
        row = da.update(obj);
        msg = da.getMsg();
        if (row > 0) {
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

        id = "'" + id.replace(",", "','") + "'";
        sql = "DELETE FROM exam_terminal WHERE ID IN (" + id + ")";
        row = da.delete(sql);
        msg = da.getMsg();
        if (row > 0) {
            return message.respondWithMessage("Success");
        } else if (msg.contains("foreign key")) {
            msg = "this record already used in reference tables, Cannot delete of this record";
        }
        return message.respondWithError(msg);
    }


}
