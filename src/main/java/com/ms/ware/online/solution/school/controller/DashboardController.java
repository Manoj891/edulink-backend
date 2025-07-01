package com.ms.ware.online.solution.school.controller;

import com.ms.ware.online.solution.school.config.DB;
import com.ms.ware.online.solution.school.config.DateConverted;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("api/dashboard")
public class DashboardController {
    @GetMapping("/{date}")
    public Map<String, Object> getDashboard(@PathVariable String date) {
        String year = Objects.requireNonNull(DateConverted.adToBs(date)).substring(2, 4);
        DB db = new DB();
        AtomicInteger totalPresent = new AtomicInteger();
        AtomicInteger totalAbsent = new AtomicInteger();
        String sql = "SELECT c.ID AS classId, c.NAME AS className,count(if(a.status is null or a.status='N', 1, null)) as absent,count(if(a.status='Y', 1, null)) as present FROM student_info s JOIN class_master c ON c.ID = s.CLASS_ID LEFT JOIN student_attendance a ON s.ID = a.STU_ID AND a.ATT_DATE = '" + date + "' WHERE s.ACADEMIC_YEAR = " + year + " AND COALESCE(s.DROP_OUT, 'N') = 'N' GROUP BY c.ID, c.NAME ORDER BY c.ID";
        List<Object> l2 = new ArrayList<>();
        l2.add(Arrays.asList("Class", "Present", "Absent"));
        db.getRecord(sql).forEach(m -> {
            int absent = Integer.parseInt(m.get("absent").toString());
            int present = Integer.parseInt(m.get("present").toString());
            l2.add(Arrays.asList(m.get("className"), present, absent));
            totalPresent.addAndGet(present);
            totalAbsent.addAndGet(absent);
        });
        Map<String, Object> map = new HashMap<>();
        map.put("data", l2);
        map.put("totalPresent", totalPresent);
        map.put("totalAbsent", totalAbsent);
        return map;
    }

    @GetMapping("/{date}/{student}")
    public List<Map<String, Object>> getStudentDashboard(@PathVariable String date, @PathVariable String student, @RequestParam(required = false) Long classId) {
        String year = Objects.requireNonNull(DateConverted.adToBs(date)).substring(2, 4);
        if (student.equalsIgnoreCase("A")) {
            return new DB().getRecord("SELECT s.id AS id, s.stu_name AS stuName, 'Absent' AS inTime, '' AS outTime, c.name AS className, s.mobile_no AS remark FROM student_info s JOIN class_master c ON s.class_id = c.id LEFT JOIN student_attendance a ON s.id = a.stu_id AND a.att_date = '" + date + "' WHERE s.class_id=ifnull(" + classId + ",s.class_id)  and (a.status is null or a.status='N') and s.ACADEMIC_YEAR = " + year + " AND COALESCE(s.DROP_OUT, 'N') = 'N'  ORDER BY stuName");
        }
        return new DB().getRecord("select s.id id,stu_name stuName,a.in_time inTime,out_time outTime,c.NAME className,ifnull(a.remark,'Present') remark from student_attendance a join student_info s on a.STU_ID=s.ID and a.ATT_DATE='" + date + "' join class_master c on s.CLASS_ID=c.ID where s.class_id=ifnull(" + classId + ",s.class_id)  and a.status='Y' and s.ACADEMIC_YEAR = " + year + " AND COALESCE(s.DROP_OUT, 'N') = 'N'  order by outTime desc,inTime desc");
    }
}
