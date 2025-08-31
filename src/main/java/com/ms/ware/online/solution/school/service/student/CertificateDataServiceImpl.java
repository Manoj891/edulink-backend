package com.ms.ware.online.solution.school.service.student;

import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.student.CertificateDataDao;
import com.ms.ware.online.solution.school.exception.CustomException;
import com.ms.ware.online.solution.school.entity.student.CertificateData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class CertificateDataServiceImpl implements CertificateDataService {
    @Autowired
    private CertificateDataDao repository;
    @Autowired
    private AuthenticationFacade facade;

    @Override
    public Map<String, Object> getStudentInfo(String regNo) {
        List<Map<String, Object>> l = repository.getRecord("select s.id as id,board_regd_no boardRegdNo,academic_year, s.stu_name stuName, date_of_birth dateOfBirth, fathers_name fathersName, district as district, municipal, ward_no wardNo, ifnull(admission_year, '') admissionYear, enter_date enterDate, subject_group subjectGroup, c.degree_name, c.level, p.program_name from student_info s join class_master c on s.class_id = c.id join program_master p on p.id = s.program where s.id=" + regNo);
        if (l.isEmpty()) throw new CustomException("Student not found");
        return l.get(0);
    }

    @Override
    public Map<String, Object> getInit() {
        Map<String, Object> map = new HashMap<>();
        map.put("class", repository.getRecord("select  id,name,level,degree_name from class_master"));
        map.put("program", repository.getRecord("select id,name,program_name from program_master"));
        map.put("academicYear", repository.getRecord("select id,year  from academic_year order by id desc"));
        map.put("group", repository.getRecord("select id,name  from subject_group"));
        return map;
    }

    @Override
    public List<CertificateData> getAll(long year, String regNo) {
        if (!regNo.isEmpty()) regNo = " and regNo='" + regNo + "'";
        else regNo = "";
        return repository.getAll("from CertificateData where passedYear='" + year + "' " + regNo + " order by name ");
    }

    @Override
    public Map<String, Object> findOne(String id) {
        List<Map<String, Object>> list = repository.getRecord("select serial_number,d.id, board_name boardName, campus_reg_date campusRegDate, campus_reg_no campusRegNo, date_of_birth dateOfBirth, degree, district, fathers_name fathersName, issued_date issuedDate, join_year joinYear, municipal, d.name, passed_year passedYear, place, reg_no regNo, title, ward_no wardNo, g.name subject_group,level,program_name from certificate_data d join subject_group g on d.subject_group=g.ID and d.id='" + id + "'");
        if (list.isEmpty()) throw new CustomException("Certificate not found");
        return list.get(0);
    }


    @Override
    public String save(CertificateData obj) {
        AuthenticatedUser user = facade.getAuthentication();;
        obj.setId(UUID.randomUUID().toString());
        obj.setCreateBy(user.getUserName());
        obj.setCreateDate(new Date());
        try {

            Map<String, Object> map = repository.getRecord("SELECT ifnull(MAX(sn),0)+1 AS id FROM certificate_data where passed_year='" + obj.getPassedYear() + "' ").get(0);
            int sn = Integer.parseInt(map.get("id").toString());
            String serialNumber = "";
            if (sn < 10) serialNumber = obj.getPassedYear() + "00" + sn;
            else if (sn < 100) serialNumber = obj.getPassedYear() + "0" + sn;
            else serialNumber = obj.getPassedYear() + "" + sn;
            obj.setSerialNumber(serialNumber);
            obj.setSn(sn);
        } catch (Exception e) {
            throw new CustomException("connection error or invalid table name");
        }
        if (repository.save(obj) == 1) return "{\"message\":\"Success\"}";
        throw new CustomException(repository.getMsg());
    }

    @Override
    public String update(CertificateData obj, String id) {
        AuthenticatedUser user = facade.getAuthentication();;
        obj.setId(id);
        obj.setModifyBy(user.getUserName());
        obj.setModifyDate(new Date());
        try {

            Map<String, Object> map = repository.getRecord("SELECT ifnull(MAX(sn),0)+1 AS id FROM certificate_data where passed_year='" + obj.getPassedYear() + "' ").get(0);
            int sn = Integer.parseInt(map.get("id").toString());
            String serialNumber = "";
            if (sn < 10) serialNumber = obj.getPassedYear() + "00" + sn;
            else if (sn < 100) serialNumber = obj.getPassedYear() + "0" + sn;
            else serialNumber = obj.getPassedYear() + "" + sn;
            obj.setSerialNumber(serialNumber);
            obj.setSn(sn);
        } catch (Exception e) {
            throw new CustomException("connection error or invalid table name");
        }
        if (repository.update(obj) == 1) return "{\"message\":\"Success\"}";
        throw new CustomException(repository.getMsg());
    }

    @Override
    public String delete(String id) {
        if (repository.delete("delete from certificate_data where id = '" + id + "'") == 1)
            return "{\"message\":\"Success\"}";
        throw new CustomException(repository.getMsg());
    }


}
