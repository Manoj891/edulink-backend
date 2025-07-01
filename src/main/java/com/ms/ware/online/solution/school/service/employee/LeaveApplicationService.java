package com.ms.ware.online.solution.school.service.employee;

import com.ms.ware.online.solution.school.entity.employee.EmpLeaveDetail;
import org.springframework.http.ResponseEntity;
import com.ms.ware.online.solution.school.entity.employee.LeaveApplication;
import java.util.List;

public interface LeaveApplicationService {

    public ResponseEntity getAll();

    public ResponseEntity save(LeaveApplication obj);

    public ResponseEntity update(LeaveApplication obj,long id);

    public ResponseEntity delete(String id);

    public ResponseEntity getAll(Long id);

    public ResponseEntity leaveApprove(List<EmpLeaveDetail> obj, long id);

    public ResponseEntity report(Long empId, String leaveDateFrom, String leaveDateTo);

}