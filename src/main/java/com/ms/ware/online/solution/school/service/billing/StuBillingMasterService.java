package com.ms.ware.online.solution.school.service.billing;

import com.ms.ware.online.solution.school.dto.BillingMasterReq;
import com.ms.ware.online.solution.school.dto.StuBillingMasterWav;

import java.util.Map;

public interface StuBillingMasterService {

    Object getAll(String regNo, String year, String month);

    Object getAll(String billNo);

    String save(BillingMasterReq req);

    Object saveOthers(String jsonData);

    Object wavFee(StuBillingMasterWav req);

    Object findCreditBill(Long regNo, Long rollNo, Long academicYear, Long program, Long classId, String year, String month);

    Object manageCredit(String jsonData);

    String delete(String id, String reason);

}
