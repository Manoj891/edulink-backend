package com.ms.ware.online.solution.school.entity.employee;

import java.util.List;
import java.util.Map;

public class MonthlySalaryReq {

    private String month;
    private String year;
    private List<Map<String, Object>> obj;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public List<Map<String, Object>> getObj() {
        return obj;
    }

    public void setObj(List<Map<String, Object>> obj) {
        this.obj = obj;
    }

}
