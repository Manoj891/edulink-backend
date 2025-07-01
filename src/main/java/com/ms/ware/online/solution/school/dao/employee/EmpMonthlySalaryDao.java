/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.dao.employee;

import com.ms.ware.online.solution.school.entity.employee.EmpMonthlySalary;

import java.util.List;
import java.util.Map;

/**
 * @author Manoj
 */
public interface EmpMonthlySalaryDao {
    List<EmpMonthlySalary> getAll(String hql);

    int save(EmpMonthlySalary obj);

    int save(List<EmpMonthlySalary> list);

    int delete(String sql);

    List<Map<String, Object>> getRecord(String sql);

    String getMsg();
}
