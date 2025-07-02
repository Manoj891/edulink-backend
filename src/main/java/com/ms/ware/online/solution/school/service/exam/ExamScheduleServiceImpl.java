package com.ms.ware.online.solution.school.service.exam;

import com.ms.ware.online.solution.school.config.DB;
import com.ms.ware.online.solution.school.config.DateConverted;
import com.ms.ware.online.solution.school.dao.exam.ExamScheduleDao;
import com.ms.ware.online.solution.school.dto.ExamScheduleReq;
import com.ms.ware.online.solution.school.entity.exam.ExamSchedule;
import com.ms.ware.online.solution.school.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class ExamScheduleServiceImpl implements ExamScheduleService {
    private final ExamScheduleDao da;
    private final DB db;

    @Override
    public List<Map<String, Object>> getAll(long exam) {
        return db.getRecord("select e.id, class_id as classId, end_time as endTime, GET_BS_DATE(exam_date) as examDate, exam_id as examId, program_id as programId, start_time as startTime, p.name as programName, c.name as className,subject_id subjectId,s.name subjectName,g.name groupName,subject_group subjectGroup from exam_schedule e join class_master c on c.id = e.class_id join program_master p on e.program_id = p.id join subject_master s on s.id = e.subject_id  join subject_group g on g.id = e.subject_group where exam_id=" + exam + " order by exam_date,start_time");
    }

    @Override
    public void save(ExamScheduleReq req) {
        ExamSchedule obj = convert(req);
        String sql = "SELECT ifnull(MAX(ID),0)+1 AS id FROM exam_schedule";
        Map<String, Object> map = db.getRecord(sql).get(0);
        obj.setId(Long.parseLong(map.get("id").toString()));
        int row = da.save(obj);
        String msg = da.getMsg();
        if (row == 0) {
            throw new RuntimeException(msg);
        }
    }

    @Override
    public void update(ExamScheduleReq req, long id) {
        ExamSchedule obj = convert(req);
        obj.setId(id);
        int row = da.update(obj);
        String msg = da.getMsg();
        if (row == 0) {
            throw new RuntimeException(msg);
        }
    }

    @Override
    public void delete(String id) {
        int row = da.delete("DELETE FROM exam_schedule WHERE ID=" + id);
        String msg = da.getMsg();
        if (row == 0) {
            throw new RuntimeException(msg);
        }
    }

    @Override
    public Map<String, Object> getEntranceCard(Long program, Long classId, Long group, Long exam, String section) {
        if (!section.isEmpty()) section = " and S.SECTION='" + section + "'";

        Map<String, Object> map = new HashMap<>();
        map.put("data", db.getRecord("SELECT IFNULL(R.REMARK, '') remark, IFNULL(R.PRESENT_DAYS, '') AS presentDays, IFNULL(R.ABSENT_DAYS, '') AS absentDays, R.EXAM_ROLL_NO examRollNo, R.ID regId, R.`STUDENT_ID` stuId, CONCAT(STU_NAME, ' [', ROLL_NO, ']') stuName, S.ROLL_NO rollNo, P.NAME programName, C.NAME className, S.ACADEMIC_YEAR academicYear,ifnull(R.board_symbol_no,'') symbolNo,ifnull(S.board_regd_no,'') boardRegNo FROM exam_student_registration R join student_info S on S.`ID` = STUDENT_ID join program_master P on P.`ID` = R.`PROGRAM` join class_master C on C.`ID` = R.CLASS_ID WHERE  R.EXAM = " + exam + " AND R.PROGRAM = " + program + " AND R.CLASS_ID = " + classId + " and R.subject_group=" + group + " " + section + " AND R.APPROVE_DATE IS NOT NULL ORDER BY programName, className, stuName"));
        return map;
    }

    private ExamSchedule convert(ExamScheduleReq req) {
        ExamSchedule obj = ExamSchedule.builder()
                .examId(req.getExamId())
                .programId(req.getProgramId())
                .classId(req.getClassId())
                .subjectGroup(req.getSubjectGroup())
                .subjectId(req.getSubjectId())
                .examDate(DateConverted.bsToAdDate(req.getExamDate()))
                .startTime(DateConverted.toTime(req.getStartTime()))
                .endTime(DateConverted.toTime(req.getEndTime()))
                .build();
        if (obj.getExamDate() == null || obj.getStartTime() == null || obj.getEndTime() == null) {
            throw new CustomException("Exam Date or Start Time or End Time is null, Please provide a valid exam date and time");
        }
        return obj;
    }

    @Override
    public Map<String, Object> getEntranceCard(String regNos, long examId, long program, long classId, long groupId) {
        Map<String, Object> map = new HashMap<>();
        map.put("data", db.getRecord("SELECT S.STU_NAME stuName, C.NAME className, S.ROLL_NO rollNo, S.ID regNo, IFNULL(S.SECTION, '') as section, ifnull(remark, '') as remark, board_symbol_no, fathers_name, upper(E.EXAM_NAME) examName FROM student_info S join exam_student_registration R on S.ID = R.STUDENT_ID join exam_master E on R.EXAM = E.ID join class_master C on R.CLASS_ID = C.ID where E.ID=" + examId + " AND R.ID IN(" + regNos + ") ORDER BY section,rollNo"));
        map.put("schedule",db.getRecord("select exam_date examData, start_time startTime, end_time endTime,m.name subjectName from exam_schedule s join subject_master m on s.subject_id=m.id where exam_id = "+examId+" and program_id = "+  program+" and class_id = "+classId+" and subject_group = "+groupId+" order by examData,startTime"));
        return map;
    }

}
