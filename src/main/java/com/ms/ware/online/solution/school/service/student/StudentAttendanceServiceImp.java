
package com.ms.ware.online.solution.school.service.student;


import com.ms.ware.online.solution.school.config.DateConverted;
import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.student.StudentAttendanceDao;
import com.ms.ware.online.solution.school.entity.student.StudentAttendance;
import com.ms.ware.online.solution.school.entity.student.StudentAttendancePK;
import com.ms.ware.online.solution.school.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class StudentAttendanceServiceImp implements StudentAttendanceService {
    @Autowired
    private AuthenticationFacade facade;
    @Autowired
    private StudentAttendanceDao da;
    @Autowired
    private Message message;
    @Override
    public List<Map<String, Object>> getAttendance(Long academicYear, Long program, Long classId, Long subjectGroup, String year, String month, String section, String type) {
        if (section != null && !section.isEmpty()) {
            section = " and c.section='" + section + "'";
        } else {
            section = "";
        }
        String sql = "select min(ad_date) start,max(ad_date) end,count(*) days from ad_bs_calender where BS_DATE like '" + year + "-" + month + "%' and ad_date<='" + DateConverted.now() + "' and STUDENT_HOLYDAY!='Y'";
        List<Map<String, Object>> list = da.getRecord(sql);
        if (list.isEmpty()) {
            throw new CustomException("Invalid date range");
        }
        String start = list.get(0).get("start").toString();
        String end = list.get(0).get("end").toString();
        String days = list.get(0).get("days").toString();

        if (!type.equalsIgnoreCase("1")) {
            return da.getRecord("SELECT s.id, s.mobile_no mobile, stu_name name, cm.name className, pm.name programName, sg.name groupName,'" + days + "' days, count((case when (a.att_date is not null and a.status='Y')  then 1 end)) present from student_info s join class_transfer c on s.id = c.student_id join class_master cm on c.class_id = cm.id join program_master pm on c.program = pm.id join subject_group sg on c.subject_group = sg.id left join student_attendance a on s.ID = a.STU_ID and a.att_date between '" + start + "' and '" + end + "' where c.academic_year=ifnull(" + academicYear + ",c.academic_year) and c.program=ifnull(" + program + ",c.program) and c.class_id=ifnull(" + classId + ",c.class_id) and c.subject_group=ifnull(" + subjectGroup + ",c.subject_group) " + section + " group by s.id, s.mobile_no, stu_name, cm.name, pm.name, sg.name");
        }
        sql = "SELECT s.id, s.mobile_no mobile, stu_name name, CM.name className, PM.name programName, SG.name groupName from student_info s join class_transfer c on s.id = c.student_id join class_master CM on c.class_id = CM.id join program_master PM on c.program = PM.id  join subject_group SG on c.subject_group = SG.id where c.academic_year=ifnull(" + academicYear + ",c.academic_year) and c.program=ifnull(" + program + ",c.program) and c.class_id=ifnull(" + classId + ",c.class_id) and c.subject_group=ifnull(" + subjectGroup + ",c.subject_group) " + section + "  order by name limit 1000";
        list = da.getRecord(sql);
        List<Map<String, Object>> l = new ArrayList<>();
        for (Map<String, Object> map : list) {
            sql = "select att_date bioDate, in_time inTime, ifnull(a.out_time, 'N/A') outTime from student_attendance a where stu_id = '" + map.get("id") + "' and status = 'Y' and att_date between '" + start + "' and '" + end + "' order by bioDate";
            map.put("data", da.getRecord(sql));
            l.add(map);
        }
        return l;
    }

    @Override
    public List<Map<String, Object>> getAll(Long academicYear, Long program, Long classId, Long subjectGroup, String date) {
        date = DateConverted.bsToAd(date);
        String sql = "SELECT S.STU_NAME stuName, ROLL_NO rollNo, SECTION section, S.ID stuId, ifnull(A.STATUS, '') status, IFNULL(IN_TIME, '') inTime, IFNULL(OUT_TIME, '') outTime, IFNULL(A.ENTER_BY, '') enterBy, IFNULL(A.ENTER_DATE, '') enterDate, CM.name className, PM.name programName, SG.name groupName FROM student_info S join class_master CM on CM.id = CLASS_ID join program_master PM on PM.id = program join subject_group SG on SG.id = subject_group left join student_attendance A on A.STU_ID = S.ID AND ATT_DATE='" + date + "' WHERE ACADEMIC_YEAR=ifnull(" + academicYear + ",ACADEMIC_YEAR) AND PROGRAM=ifnull(" + program + ",PROGRAM) AND SUBJECT_GROUP=ifnull(" + subjectGroup + ",SUBJECT_GROUP) AND CLASS_ID=ifnull(" + classId + ",CLASS_ID) and ifnull(ATT_DATE,'" + date + "') = '" + date + "' order by enterDate desc,inTime desc,stuName";
        return da.getRecord(sql);
    }

    @Override
    public ResponseEntity save(List<StudentAttendance> list) {
        
        AuthenticatedUser td = facade.getAuthentication();
        String enterBy = td.getUserName();
        String enterDate = DateConverted.now();
        int row = 0;
        String msg;
        try {
            StudentAttendance obj;
            for (StudentAttendance studentAttendance : list) {
                obj = studentAttendance;
                Date attDate = DateConverted.toDate(DateConverted.bsToAd(obj.getAttDate()));
                obj.setPk(StudentAttendancePK.builder().stuId(obj.getStuId()).attDate(attDate).build());
                obj.setEnterBy(enterBy);
                obj.setEnterDate(enterDate);
                obj.setUpdateBy(enterBy);
                obj.setUpdateDate(enterDate);
                row += da.save(obj);
            }
            msg = da.getMsg();
            if (row > 0) {
                return ResponseEntity.status(200).body(message.respondWithMessage(row + " Record Saved!!"));
            } else if (msg.contains("Duplicate entry")) {
                msg = "This record already exist";
            }
            return ResponseEntity.status(200).body(message.respondWithError(msg));

        } catch (Exception e) {
            return ResponseEntity.status(200).body(message.respondWithError(e.getMessage()));
        }
    }

    @Override
    public ResponseEntity update(StudentAttendance obj) {
        
        AuthenticatedUser td = facade.getAuthentication();
        int row;
        String msg = "";
        row = da.update(obj);
        msg = da.getMsg();
        if (row > 0) {
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
        
        AuthenticatedUser td = facade.getAuthentication();
        int row;
        String msg = "", sql;
        sql = "DELETE FROM student_attendance WHERE ID='" + id + "'";
        row = da.delete(sql);
        msg = da.getMsg();
        if (row > 0) {
            return ResponseEntity.status(200).body(message.respondWithMessage("Success"));
        } else if (msg.contains("foreign key")) {
            msg = "this record already used in reference tables, Cannot delete of this record";
        }
        return ResponseEntity.status(200).body(message.respondWithError(msg));
    }

    @Override
    public ResponseEntity saveTeacher(List<StudentAttendance> list) {

        
        AuthenticatedUser td = facade.getAuthentication();
        int row = 0;
        String msg;
        String enterBy = td.getUserName();
        String enterDate = DateConverted.now();
        try {
            StudentAttendance obj;
            for (StudentAttendance studentAttendance : list) {
                obj = studentAttendance;
                Date attDate = DateConverted.toDate(DateConverted.bsToAd(obj.getAttDate()));
                obj.setPk(new StudentAttendancePK(obj.getStuId(), attDate));
                obj.setEnterBy(enterBy);
                obj.setEnterDate(enterDate);
                row += da.save(obj);
            }
            msg = da.getMsg();
            if (row > 0) {
                return ResponseEntity.status(200).body(message.respondWithMessage(row + " Record Saved!!"));
            } else if (msg.contains("Duplicate entry")) {
                msg = "This record already exist";
            }
            return ResponseEntity.status(200).body(message.respondWithError(msg));

        } catch (Exception e) {
            return ResponseEntity.status(200).body(message.respondWithError(e.getMessage()));
        }

    }

}
