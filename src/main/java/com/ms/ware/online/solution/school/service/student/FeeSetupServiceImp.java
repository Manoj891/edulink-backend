/*    map = new com.fasterxml.jackson.databind.ObjectMapper().readValue(jsonData, new com.fasterxml.jackson.core.type.TypeReference<>() {});
 */
package com.ms.ware.online.solution.school.service.student;


import com.ms.ware.online.solution.school.config.CreditBillAutoGenerate;
import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.student.FeeSetupDao;
import com.ms.ware.online.solution.school.dto.OldStudent;
import com.ms.ware.online.solution.school.entity.student.FeeSetup;
import com.ms.ware.online.solution.school.entity.student.FeeSetupPK;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class FeeSetupServiceImp implements FeeSetupService {
    @Autowired
    private AuthenticationFacade facade;
    @Autowired
    FeeSetupDao da;
    @Autowired
    private CreditBillAutoGenerate crBill;
    Message message = new Message();
    String msg = "", sql;
    int row;

    @Override
    public Object getAll(Long program, Long classId, Long academicYear, Long subjectGroup) {
        return da.getRecord("SELECT CONCAT(PROGRAM,'-',CLASS_ID,'-',ACADEMIC_YEAR,'-',SUBJECT_GROUP,'-',FEE_ID) AS id,F.PROGRAM program,F.CLASS_ID classId,F.ACADEMIC_YEAR academicYear,SUBJECT_GROUP subjectGroup,F.FEE_ID feeId,AMOUNT amount,F.PAY_TIME payTime,F.TOTAL_AMOUNT totalAmount,B.NAME feeName FROM fee_setup F,bill_master B WHERE F.FEE_ID=B.ID AND PROGRAM=IFNULL(" + program + ",PROGRAM) AND CLASS_ID=IFNULL(" + classId + ",CLASS_ID) AND SUBJECT_GROUP=IFNULL(" + subjectGroup + ",SUBJECT_GROUP) AND ACADEMIC_YEAR=IFNULL(" + academicYear + ",ACADEMIC_YEAR) ");
    }

    @Override
    public Object save(FeeSetup obj) {
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        try {
            FeeSetupPK pk = new FeeSetupPK();
            pk.setAcademicYear(obj.getAcademicYear().getId());
            pk.setClassId(obj.getClassId().getId());
            pk.setProgram(obj.getProgram().getId());
            pk.setFeeId(obj.getFeeId().getId());
            pk.setSubjectGroup(obj.getSubjectGroup().getId());
            obj.setPk(pk);
            obj.setTotalAmount(obj.getAmount() * obj.getPayTime());
            row = da.save(obj);
            msg = da.getMsg();
            if (row > 0) {
                return message.respondWithMessage("Success");
            } else if (msg.contains("Duplicate entry")) {
                msg = "This record already exist";
            }
            return message.respondWithError(msg);

        } catch (Exception e) {
            return message.respondWithError(e.getMessage());
        }
    }

    @Override
    public Object update(FeeSetup obj, String id) {
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        FeeSetupPK pk = new FeeSetupPK();
        pk.setAcademicYear(obj.getAcademicYear().getId());
        pk.setClassId(obj.getClassId().getId());
        pk.setProgram(obj.getProgram().getId());
        pk.setFeeId(obj.getFeeId().getId());
        pk.setSubjectGroup(obj.getSubjectGroup().getId());
        obj.setPk(pk);
        obj.setTotalAmount(obj.getAmount() * obj.getPayTime());
        row = da.update(obj);
        msg = da.getMsg();
        if (row > 0) {
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
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        sql = "DELETE FROM fee_setup WHERE CONCAT(PROGRAM,'-',CLASS_ID,'-',ACADEMIC_YEAR,'-',SUBJECT_GROUP,'-',FEE_ID)= '" + id + "'";
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
    public Object copy(Long program, Long classId, Long academicYear, Long programTo, Long classIdTo, Long academicYearTo, Long subjectGroup, Long subjectGroupTo) {
        try {
            FeeSetupPK pk = new FeeSetupPK();
            FeeSetup obj = new FeeSetup();
            pk.setAcademicYear(academicYearTo);
            pk.setClassId(classIdTo);
            pk.setProgram(programTo);
            pk.setSubjectGroup(subjectGroupTo);
            sql = "SELECT FEE_ID feeId,AMOUNT amount,PAY_TIME payTime FROM fee_setup WHERE PROGRAM=" + program + " AND CLASS_ID=" + classId + " AND ACADEMIC_YEAR=" + academicYear + " AND SUBJECT_GROUP=" + subjectGroup;
            List l = da.getRecord(sql);
            Map map;
            row = 0;
            for (int i = 0; i < l.size(); i++) {
                map = (Map) l.get(i);
                try {
                    pk.setFeeId(Long.parseLong(map.get("feeId").toString()));
                    obj.setPk(pk);
                    obj.setAmount(Float.parseFloat(map.get("amount").toString()));
                    obj.setPayTime(Integer.parseInt(map.get("payTime").toString()));
                    obj.setTotalAmount(obj.getAmount() * obj.getPayTime());
                    row = row + da.saveOrUpdate(obj);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        } catch (Exception ignored) {
        }
        return message.respondWithMessage(row + " Copy");
    }

    @Override
    public Object save(OldStudent obj) {
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }

        long academicYear = obj.getAcademicYear();
        long program = obj.getProgram();
        long classId = obj.getClassId();
        long subjectGroup = obj.getSubjectGroup();


        try {
            for (long studentId : obj.getObj()) {
                if (!crBill.generate(academicYear, program, classId, subjectGroup, studentId, td.getUserName())) {
                    return message.respondWithError(crBill.getMsg());
                }
            }


        } catch (Exception ex) {
            Logger.getLogger(FeeSetupServiceImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return message.respondWithMessage("Success");
    }
}
