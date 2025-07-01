package com.ms.ware.online.solution.school.dao.utility;

import com.ms.ware.online.solution.school.entity.utility.MenuUserAccess;
import com.ms.ware.online.solution.school.entity.utility.OrganizationUserInfo;

import java.util.List;
import java.util.Map;
public interface OrganizationUserInfoDao {

     List<OrganizationUserInfo> getAll(String hql);

     int save(OrganizationUserInfo obj);

     int save(MenuUserAccess obj);

     int update(OrganizationUserInfo obj);

     int delete(String sql);

    List<Map<String, Object>> getRecord(String sql);

     String getMsg();
}
