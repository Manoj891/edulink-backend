package com.ms.ware.online.solution.school.controller.billing;

import com.ms.ware.online.solution.school.config.DB;
import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.student.StudentTransportationDao;
import com.ms.ware.online.solution.school.dto.HostelTransportation;
import com.ms.ware.online.solution.school.dto.PostHostelTransportation;
import com.ms.ware.online.solution.school.config.DateConverted;
import com.ms.ware.online.solution.school.model.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("api/billing/post-hostel-transportation")
public class PostHostelTransportationController {
    @Autowired
    private StudentTransportationDao da;
    @Autowired
    private DB db;
    @Autowired
    private AuthenticationFacade facade;


    @PostMapping
    public ResponseEntity<String> save(@RequestBody HostelTransportation r) {
        AuthenticatedUser user = facade.getAuthentication();
        String username = user.getUserName();
        String today = DateConverted.today();
        String effectDate = DateConverted.bsToAd(r.getYear() + "-" + r.getMonth() + "-01");
        String sql = "select id from fiscal_year where '" + today + "' between start_date and end_date";
        long fiscalYear = Long.parseLong(db.getRecord(sql).get(0).get("id").toString());
        r.getDetail().forEach(d -> generate(effectDate, today, fiscalYear, r.getAcademicYear(), r.getClassId(), r.getProgram(), r.getYear(), r.getMonth(), d, username));
        return ResponseEntity.status(HttpStatus.OK).body("{\"message\":\"Success\"}");
    }

    private void generate(String effectDate, String today, long fiscalYear, long academicYear, long classId, long program, long year, String month, PostHostelTransportation d, String enterBy) {
        Session session = HibernateUtil.getSession();
        Transaction tr = session.beginTransaction();
        String sql = "SELECT IFNULL(max(BILL_SN),0)+1 AS billSn FROM stu_billing_master WHERE FISCAL_YEAR='" + fiscalYear + "' AND BILL_TYPE='CR'";
        Map<String, Object> map = da.getRecord(sql).get(0);
        long billSn = Long.parseLong(map.get("billSn").toString());
        String billNo;
        if (billSn < 10) {
            billNo = "CR" + fiscalYear + "000" + billSn;
        } else if (billSn < 100) {
            billNo = "CR" + fiscalYear + "00" + billSn;
        } else if (billSn < 1000) {
            billNo = "CR" + fiscalYear + "0" + billSn;
        } else {
            billNo = "CR" + fiscalYear + billSn;
        }
        try {
            sql = "INSERT INTO stu_billing_master(BILL_NO,BILL_SN,BILL_TYPE,REG_NO,ACADEMIC_YEAR,PROGRAM,CLASS_ID,SUBJECT_GROPU,FISCAL_YEAR,ENTER_BY,ENTER_DATE,APPROVE_BY,APPROVE_DATE,AUTO_GENERATE) VALUES('" + billNo + "'," + billSn + ",'CR', '" + d.getId() + "', '" + academicYear + "'," + program + ",'" + classId + "','" + d.getGroup() + "','" + fiscalYear + "','" + enterBy + "','" + today + "','" + enterBy + "','" + today + "','N')";
            session.createSQLQuery(sql).executeUpdate();
            if (d.getTransportationAmount() > 0) {
                sql = "INSERT INTO stu_billing_detail(BILL_NO,BILL_SN,REG_NO,BILL_ID,ACADEMIC_YEAR,PROGRAM,CLASS_ID,CR,DR,PAYMENT_DATE) VALUES('" + billNo + "',1," + d.getId() + ",-1," + academicYear + "," + program + "," + classId + "," + d.getTransportationAmount() + ",0,'" + effectDate + "')";
                session.createSQLQuery(sql).executeUpdate();
            }
            if (d.getHostelAmount() > 0) {
                sql = "INSERT INTO stu_billing_detail(BILL_NO,BILL_SN,REG_NO,BILL_ID,ACADEMIC_YEAR,PROGRAM,CLASS_ID,CR,DR,PAYMENT_DATE) VALUES('" + billNo + "',1," + d.getId() + ",-2," + academicYear + "," + program + "," + classId + "," + d.getHostelAmount() + ",0,'" + effectDate + "')";
                session.createSQLQuery(sql).executeUpdate();
            }
            sql = "insert into transportation_hostel_bill_generated(id, generated_month,reg_no, hostel_amount,  transportation_amount) values ('" + UUID.randomUUID() + "'," + year + month + "," + d.getId() + "," + d.getHostelAmount() + "," + d.getTransportationAmount() + ")";
            session.createSQLQuery(sql).executeUpdate();
            tr.commit();
        } catch (Exception e) {
            tr.rollback();
        }
    }

    @GetMapping
    public ResponseEntity<List<PostHostelTransportation>> index(@RequestParam(required = false) Long regNo, @RequestParam String year, @RequestParam String month, @RequestParam Long academicYear, @RequestParam Long program, @RequestParam Long classId) {
        return ResponseEntity.status(HttpStatus.OK).body(getRecord(year, month, academicYear, program, classId));
    }

    private List<PostHostelTransportation> getRecord(String year, String month, Long academicYear, Long program, Long classId) {
        List<PostHostelTransportation> data = new ArrayList<>();
//        String startDate = DateConverted.bsToAd(year + "-" + month + "-01");
//        String endDate = DateConverted.bsToAd(year + "-" + month + "-32");
//        if (endDate == null || endDate.equalsIgnoreCase("invalid"))
//            endDate = DateConverted.bsToAd(year + "-" + month + "-31");
//        if (endDate == null || endDate.equalsIgnoreCase("invalid"))
//            endDate = DateConverted.bsToAd(year + "-" + month + "-30");
//        if (endDate == null || endDate.equalsIgnoreCase("invalid"))
//            endDate = DateConverted.bsToAd(year + "-" + month + "-29");
//        if (endDate == null || endDate.equalsIgnoreCase("invalid"))
//            endDate = DateConverted.bsToAd(year + "-" + month + "-28");

        String sql = "select s.id,s.stu_name name,s.subject_group subject_group,ifnull(t.status,'N') tStatus , ifnull(h.status,'N') hStatus,ifnull(t.monthly_charge,0) transportationCharge,ifnull(h.monthly_charge,0) hostelCharge from student_info s left join student_transportation t on s.id = t.reg_no left join school_hostal h on s.id = h.reg_no left join transportation_hostel_bill_generated g on s.id = g.reg_no and g.generated_month=" + year + month + " where academic_year = " + academicYear + " and s.class_id = " + classId + " and s.program = " + program + " and (t.status ='Y' or h.status ='Y') and ifnull(g.generated_month,0)!=" + year + month + " ";
        List<Map<String, Object>> list = da.getRecord(sql);

        list.forEach(s -> {
            double hostelCharge = 0;
            double transportationCharge = 0;
            String hStatus = s.get("hStatus").toString();
            String tStatus = s.get("tStatus").toString();
            if (hStatus.equalsIgnoreCase("Y")) {
                hostelCharge = Double.parseDouble(s.get("hostelCharge").toString());

            }
            if (tStatus.equalsIgnoreCase("Y")) {
                transportationCharge = Double.parseDouble(s.get("transportationCharge").toString());
            }
            if (hostelCharge == 0 && transportationCharge == 0) return;
            data.add(PostHostelTransportation.builder()
                    .id(Long.parseLong(s.get("id").toString()))
                    .name(s.get("name").toString())
                    .hostelAmount(hostelCharge)
                    .transportationAmount(transportationCharge)
                    .group(Long.parseLong(s.get("subject_group").toString()))
                    .build());
        });

        return data;
    }
}
