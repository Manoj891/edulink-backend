
package com.ms.ware.online.solution.school.dao.setup;

import java.util.List;

import com.ms.ware.online.solution.school.entity.setup.BillMaster;
import com.ms.ware.online.solution.school.entity.utility.SmsConfiguration;

public interface BillMasterDao {

    List<Object> getAll(String hql);

    int save(BillMaster obj);

    int save(SmsConfiguration obj);

    int update(BillMaster obj);

    int delete(String sql);

    List getRecord(String sql);

    String getMsg();
}
