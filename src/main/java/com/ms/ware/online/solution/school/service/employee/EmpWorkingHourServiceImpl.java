package com.ms.ware.online.solution.school.service.employee;

import com.ms.ware.online.solution.school.dao.employee.EmployeeInfoDao;
import com.ms.ware.online.solution.school.entity.employee.EmpWorkingHour;
import com.ms.ware.online.solution.school.entity.employee.EmpWorkingHourPK;
import java.util.List;
import java.util.Map;
import com.ms.ware.online.solution.school.config.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmpWorkingHourServiceImpl implements EmpWorkingHourService {

    @Autowired
    EmployeeInfoDao da;
    Message message = new Message();
    String days[] = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

    @Override
    public List<EmpWorkingHour> index(Long empId) {
        String sql = "SELECT ID id,CONCAT(first_name,' ',middle_name,' ',last_name) name FROM employee_info  where id=ifnull(" + empId + ",id)";
        List list = da.getRecord(sql);
        List empWorkingHour;
        Map map;

        for (int i = 0; i < list.size(); i++) {
            map = (Map) list.get(i);
            empId = Long.parseLong(map.get("id").toString());
            sql = "SELECT H.WORKING_DAY workingDay,LATE_IN_TIME lateInTime,LATE_OUT_TIME lateOutTime,IN_TIME inTime,OUT_TIME outTime FROM day_order_master O,emp_working_hour H WHERE O.NAME=H.WORKING_DAY  AND EMP_ID='" + empId + "' ORDER BY O.ID";
            empWorkingHour = da.getRecord(sql);
            if (empWorkingHour.size() != days.length) {
                saveDays(empId);
                empWorkingHour = da.getRecord(sql);
                map.put("days", empWorkingHour);
            } else {
                map.put("days", empWorkingHour);
            }
            list.remove(i);
            list.add(i, map);
        }

        return list;
    }

    @Override
    public Object doSave(List<EmpWorkingHour> list) {

        try {
            EmpWorkingHour obj;
            for (int i = 0; i < list.size(); i++) {
                obj = list.get(i);

                obj.setPk(new EmpWorkingHourPK(obj.getEmpId(), obj.getWorkingDay()));
                if (obj.getInTime().length() < 8 || obj.getOutTime().length() < 8 || obj.getLateInTime().length() < 8 || obj.getLateOutTime().length() < 8) {
                    continue;
                }
                da.save(obj);
                try {
//                    repository.updateWorkingTime(obj.getInTime(), obj.getLateInTime(), obj.getOutTime(), obj.getLateOutTime(), empId, obj.getWorkingDay());
                } catch (Exception e) {
                }
            }
//            repository.updateWorkingMinute();
            return message.respondWithMessage("Success");
        } catch (Exception e) {
            return message.respondWithError(e.getMessage());
        }

    }

    void saveDays(Long empId) {
        for (int i = 0; i < days.length; i++) {
            EmpWorkingHour empWorkingHour = new EmpWorkingHour();
            empWorkingHour.setPk(new EmpWorkingHourPK(empId, days[i]));
            empWorkingHour.setInTime("");
            empWorkingHour.setLateInTime("");
            empWorkingHour.setLateOutTime("");
            empWorkingHour.setOutTime("");
            da.save(empWorkingHour);
        }
    }

}
