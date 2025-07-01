package com.ms.ware.online.solution.school.service.utility;

import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.utility.AboutAppDao;
import com.ms.ware.online.solution.school.exception.CustomException;
import com.ms.ware.online.solution.school.entity.utility.BiometricDeviceMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class BiometricDeviceMapService {
    @Autowired
    private AboutAppDao dao;
    @Autowired
    private AuthenticationFacade facade;

    public List<BiometricDeviceMap> findAll(String type) {
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            throw new CustomException("Invalid Access");
        }
        if (type != null && type.length() > 2) {
            return dao.getBiometricDeviceMap("from BiometricDeviceMap where userType='" + type + "'");
        }
        return dao.getBiometricDeviceMap("from BiometricDeviceMap  where userType!='Staff'");
    }

    public void save(BiometricDeviceMap obj) {
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            throw new CustomException("Invalid Access");
        }
        if (obj.getUserType().equalsIgnoreCase("Student")) {
            if (obj.getStudentId() == null) throw new CustomException("Please provide Student ID");
            obj.setEmpId(null);
        } else if (obj.getUserType().equalsIgnoreCase("Staff")) {
            if (obj.getEmpId() == null) throw new CustomException("Please provide Staff ID");
            obj.setStudentId(null);
        }
        obj.setId(UUID.randomUUID().toString());
        if (dao.save(obj) == 0) throw new CustomException(dao.getMsg());
    }

    public void update(BiometricDeviceMap obj) {
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            throw new CustomException("Invalid Access");
        }
        if (obj.getUserType().equalsIgnoreCase("Student")) {
            if (obj.getStudentId() == null) throw new CustomException("Please provide Student ID");
            obj.setEmpId(null);
        } else if (obj.getUserType().equalsIgnoreCase("Staff")) {
            if (obj.getEmpId() == null) throw new CustomException("Please provide Staff ID");
            obj.setStudentId(null);
        }
        if (dao.update(obj) == 0) throw new CustomException(dao.getMsg());
    }

    public void delete(String id) {
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            throw new CustomException("Invalid Access");
        }
        if (dao.delete("delete from biometric_device_map where id='" + id + "'") == 0)
            throw new CustomException(dao.getMsg());
    }

//
//
//    public void fatchData(String userType) {
//
//
//        List<Map<String, Object>> list = dao.getRecord("select a.id,att_date attDate,in_time inTime,ifnull(out_time,'') outTime,b.student_id studentId,b.emp_id empId,user_type userType from iclock.date_wise_attendance a join biometric_device_map b on a.emp_id = b.device_id and b.user_type='" + userType + "' and branch = '1' and updated is false");
//        List<String> sql = new ArrayList<>();
//        list.forEach(map -> {
//            String inTime = map.get("inTime").toString().substring(11, 19);
//            String outTime = map.get("outTime").toString();
//            if (outTime.length() < 18) outTime = null;
//            else outTime = outTime.substring(11, 19);
//            String date = map.get("attDate").toString();
//            long id = Long.parseLong(map.get("id").toString());
//            try {
//                StudentAttendancePK pk = StudentAttendancePK.builder().attDate(DateConverted.toDate(date)).stuId(Long.parseLong(map.get("studentId").toString())).build();
//                dao.update(StudentAttendance.builder().enterDate(DateConverted.now()).enterBy("Device").inTime(inTime).outTime(outTime).status("Y").pk(pk).build(), id);
//                sql.add("update date_wise_attendance set updated=true where id=" + id + ";");
//            } catch (Exception ex) {
//                log.info(ex.getMessage());
//            }
//        });
//        sql.forEach(map -> dao.delete(map));
//    }
}
