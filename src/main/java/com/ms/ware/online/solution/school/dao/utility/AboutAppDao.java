
package com.ms.ware.online.solution.school.dao.utility;

import java.util.List;
import java.util.Map;

import com.ms.ware.online.solution.school.entity.student.StudentAttendance;
import com.ms.ware.online.solution.school.entity.utility.AboutApp;
import com.ms.ware.online.solution.school.entity.utility.BiometricDeviceMap;

public interface AboutAppDao {

    List<AboutApp> getAll(String hql);

    int save(AboutApp obj);

    int update(AboutApp obj);

    int delete(String sql);

    List<Map<String, Object>> getRecord(String sql);

    String getMsg();

    int save(BiometricDeviceMap obj);

    int update(BiometricDeviceMap obj);

    int update(StudentAttendance obj,long id);

    List<BiometricDeviceMap> getBiometricDeviceMap(String hql);
}
