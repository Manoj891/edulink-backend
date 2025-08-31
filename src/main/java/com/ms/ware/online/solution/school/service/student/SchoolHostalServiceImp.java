/*    map = new com.fasterxml.jackson.databind.ObjectMapper().readValue(jsonData, new com.fasterxml.jackson.core.type.TypeReference<>() {});
 */
package com.ms.ware.online.solution.school.service.student;


import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.student.SchoolHostelDao;
import com.ms.ware.online.solution.school.entity.student.SchoolHostal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SchoolHostalServiceImp implements SchoolHostalService {
    @Autowired
    private AuthenticationFacade facade;
    @Autowired
    private SchoolHostelDao da;
    @Autowired
    private Message message;
    String msg = "", sql;
    int row;

    @Override
    public Object getAll(Long regNo, Long academicYear, Long program, Long classId, Long hostelType) {
        return da.getRecord("select t.id,t.status,roll_no rollNo,bill_id billId,start_date startDate,ifnull(end_date,'') endDate,reg_no regNo,hostel_type hostelType,monthly_charge monthlyCharge,stu_name stuName,mobile_no mobileNo,academic_year academicYear,fathers_name fathersName,program,class_id classId from school_hostal t join student_info s on t.reg_no = s.id  where reg_no=ifnull(" + regNo + ",reg_no) and academic_year=ifnull(" + academicYear + ",academic_year) and program=ifnull(" + program + ",program) and class_id=ifnull(" + classId + ",class_id)  and hostel_type=ifnull(" + hostelType + ",hostel_type) ");

        // return da.getAll("from SchoolHostal where regNo=ifnull("+regNo+",regNo) and studentInfo.academicYear=ifnull(" + academicYear + ",studentInfo.academicYear) and studentInfo.program=ifnull(" + program + ",studentInfo.program) and studentInfo.classId=ifnull(" + classId + ",studentInfo.classId)  and hostelType=ifnull(" + hostelType + ",hostelType) ");
    }

    @Override
    public Object save(SchoolHostal obj) {
        AuthenticatedUser td = facade.getAuthentication();
        ;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        try {

            sql = "SELECT ifnull(MAX(ID),0)+1 AS id FROM school_hostal";
            message.map = (Map) da.getRecord(sql).get(0);
            obj.setId(Long.parseLong(message.map.get("id").toString()));
            obj.setBillId(-2l);
            row = da.save(obj);
            msg = da.getMsg();
            msg = da.getMsg().replace("`", "").toLowerCase();
            if (row > 0) {
                return message.respondWithMessage("Success");
            } else if (msg.contains("Duplicate entry")) {
                msg = "This record already exist";
            }
            if (msg.contains("foreign key (bill_id) references bill_master (id))")) {
                sql = "INSERT INTO bill_master (ID,NAME,STATUS,AC_CODE,IS_INVENTORY,INVENTORY_AC_CODE) VALUES (-2,'Hostal Charge', 'N', '30101', 'N', NULL);";
                da.delete(sql);
                return message.respondWithError("Try Again");
            }
            return message.respondWithError(msg);

        } catch (Exception e) {
            return message.respondWithError(e.getMessage());
        }
    }

    @Override
    public Object update(SchoolHostal obj, long id) {
        AuthenticatedUser td = facade.getAuthentication();
        ;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        obj.setId(id);
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
        ;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }

        id = "'" + id.replace(",", "','") + "'";
        sql = "DELETE FROM school_hostal WHERE ID IN (" + id + ")";
        row = da.delete(sql);
        msg = da.getMsg();
        if (row > 0) {
            return message.respondWithMessage("Success");
        } else if (msg.contains("foreign key")) {
            msg = "this record already used in reference tables, Cannot delete of this record";
        }
        return message.respondWithError(msg);
    }
}
