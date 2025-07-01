/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.service.employee;

import com.ms.ware.online.solution.school.entity.employee.EmpWorkingHour;
import java.util.List;

public interface EmpWorkingHourService {

    public List<EmpWorkingHour> index(Long empId);

    public Object doSave(List<EmpWorkingHour> list);
}
