package com.ms.ware.online.solution.school.dao.utility;

import com.ms.ware.online.solution.school.entity.exam.CharacterIssue;
import com.ms.ware.online.solution.school.entity.utility.SmsCreditAmount;

import java.util.List;
import java.util.Map;

import com.ms.ware.online.solution.school.entity.utility.OrganizationMaster;
import com.ms.ware.online.solution.school.entity.utility.SentSms;


public interface OrganizationMasterDao {

     List getAll(String hql);

     int save(OrganizationMaster obj);

     int save(SentSms obj);

     int save(SmsCreditAmount obj);

     int save(CharacterIssue obj);

     int update(OrganizationMaster obj);

     int delete(String sql);

     List<Map<String, Object>> getRecord(String sql);

     String getMsg();
}
