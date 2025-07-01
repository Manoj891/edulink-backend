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
}
