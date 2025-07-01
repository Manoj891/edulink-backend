package com.ms.ware.online.solution.school.dao.teacherpanel;

import com.ms.ware.online.solution.school.entity.teacherpanel.TeachersHomework;
import com.ms.ware.online.solution.school.entity.teacherpanel.UploadTeachersVideo;

import java.util.List;
import java.util.Map;

public interface TeachersHomeworkDao {

     List<TeachersHomework> getAll(String hql);

     int save(TeachersHomework obj);

     int save(UploadTeachersVideo obj);

     int update(UploadTeachersVideo obj);

     int update(TeachersHomework obj);

     int delete(String sql);

    List<Map<String, Object>> getRecord(String sql);

     String getMsg();
}
