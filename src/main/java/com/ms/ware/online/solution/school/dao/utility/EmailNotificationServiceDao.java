
package com.ms.ware.online.solution.school.dao.utility;

import com.ms.ware.online.solution.school.entity.utility.EmailNotificationService;

import java.util.List;
import java.util.Map;
 public interface EmailNotificationServiceDao {

     List<EmailNotificationService> getAll(String hql);

     int save(EmailNotificationService obj);

     int update(EmailNotificationService obj);

     int delete(String sql);

    List<Map<String, Object>> getRecord(String sql);

     String getMsg();
}
