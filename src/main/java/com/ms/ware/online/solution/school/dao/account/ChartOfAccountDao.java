
package com.ms.ware.online.solution.school.dao.account;

import java.util.List;
import com.ms.ware.online.solution.school.entity.account.ChartOfAccount;

public interface ChartOfAccountDao {

     List<ChartOfAccount> getAll(String hql);

     int save(ChartOfAccount obj);

     int update(ChartOfAccount obj);

     int delete(String sql);

     List getRecord(String sql);

     String getMsg();
}
