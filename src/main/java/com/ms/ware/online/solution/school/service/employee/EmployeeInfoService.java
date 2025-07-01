package com.ms.ware.online.solution.school.service.employee;

import com.ms.ware.online.solution.school.entity.employee.EmployeeInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface EmployeeInfoService {

    List<EmployeeInfo> getAll();

    void save(EmployeeInfo obj);

    void update(EmployeeInfo obj, long id);

    void delete(String id);

    void doDeviceIdMap(long id, Long biometricCompanyId, Long biometricEmpId);


    void doSavePhoto(Long empId, MultipartFile photo);

    List<Map<String, Object>> findTeacher();

}
