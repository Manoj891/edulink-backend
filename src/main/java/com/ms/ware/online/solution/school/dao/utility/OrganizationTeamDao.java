
package com.ms.ware.online.solution.school.dao.utility;

import com.ms.ware.online.solution.school.entity.utility.OrganizationTeam;

import java.util.List;
import java.util.Map;

public interface OrganizationTeamDao {

     List<OrganizationTeam> getAll(String hql);

     int save(OrganizationTeam obj);

     int update(OrganizationTeam obj);

     int delete(String sql);

    List<Map<String, Object>> getRecord(String sql);

     String getMsg();
}
