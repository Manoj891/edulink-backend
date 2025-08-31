
package com.ms.ware.online.solution.school.config;

import com.ms.ware.online.solution.school.exception.CustomException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import com.ms.ware.online.solution.school.model.HibernateUtilImpl;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Getter
@Service
public class CreditBillAutoGenerate {

    private String msg = "";

    public boolean generate(long academicYear, long program, long classId, long subjectGroup, long regNo, String enterBy) {
        String sql;
        Session session = util.getSession();
        Transaction tr = session.beginTransaction();
        try {
            Map<String, Object> map;
            String masterId, feeId, startDate;
            int time, row;
            float amount;
            sql = "SELECT ID masterId,START_DATE startDate,END_DATE endDate,TOTAL_MONTH totalMonth FROM school_class_session WHERE ACADEMIC_YEAR='" + academicYear + "' AND PROGRAM='" + program + "' AND CLASS_ID='" + classId + "'";
            List<Map<String, Object>> list = session.createSQLQuery(sql).setResultTransformer(org.hibernate.Criteria.ALIAS_TO_ENTITY_MAP).list();
            if (list.isEmpty()) {
                msg = "Please define school class session";
                return false;
            }
            map = list.get(0);
            masterId = map.get("masterId").toString();
            startDate = DateConverted.toString(DateConverted.addDate(map.get("startDate").toString(), 2));
            sql = "SELECT PAYMENT_DATE paydate FROM school_class_session_bill_date WHERE MASTER_ID=" + masterId + " ORDER BY PAYMENT_TIME";
            list = session.createSQLQuery(sql).setResultTransformer(org.hibernate.Criteria.ALIAS_TO_ENTITY_MAP).list();
            String[] schoolClassSessionBillDate = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                map = list.get(i);
                schoolClassSessionBillDate[i] = map.get("paydate").toString();
            }

            String billNo;
            sql = "SELECT ID id FROM fiscal_year WHERE START_DATE<='" + startDate + "' AND END_DATE>='" + startDate + "'";
            System.out.println(sql);
            list = session.createSQLQuery(sql).setResultTransformer(org.hibernate.Criteria.ALIAS_TO_ENTITY_MAP).list();
            if (list.isEmpty())
                throw new CustomException("Fiscal Year not found on date at " + startDate + ", Education Session start from " + startDate);
            map = list.get(0);
            long fiscalYear = Long.parseLong(map.get("id").toString());
            sql = "SELECT BILL_NO billNo FROM stu_billing_master WHERE REG_NO='" + regNo + "' AND ACADEMIC_YEAR='" + academicYear + "' and CLASS_ID=" + classId + " AND AUTO_GENERATE='Y' AND BILL_TYPE='CR'";
            list = session.createSQLQuery(sql).setResultTransformer(org.hibernate.Criteria.ALIAS_TO_ENTITY_MAP).list();
            if (!list.isEmpty()) {
                map = list.get(0);
                billNo = map.get("billNo").toString();
                sql = "DELETE FROM stu_billing_detail WHERE BILL_NO ='" + billNo + "'";
                session.createSQLQuery(sql).executeUpdate();
                sql = "DELETE FROM stu_billing_master WHERE BILL_NO ='" + billNo + "'";
                session.createSQLQuery(sql).executeUpdate();
            }
            sql = "SELECT IFNULL(max(BILL_SN),0)+1 AS billSn FROM stu_billing_master WHERE FISCAL_YEAR='" + fiscalYear + "' AND BILL_TYPE='CR'";
            list = session.createSQLQuery(sql).setResultTransformer(org.hibernate.Criteria.ALIAS_TO_ENTITY_MAP).list();
            map = list.get(0);
            long billSn = Long.parseLong(map.get("billSn").toString());

            if (billSn < 10) {
                billNo = "CR" + academicYear + "-" + classId + "-000" + billSn;
            } else if (billSn < 100) {
                billNo = "CR" + academicYear + "-" + classId + "-00" + billSn;
            } else if (billSn < 1000) {
                billNo = "CR" + academicYear + "-" + classId + "-0" + billSn;
            } else {
                billNo = "CR" + academicYear + "-" + classId + "-" + billSn;
            }
            sql = "INSERT INTO stu_billing_master(BILL_NO,BILL_SN,BILL_TYPE,REG_NO,ACADEMIC_YEAR,PROGRAM,CLASS_ID,SUBJECT_GROPU,FISCAL_YEAR,ENTER_BY,ENTER_DATE,APPROVE_BY,APPROVE_DATE,AUTO_GENERATE) VALUES('" + billNo + "'," + billSn + ",'CR', '" + regNo + "', '" + academicYear + "'," + program + ",'" + classId + "','" + subjectGroup + "','" + fiscalYear + "','" + enterBy + "','" + startDate + "','" + enterBy + "','" + startDate + "','Y');";
            row = session.createSQLQuery(sql).executeUpdate();
            if (row == 0) {
                msg = "stu_billing_master insert error";
                tr.rollback();
                session.close();
                return false;
            }

            sql = "SELECT AMOUNT amount,PAY_TIME 'time',FEE_ID feeId,ifnull(fee_month,'01') feeMonth FROM fee_setup WHERE ACADEMIC_YEAR='" + academicYear + "' AND PROGRAM='" + program + "' AND CLASS_ID='" + classId + "' AND SUBJECT_GROUP='" + subjectGroup + "'";
            list = session.createSQLQuery(sql).setResultTransformer(org.hibernate.Criteria.ALIAS_TO_ENTITY_MAP).list();
            int record = 1;
            List<String> sqlList = new ArrayList<>();
            for (Map<String, Object> stringObjectMap : list) {
                amount = Float.parseFloat(stringObjectMap.get("amount").toString());
                time = Integer.parseInt(stringObjectMap.get("time").toString());
                feeId = stringObjectMap.get("feeId").toString();
                String feeMonth = stringObjectMap.get("feeMonth").toString();
                String paymentDate = feeMonth.equals("01") ? startDate : (DateConverted.bsToAd("20" + academicYear + "-" + feeMonth + "-01"));
                if (time == 1) {
                    sqlList.add("INSERT INTO stu_billing_detail(BILL_NO,BILL_SN,REG_NO,BILL_ID,ACADEMIC_YEAR,PROGRAM,CLASS_ID,CR,DR,PAYMENT_DATE) VALUES('" + billNo + "'," + record + ",'" + regNo + "','" + feeId + "','" + academicYear + "','" + program + "','" + classId + "','" + amount + "','0','" + paymentDate + "');\n");
                    record++;
                } else {
                    for (int j = 0; j < schoolClassSessionBillDate.length; j++) {
                        sqlList.add("INSERT INTO stu_billing_detail(BILL_NO,BILL_SN,REG_NO,BILL_ID,ACADEMIC_YEAR,PROGRAM,CLASS_ID,CR,DR,PAYMENT_DATE) VALUES('" + billNo + "'," + record + "00" + j + ",'" + regNo + "','" + feeId + "','" + academicYear + "','" + program + "','" + classId + "','" + amount + "','0','" + schoolClassSessionBillDate[j] + "');\n");
                        record++;
                    }
                }
            }

            sqlList.forEach(s -> session.createSQLQuery(s).executeUpdate());

            if (record > 0) {
                tr.commit();
                session.close();
                return true;
            } else {
                tr.rollback();
                session.close();
                return false;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            try {
                tr.rollback();
                session.close();
            } catch (Exception ignored) {
            }
            return false;
        }
    }
/*
 insert into class_transfer(ACADEMIC_YEAR, STUDENT_ID, CLASS_ID, PROGRAM, ROLL_NO, SECTION, SUBJECT_GROUP, created,
                           updated)
select ACADEMIC_YEAR,
       STUDENT_ID,
       7 CLASS_ID,
       PROGRAM,
       ROLL_NO,
       SECTION,
       SUBJECT_GROUP,
       created,
       updated
from class_transfer
where ACADEMIC_YEAR = 81
  and PROGRAM = 4
  and CLASS_ID = 8
  and SUBJECT_GROUP = 2;*/
}
