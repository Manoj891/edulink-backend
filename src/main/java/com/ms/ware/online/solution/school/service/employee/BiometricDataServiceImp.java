package com.ms.ware.online.solution.school.service.employee;


import com.ms.ware.online.solution.school.dao.employee.BiometricDataDao;
import com.ms.ware.online.solution.school.dto.BiometricData;
import com.ms.ware.online.solution.school.dto.BiometricReq;
import com.ms.ware.online.solution.school.exception.CustomException;
import com.ms.ware.online.solution.school.entity.employee.EmployeeAttendance;
import com.ms.ware.online.solution.school.entity.employee.EmployeeAttendancePK;
import com.ms.ware.online.solution.school.entity.student.StudentAttendance;
import com.ms.ware.online.solution.school.entity.student.StudentAttendancePK;
import com.ms.ware.online.solution.school.entity.utility.BiometricLog;
import lombok.extern.slf4j.Slf4j;
import com.ms.ware.online.solution.school.config.DateConverted;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class BiometricDataServiceImp implements BiometricDataService {

    @Autowired
    private BiometricDataDao da;

    @Override
    public List<Map<String, Object>> getAttendance(Long employee, String year, String month) {
        String sql = "SELECT id,code,mobile,CONCAT(first_name,' ',middle_name,' ',last_name) empName FROM employee_info WHERE id=IFNULL(" + employee + ",ID) ORDER BY first_name,middle_name,last_name";
        List<Map<String, Object>> list = da.getRecord(sql);
        List<Map<String, Object>> l = new ArrayList<>();
        List<Map<String, Object>> l2;
        for (Map<String, Object> map : list) {
            sql = "select timediff(ifnull(a.out_time, a.in_time), a.in_time) minute, c.bs_date bioDate, a.punch_date bioDateAd, a.in_time inTime, ifnull(a.out_time, 'N/A') outTime from ad_bs_calender c join employee_attendance a on c.BS_DATE like '" + year + "-" + month + "%' and AD_DATE = punch_date and a.emp_id='" + map.get("id") + "' order by bs_date";
            l2 = da.getRecord(sql);
            if (!l2.isEmpty()) map.put("attData", l2);
            l.add(map);
        }

        return l;
    }

    @Override
    public List<Map<String, Object>> getAll(String date) {
        return da.getRecord("select punch_date, punch_time,l.user_id, ifnull(concat(e.first_name, ' ', last_name),' Unmapped') name from biometric_log l left join biometric_device_map m on m.device_id = l.user_id left join employee_info e on m.emp_id = e.id where punch_date = '" + DateConverted.bsToAd(date) + "' order by punch_time desc");
    }


    @Override
    public List<String> save(BiometricData req) {
        List<String> res = new ArrayList<>();
        String password = req.getPassword();
        int branch = req.getBranch();
        if (!password.equals("6344d6ae-684c-42ad-a7e7-e106caacc87b")) throw new CustomException("Invalid credential");
        String date = DateConverted.now();

        req.getData().forEach(r -> {
            if (da.save(BiometricLog.builder().status(true).id(r.getId()).userId(r.getUserId()).branch(branch).punchDate(r.getDate()).punchTime(r.getTime()).build()) == 1) {
                String sql = "select emp_id, student_id,user_type from biometric_device_map where device_id=" + r.getUserId()+" and device_branch="+branch;
                da.getRecord(sql).forEach(map -> {
                    if (map.get("user_type").toString().equalsIgnoreCase("Staff")) {
                        long id = Long.parseLong(map.get("emp_id").toString());
                        da.save(EmployeeAttendance.builder().pk(EmployeeAttendancePK.builder().empId(id).punchDate(DateConverted.toDate(r.getDate())).build()).inTime(r.getTime()).outTime(r.getTime()).enterBy("SYSTEM").updateBy("SYSTEM").enterDate(date).updateDate(date).status("Y").remark("Device").build());
                    } else {
                        long id = Long.parseLong(map.get("student_id").toString());
                        da.save(StudentAttendance.builder().pk(StudentAttendancePK.builder().stuId(id).attDate(DateConverted.toDate(r.getDate())).build()).inTime(r.getTime()).outTime(r.getTime()).enterBy("SYSTEM").updateBy("SYSTEM").enterDate(date).updateDate(date).status("Y").remark("Device").build());
                    }
                    log.info("{} {} {}", r.getDate(), r.getTime(), r.getUserId());
                });
                res.add(r.getId());
            }
        });
        return res;
    }


}
