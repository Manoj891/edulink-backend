/*    map = new com.fasterxml.jackson.databind.ObjectMapper().readValue(jsonData, new com.fasterxml.jackson.core.type.TypeReference<>() {});
 */
package com.ms.ware.online.solution.school.service.exam;


import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.exam.ExamStudentRegistrationDao;
import com.ms.ware.online.solution.school.dto.ExamStudentAttendance;
import com.ms.ware.online.solution.school.dto.ExamStudentRegistrationApprove;
import com.ms.ware.online.solution.school.exception.CustomException;
import com.ms.ware.online.solution.school.exception.PermissionDeniedException;
import com.ms.ware.online.solution.school.entity.exam.ExamStudentRegistration;
import com.ms.ware.online.solution.school.config.Message;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ms.ware.online.solution.school.config.DB;
import com.ms.ware.online.solution.school.config.DateConverted;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExamStudentRegistrationServiceImp implements ExamStudentRegistrationService {

    @Autowired
    ExamStudentRegistrationDao da;
    Message message = new Message();
    String msg = "", sql;
    int row;
    @Autowired
    private AuthenticationFacade facade;

    @Override
    public Object getRecord(Long program, Long classId, Long exam) {
        return da.getRecord("SELECT s.STUDENT_ID AS stuId, sd.STU_NAME AS stuName, s.ROLL_NO AS rollNo, pm.NAME AS programName, cm.NAME AS className, s.ACADEMIC_YEAR AS academicYear, s.SUBJECT_GROUP AS subjectGroup FROM class_transfer s JOIN student_info sd ON s.STUDENT_ID = sd.ID JOIN exam_master em ON s.ACADEMIC_YEAR = em.ACADEMIC_YEAR JOIN program_master pm ON pm.ID = s.PROGRAM JOIN class_master cm ON cm.ID = s.CLASS_ID LEFT JOIN exam_student_registration r ON r.STUDENT_ID = s.STUDENT_ID AND r.EXAM = em.ID WHERE em.ID = " + exam + " AND s.PROGRAM = " + program + " AND s.CLASS_ID = " + classId + " AND r.STUDENT_ID IS NULL AND (sd.DROP_OUT IS NULL OR sd.DROP_OUT != 'Y') ORDER BY s.CLASS_ID, stuName, s.ROLL_NO;");
    }

    @Override
    public Object getPending(Long program, Long classId, Long exam) {
        return da.getRecord("SELECT r.id regId,ifnull(r.board_symbol_no,r.id) symbolNo, student_id stuId, s.`stu_name` stuName, s.roll_no rollNo, p.`name` programName, c.`name` className, r.year academicYear, ifnull(r.approve_date, '') status from exam_student_registration r join student_info s on s.`id` = r.`student_id` join program_master p on p.`id` = r.`program` join class_master c on c.`id` = r.`class_id` where exam =" + exam + " and r.program = " + program + " and r.class_id = " + classId + " order by programName, className, stuName");
    }

    @Override
    public Object getApprove(Long program, Long classId, Long exam, String section) {
        section = section.isEmpty() ? "ifnull(S.SECTION,'')" : "'" + section + "'";
        return da.getRecord("SELECT IFNULL(R.REMARK, '') remark, IFNULL(R.PRESENT_DAYS, '') AS presentDays, IFNULL(R.ABSENT_DAYS, '') AS absentDays, R.EXAM_ROLL_NO examRollNo, R.ID regId, R.`STUDENT_ID` stuId, CONCAT(STU_NAME, ' [', ROLL_NO, ']') stuName, S.ROLL_NO rollNo, P.NAME programName, C.NAME className, S.ACADEMIC_YEAR academicYear,ifnull(R.board_symbol_no,'') symbolNo,ifnull(S.board_regd_no,'') boardRegNo FROM exam_student_registration R join student_info S on S.`ID` = STUDENT_ID join program_master P on P.`ID` = R.`PROGRAM` join class_master C on C.`ID` = R.CLASS_ID WHERE R.EXAM = " + exam + " AND R.PROGRAM = " + program + " AND R.CLASS_ID = " + classId + " and ifnull(S.SECTION,'') = " + section + " AND R.APPROVE_DATE IS NOT NULL ORDER BY programName, className, stuName");
    }

    @Override
    public Object save(String jsonData) {
        AuthenticatedUser td = facade.getAuthentication();;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        ExamStudentRegistration obj;
        String[] jsonDataArray = message.jsonDataToStringArray(jsonData);
        try {
            message.map = new com.fasterxml.jackson.databind.ObjectMapper().readValue(jsonDataArray[0], new com.fasterxml.jackson.core.type.TypeReference<>() {
            });
            long exam = Long.parseLong(message.map.get("exam").toString());
            long program = Long.parseLong(message.map.get("program").toString());
            long classId = Long.parseLong(message.map.get("classId").toString());
            Date date = DateConverted.bsToAdDate(message.map.get("date").toString());
            sql = "SELECT ACADEMIC_YEAR year FROM exam_master WHERE ID=" + exam;
            message.map = (Map) da.getRecord(sql).get(0);
            long year = Long.parseLong(message.map.get("year").toString());
            message.list = new com.fasterxml.jackson.databind.ObjectMapper().readValue(jsonDataArray[1], new com.fasterxml.jackson.core.type.TypeReference<List>() {
            });
            String enterBy = td.getUserName();
            long studentId, subjectGroup;
            row = 0;
            for (int i = 0; i < message.list.size(); i++) {
                obj = new ExamStudentRegistration();
                message.map = (Map) message.list.get(i);
                studentId = Long.parseLong(message.map.get("stuId").toString());
                subjectGroup = Long.parseLong(message.map.get("subjectGroup").toString());
                obj.setStudentId(studentId);
                obj.setSubjectGroup(subjectGroup);
                obj.setExam(exam);
                obj.setClassId(classId);
                obj.setProgram(program);
                obj.setYear(year);
                obj.setEnterBy(enterBy);
                obj.setEnterDate(date);
                try {
                    sql = "SELECT ifnull(MAX(ID),0)+1 AS id FROM exam_student_registration";
                    message.map = (Map) da.getRecord(sql).get(0);
                    obj.setId(Long.parseLong(message.map.get("id").toString()));
                } catch (Exception e) {
                    continue;
                }

                row += da.save(obj);
            }
            msg = da.getMsg();
            if (row > 0) {
                return message.respondWithMessage(row + " Student Registred!!");
            } else if (msg.contains("Duplicate entry")) {
                msg = "This record already exist";
            }
            return message.respondWithError(msg);

        } catch (Exception e) {
            return message.respondWithError(e.getMessage());
        }
    }

    @Override
    public Object approve(ExamStudentRegistrationApprove req) {
        AuthenticatedUser td = facade.getAuthentication();;
        try {
            String date = DateConverted.bsToAd(req.getDate());
            String id, year, classId, examRollSn, examRollNo, enterBy = td.getUserName();


            row = 0;
            for (int i = 0; i < req.getObj().size(); i++) {
                id = req.getObj().get(i);
                sql = "SELECT YEAR year,CLASS_ID classId FROM exam_student_registration WHERE ID=" + id;
                message.map = da.getRecord(sql).get(0);
                year = message.map.get("year").toString();
                classId = message.map.get("classId").toString();
                sql = "SELECT IFNULL(MAX(EXAM_ROLL_SN),0)+1 examRollSn FROM exam_student_registration WHERE YEAR='" + year + "' AND CLASS_ID=" + classId;
                message.map = da.getRecord(sql).get(0);
                examRollSn = message.map.get("examRollSn").toString();
                if (classId.length() == 1) {
                    classId = "0" + classId;
                }
                if (examRollSn.length() == 1) {
                    examRollNo = year + classId + "00" + examRollSn;
                } else if (examRollSn.length() == 2) {
                    examRollNo = year + classId + "0" + examRollSn;
                } else {
                    examRollNo = year + classId + examRollSn;
                }
                sql = "UPDATE exam_student_registration SET EXAM_ROLL_NO='" + examRollNo + "',EXAM_ROLL_SN='" + examRollSn + "',APPROVE_BY='" + enterBy + "',APPROVE_DATE='" + date + "' WHERE ID='" + id + "'";
                row = row + da.delete(sql);

            }
            msg = da.getMsg();
            if (row > 0) {
                return message.respondWithMessage(row + " Student Registred!!");
            } else if (msg.contains("Duplicate entry")) {
                msg = "This record already exist";
            }
            return message.respondWithError(msg);

        } catch (Exception e) {
            return message.respondWithError(e.getMessage());
        }
    }

    @Override
    public Object delete(String id) {

        for (Map<String, Object> map : da.getRecord("select ifnull(APPROVE_DATE,'') approve_date from exam_mark_entry where exam_reg_id=" + id)) {
            if (!map.get("approve_date").toString().isEmpty()) throw new CustomException("Exam mark entry approved");
        }
        row = da.delete("delete from exam_mark_entry where exam_reg_id=" + id);
        row = da.delete("delete from exam_student_registration where id=" + id);
        msg = da.getMsg();
        if (row > 0) {
            return message.respondWithMessage("Success");
        } else if (msg.contains("foreign key")) {
            msg = "this record already used in reference tables, Cannot delete of this record";
        }
        throw new CustomException(msg);
    }

    @Override
    public Object doStudentAttendance(List<ExamStudentAttendance> req) {
        String sql;
        try {
            row = 0;
            for (ExamStudentAttendance d : req) {
                sql = "UPDATE exam_student_registration SET REMARK='" + d.getRemark() + "',PRESENT_DAYS=" + d.getPresentDays() + ",board_symbol_no='" + d.getSymbolNo() + "' WHERE EXAM_ROLL_NO='" + d.getExamRollNo() + "'";
                row = row + da.delete(sql);
                try {
                    if (d.getBoardRegNo().length() > 2) {
                        da.delete("update student_info set board_regd_no='" + d.getBoardRegNo() + "' where ID=" + d.getRegNo());
                    }
                } catch (Exception ignored) {
                }
            }
            return message.respondWithMessage(row + " Record Saved!!");
        } catch (Exception e) {
            return message.respondWithError(e.getMessage());
        }
    }


    @Override
    public Map<String, Object> getRegistration(long examId, long regNo) {
        sql = "select class_id  classId, program program, subject_group groupId from exam_student_registration where exam=" + examId + " and student_id=" + regNo;
        List<Map<String, Object>> list = new DB().getRecord(sql);
        if (list.isEmpty()) throw new CustomException("Student Not Register");
        return list.get(0);
    }

    @Override
    public Map<String, Object> updateRegistration(long examId, long regNo, long program, long classId, long groupId) {
        AuthenticatedUser user = facade.getAuthentication();;
        if (!user.getUserType().equalsIgnoreCase("ADM")) {
            throw new PermissionDeniedException();
        }
        sql = "update exam_student_registration set program=" + program + ",class_id=" + classId + ",subject_group=" + groupId + " where exam=" + examId + " and student_id=" + regNo;
        new DB().delete(sql);
        return getRegistration(examId, regNo);
    }
}
