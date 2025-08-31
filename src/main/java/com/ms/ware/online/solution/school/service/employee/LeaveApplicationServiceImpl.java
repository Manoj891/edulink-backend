package com.ms.ware.online.solution.school.service.employee;

import com.ms.ware.online.solution.school.config.EmailService;

import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.employee.LeaveApplicationDao;
import com.ms.ware.online.solution.school.entity.employee.EmpLeaveDetail;
import com.ms.ware.online.solution.school.entity.employee.EmpLeaveDetailPK;
import com.ms.ware.online.solution.school.entity.employee.LeaveApplication;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ms.ware.online.solution.school.config.DB;
import com.ms.ware.online.solution.school.config.DateConverted;
import com.ms.ware.online.solution.school.config.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LeaveApplicationServiceImpl implements LeaveApplicationService {
    @Autowired
    private DB db;
    @Autowired
    private LeaveApplicationDao da;
    @Autowired
    private AuthenticationFacade facade;
    @Autowired
    private EmailService es ;

    @Override
    public ResponseEntity getAll() {
        return ResponseEntity.ok(da.getAll("from LeaveApplication where approveDate is null"));
    }

    @Override
    public ResponseEntity getAll(Long id) {
        Message message = new Message();
        List<LeaveApplication> applications = da.getAll("from LeaveApplication where id=" + id);
        if (applications.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message.respondWithMessage(message.respondWithError("Invalid ID")));
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String sql = "SELECT BS_DATE date FROM ad_bs_calender WHERE AD_DATE BETWEEN '" + dateFormat.format(applications.get(0).getLeaveDateFromAd()) + "' AND '" + dateFormat.format(applications.get(0).getLeaveDateToAd()) + "'";
        Map map = new HashMap();
        map.put("empName", applications.get(0).getEmployeeInfo().getFirstName() + " " + applications.get(0).getEmployeeInfo().getMiddleName() + " " + applications.get(0).getEmployeeInfo().getLastName());
        map.put("mobile", applications.get(0).getEmployeeInfo().getMobile());
        map.put("email", applications.get(0).getEmployeeInfo().getEmail());
        map.put("empId", applications.get(0).getEmployeeInfo().getId());
        map.put("id", applications.get(0).getId());
        map.put("data", da.getRecord(sql));

        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    @Override
    public ResponseEntity save(LeaveApplication obj) {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        try {
            String sql = "SELECT ifnull(MAX(ID),0)+1 AS id FROM leave_application";
            message.map = (Map) da.getRecord(sql).get(0);
            obj.setId(Long.parseLong(message.map.get("id").toString()));
            obj.setEnterDate(new Date());
            obj.setEnterBy(td.getUserName());
        } catch (NumberFormatException e) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message.respondWithMessage("connection error or invalid table name"));
        }
        da.save(obj);
        return ResponseEntity.status(HttpStatus.CREATED).body(message.respondWithMessage("Success"));

    }

    @Override
    public ResponseEntity update(LeaveApplication obj, long id) {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        obj.setId(id);
        obj.setEnterDate(new Date());
        obj.setEnterBy(td.getUserName());
        da.update(obj);
        return ResponseEntity.status(HttpStatus.CREATED).body(message.respondWithMessage("Success"));
    }

    @Override
    public ResponseEntity delete(String id) {
        Message message = new Message();
        da.delete(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(message.respondWithMessage(""));
    }

    @Override
    public ResponseEntity leaveApprove(List<EmpLeaveDetail> obj, long id) {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        for (int i = 0; i < obj.size(); i++) {
            obj.get(i).setLeaveId(id);
            obj.get(i).setPk(new EmpLeaveDetailPK(DateConverted.bsToAdDate(obj.get(i).getLeaveDate()), obj.get(i).getEmpId()));
        }
        if (da.save(obj) > 0) {
           
            String sql = "UPDATE leave_application SET STATUS='Y',APPROVE_DATE=SYSDATE(),APPROVE_BY='" + td.getUserName() + "' WHERE ID=" + obj.get(0).getLeaveId();
            db.save(sql);
            new Thread(() -> {
                String sql1 = "SELECT E.email email,E.first_name firstName,L.reasons reasons,get_bs_date(L.leave_date_from) leaveFrom,get_bs_date(L.leave_date_to) leaveTo FROM employee_info E,leave_application L WHERE E.id=L.emp_id and L.id=" + obj.get(0).getLeaveId();
                Map<String, Object> map = db.getMapRecord(sql1).get(0);
                es.sendmail(map.get("email").toString(), "LEAVE APPLICATION APPROVED.", "Dear " + map.get("firstName") + ",<br>"
                        + "Your leave application approve from " + map.get("leaveFrom") + " ~ " + map.get("leaveTo") + "<br>" + map.get("reasons"));
            }).start();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(obj);
    }

    @Override
    public ResponseEntity report(Long empId, String leaveDateFrom, String leaveDateTo) {

        if (leaveDateTo.length() == 10 && leaveDateFrom.length() == 10) {
            leaveDateFrom = DateConverted.bsToAd(leaveDateFrom);
            leaveDateTo = DateConverted.bsToAd(leaveDateTo);
        } else if (leaveDateTo.length() == 10) {
            leaveDateTo = DateConverted.bsToAd(leaveDateTo);
            leaveDateFrom = DateConverted.toString(DateConverted.addDate(DateConverted.toDate(leaveDateTo), (-365)));
        } else if (leaveDateFrom.length() == 10) {
            leaveDateFrom = DateConverted.bsToAd(leaveDateFrom);
            leaveDateTo = DateConverted.toString(DateConverted.addDate(DateConverted.toDate(leaveDateFrom), (365)));
        } else {
            leaveDateTo = DateConverted.today();
            leaveDateFrom = DateConverted.toString(DateConverted.addDate(DateConverted.toDate(leaveDateTo), (-365)));
        }

        String sql = "SELECT CONCAT(first_name,' ',middle_name,' ',last_name) empName,mobile,email,id FROM employee_info  WHERE id=ifnull(" + empId + ",id) ORDER BY empName";
        List list = da.getRecord(sql);
        List ll, l = new ArrayList();
        Map map;
        for (int i = 0; i < list.size(); i++) {
            map = (Map) list.get(i);
            sql = "SELECT GET_BS_DATE(D.LEAVE_DATE) leaveDate,D.PAY_TYPE payType,A.REASONS reasons,A.`APPROVE_BY` approveBy,GET_BS_DATE(A.`APPROVE_DATE`) approveDate FROM emp_leave_detail D,leave_application A WHERE D.LEAVE_ID=A.ID AND D.EMP_ID=" + map.get("id") + " AND D.LEAVE_DATE BETWEEN '" + leaveDateFrom + "' AND '" + leaveDateTo + "' ORDER BY leaveDate";
            ll = da.getRecord(sql);
            if (ll.isEmpty()) {
                continue;
            }
            map.put("detail", ll);
            l.add(map);
        }
        return ResponseEntity.status(HttpStatus.OK).body(l);
    }

}
