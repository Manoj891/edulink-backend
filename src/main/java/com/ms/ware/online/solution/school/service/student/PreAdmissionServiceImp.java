/*    map = new com.fasterxml.jackson.databind.ObjectMapper().readValue(jsonData, new com.fasterxml.jackson.core.type.TypeReference<>() {});
 */
package com.ms.ware.online.solution.school.service.student;


import com.ms.ware.online.solution.school.config.DateConverted;
import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.student.PreAdmissionDao;
import com.ms.ware.online.solution.school.entity.student.PreAdmission;
import com.ms.ware.online.solution.school.service.account.VoucherEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PreAdmissionServiceImp implements PreAdmissionService {
    @Autowired
    private AuthenticationFacade facade;
    @Autowired
    private PreAdmissionDao da;
    @Autowired
    private VoucherEntry ve;
    @Autowired
    private Message message;
    String msg = "", sql;
    int row;

    @Override
    public Object getAll(Long academicYear, Long program, Long classId) {
        return da.getAll("from PreAdmission where academicYear=" + academicYear + " and program=ifnull(" + program + ",program) and classId=ifnull(" + classId + ",classId) and status='N'");
    }

    @Override
    public Object save(PreAdmission obj) {
        AuthenticatedUser td = facade.getAuthentication();
        ;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        String userName = td.getUserName();
        String cashAccount = td.getCashAccount();
        if (cashAccount.length() == 0) {
            return message.respondWithError("Please define cash Account of " + userName);
        }
        String date = DateConverted.toString(obj.getEnterDateAd());
        try {
            sql = "SELECT ifnull(MAX(ID),0)+1 AS id FROM pre_admission";
            message.map = (Map) da.getRecord(sql).get(0);
            obj.setId(Long.parseLong(message.map.get("id").toString()));
            sql = "SELECT ID id FROM fiscal_year WHERE '" + date + "' BETWEEN START_DATE AND END_DATE;";
            message.map = (Map) da.getRecord(sql).get(0);
            long fiscalYear = Long.parseLong(message.map.get("id").toString());
            sql = "SELECT IFNULL(max(BILL_SN),0)+1 AS billSn FROM stu_billing_master WHERE FISCAL_YEAR='" + fiscalYear + "' AND BILL_TYPE='DR'";
            message.map = (Map) da.getRecord(sql).get(0);
            long billSn = Long.parseLong(message.map.get("billSn").toString());

            String billNo = "";
            if (billSn < 10) {
                billNo = fiscalYear + "000" + billSn;
            } else if (billSn < 100) {
                billNo = fiscalYear + "00" + billSn;
            } else if (billSn < 1000) {
                billNo = fiscalYear + "0" + billSn;
            } else {
                billNo = fiscalYear + "" + billSn;
            }
            obj.setEnterBy(td.getUserName());
            obj.setBillNo(billNo);
            row = da.save(obj);
            msg = da.getMsg();

            if (row == 1 && obj.getBillAmount() > 0) {
                sql = "INSERT INTO stu_billing_master (ACADEMIC_YEAR,CLASS_ID,PROGRAM,SUBJECT_GROPU,BILL_NO, BILL_SN,ADDRESS, APPROVE_BY, APPROVE_DATE, BILL_TYPE, ENTER_BY, ENTER_DATE, FATHER_NAME, FISCAL_YEAR, MOBILE_NO, REG_NO, STUDENT_NAME,BILL_AMOUNT) VALUES ('" + obj.getAcademicYear() + "','" + obj.getClassId() + "','" + obj.getProgram() + "','1','" + billNo + "'," + billSn + ", '" + obj.getDistrict() + " " + obj.getMunicipal() + " " + obj.getWardNo() + "', '" + td.getUserName() + "', '" + date + "',  'DR', '" + td.getUserName() + "', '" + date + "', '" + obj.getFatherName() + "', " + fiscalYear + ", '" + obj.getMobileNo() + "', NULL, '" + obj.getStuName() + "','" + obj.getBillAmount() + "');";
                row = da.delete(sql);
                if (row == 1) {
                    sql = "INSERT INTO stu_billing_detail (BILL_NO,BILL_SN,BILL_ID, ACADEMIC_YEAR, CLASS_ID, CR, DR, PROGRAM, REG_NO) VALUES ('" + billNo + "',1,'" + obj.getBillId() + "', " + obj.getAcademicYear() + ", " + obj.getClassId() + ", 0.0, " + obj.getBillAmount() + ", " + obj.getProgram() + ", NULL)";
                    row = da.delete(sql);
                    if (row == 1) {
                        if (!postInVoucher(obj.getId(), obj.getBillId(), obj.getBillNo(), obj.getBillAmount(), cashAccount, obj.getStuName(), fiscalYear, date, td.getUserName())) {
                            return message.respondWithError(msg);
                        }
                    } else {
                        sql = "DELETE FROM pre_admission WHERE ID='" + obj.getId() + "';\nDELETE FROM stu_billing_master WHERE BILL_NO='" + billNo + "';";
                        da.delete(sql);
                        return message.respondWithError("Account Permission Error for " + userName);
                    }
                } else {
                    sql = "DELETE FROM pre_admission WHERE ID='" + obj.getId() + "';";
                    da.delete(sql);
                    return message.respondWithError("Account Permission Error for " + userName);
                }
            }

            if (row > 0) {
                return message.respondWithMessage("Success", billNo);
            } else if (msg.contains("Duplicate entry")) {
                msg = "This record already exist";
            }
            return message.respondWithError(msg);

        } catch (Exception e) {
            return message.respondWithError(e.getMessage());
        }
    }

    @Override
    public Object update(PreAdmission obj, long id) {
        AuthenticatedUser td = facade.getAuthentication();
        ;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        obj.setId(id);
        row = da.update(obj);
        msg = da.getMsg();
        if (row > 0) {
            obj.getBillNo();
            sql = "UPDATE stu_billing_detail SET DR='" + obj.getBillAmount() + "',BILL_ID='" + obj.getBillId() + "',ACADEMIC_YEAR='" + obj.getAcademicYear() + "',CLASS_ID='" + obj.getClassId() + "',PROGRAM='" + obj.getProgram() + "' WHERE BILL_NO='" + obj.getBillNo() + "'";
            da.delete(sql);

            sql = "SELECT * FROM  voucher WHERE `FEE_RECEIPT_NO`='" + obj.getBillNo() + "'";
            List list = da.getRecord(sql);
            if (list.isEmpty()) {
                String date = DateConverted.toString(obj.getEnterDateAd());
                sql = "SELECT ID id FROM fiscal_year WHERE '" + date + "' BETWEEN START_DATE AND END_DATE;";
                message.map = (Map) da.getRecord(sql).get(0);
                long fiscalYear = Long.parseLong(message.map.get("id").toString());
                String cashAccount = td.getCashAccount();
                if (cashAccount.length() == 0) {
                    return message.respondWithError("Please define cash Account of " + td.getUserName());
                }
                if (!postInVoucher(obj.getId(), obj.getBillId(), obj.getBillNo(), obj.getBillAmount(), cashAccount, obj.getStuName(), fiscalYear, date, td.getUserName())) {
                    return message.respondWithError(msg);
                }
            }
            return message.respondWithMessage("Success");
        } else if (msg.contains("Duplicate entry")) {
            msg = "This record already exist";
        } else if (msg.contains("foreign key")) {
            msg = "this record already used in reference tables, Cannot delete of this record";
        }
        return message.respondWithError(msg);
    }

    @Override
    public Object delete(String id) {
        AuthenticatedUser td = facade.getAuthentication();
        ;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }

        id = "'" + id.replace(",", "','") + "'";
        sql = "DELETE FROM pre_admission WHERE ID IN (" + id + ")";
        row = da.delete(sql);
        msg = da.getMsg();
        if (row > 0) {
            return message.respondWithMessage("Success");
        } else if (msg.contains("foreign key")) {
            msg = "this record already used in reference tables, Cannot delete of this record";
        }
        return message.respondWithError(msg);
    }

    @Override
    public Object getRecord(long id) {
        List list = da.getAll("from PreAdmission where id=" + id + " ");
        if (list.isEmpty()) {
            return message.respondWithError("Record not found!!");
        }
        return list.get(0);
    }

    private boolean postInVoucher(Long id, Long billId, String billNo, double billAmount, String cashAccount, String studentName, Long fiscalYear, String date, String enterBy) {

        sql = "SELECT AC_CODE acCode,NAME billName FROM bill_master WHERE ID='" + billId + "'";
        message.map = (Map) da.getRecord(sql).get(0);
        String acCode[] = {message.map.get("acCode").toString(), cashAccount};
        String particular[] = {"Being " + message.map.get("billName").toString() + " Paid by  " + studentName, "Being cash Receive from " + studentName};
        double drAmount[] = {0, billAmount};
        double crAmount[] = {billAmount, 0};

        String narration = "Being cash Receive from " + studentName + " .";

        if (!ve.save(fiscalYear, date, enterBy, "BRV", narration, "", billNo, acCode, particular, drAmount, crAmount)) {
            sql = "DELETE FROM stu_billing_detail WHERE BILL_NO='" + billNo + "';";
            da.delete(sql);
            sql = "DELETE FROM stu_billing_master WHERE BILL_NO='" + billNo + "';";
            da.delete(sql);
            sql = "DELETE FROM pre_admission WHERE ID='" + id + "';";
            da.delete(sql);
            msg = "Account Permission Error for " + enterBy;
            return false;
        }
        return true;
    }

}
