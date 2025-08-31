/*
DELETE FROM class_transfer;
DELETE FROM student_transportation;
DELETE FROM stu_billing_detail;
DELETE FROM stu_billing_master;
DELETE FROM student_info;
 */
package com.ms.ware.online.solution.school.excel;

import com.ms.ware.online.solution.school.dao.employee.EmployeeInfoDao;
import com.ms.ware.online.solution.school.dao.employee.EmployeeInfoDaoImp;
import com.ms.ware.online.solution.school.entity.employee.EmployeeInfo;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;

public class ReadDBempData {



    public void doImport(String fileName) {

        EmployeeInfoDao da = new EmployeeInfoDaoImp();
        String gender, sql;
        try {
            File file = new File(fileName);

            FileInputStream inputStream = new FileInputStream(file);
            Workbook excel = new XSSFWorkbook(inputStream);
            sql = "insert into department_master(ID, NAME) values ('1','Academic');";
            da.delete(sql);
            sql = "insert into department_master(ID, NAME) values ('2','Ancillary');";
            da.delete(sql);
            sql = "insert into emp_level_master(ID, NAME) VALUES ('1','General');";
            da.delete(sql);
            EmployeeInfo obj = new EmployeeInfo();

            long id = 86;
            String empType;

//            sql = "DELETE FROM teachers_information";
//            da.delete(sql);
//            sql = "DELETE FROM employee_info";
//            da.delete(sql);

            sql = "DELETE FROM temp_not_import";
            da.delete(sql);


            Sheet excelSheet = excel.getSheet("withoutEmail");

            for (Row row : excelSheet) {
//                row.cellIterator().forEachRemaining(cell -> {
//                    System.out.print(cell.toString());
//                });
//                System.out.println();
//System.err.println(row.getCell(3).getStringCellValue());
                try {
                    try {
                        obj.setDepartment((long) row.getCell(0).getNumericCellValue());
                    } catch (Exception e) {
                        obj.setDepartment(Long.parseLong(row.getCell(0).getStringCellValue()));
                    }
                    try {
                        obj.setEmpLevel((long) row.getCell(1).getNumericCellValue());
                    } catch (Exception e) {
                        obj.setEmpLevel(Long.parseLong(row.getCell(1).getStringCellValue()));
                    }
                    try {

                        obj.setId((long) row.getCell(2).getNumericCellValue());
                        obj.setCode(String.valueOf(obj.getId()));
                    } catch (Exception e) {
                        obj.setCode(row.getCell(2).getStringCellValue());
                        obj.setId(Long.parseLong(obj.getCode()));
                    }
                    obj.setFirstName(row.getCell(3).getStringCellValue());
                    obj.setMiddleName(row.getCell(4).getStringCellValue());
                    obj.setLastName(row.getCell(5).getStringCellValue());
                    try {
                        obj.setEmail(row.getCell(6).getStringCellValue());
                    } catch (Exception e) {
                    }
                    try {
                        obj.setMobile(String.valueOf((long) row.getCell(7).getNumericCellValue()));
                    } catch (Exception e) {
                        obj.setMobile(row.getCell(1).getStringCellValue());
                    }
                    obj.setMaritalStatus(row.getCell(8).getStringCellValue());
                    obj.setDistrict(row.getCell(9).getStringCellValue());
                    obj.setMunicipal(row.getCell(10).getStringCellValue());
                    empType = row.getCell(12).getStringCellValue();
                    obj.setEmpType(empType);
//                    if ("Teacher".equals(empType)) {
//                        obj.setEmpType("Teaching");
//                    } else {
//                        obj.setEmpType("Non Teaching");
//                    }
                    obj.setGender(row.getCell(13).getStringCellValue());
                    obj.setHolyday1(row.getCell(14).getStringCellValue());
                    obj.setHouseNo(row.getCell(15).getStringCellValue());

//                    System.out.println(obj);
                    if (da.save(obj) != 1) {
                        sql = "INSERT INTO temp_not_import (ID,MSG) VALUES (" + id + ", '" + da.getMsg().replace("'", " ") + "')";
                        da.delete(sql);
                    }
                } catch (Exception e) {
                    continue;
                }


            }

        } catch (Exception e) {
        }

    }

    SimpleDateFormat d1 = new SimpleDateFormat("M/d/yyyy");
    SimpleDateFormat d2 = new SimpleDateFormat("yyyy/M/d");
    SimpleDateFormat d3 = new SimpleDateFormat("d/M/yyyy");
    SimpleDateFormat d4 = new SimpleDateFormat("yyyy-MM-dd");

    String toDate(String ddd) {
        try {
            return d4.format(d1.parse(ddd));
        } catch (Exception e) {
        }
        try {
            return d4.format(d2.parse(ddd));
        } catch (Exception e) {
        }
        try {
            return d4.format(d3.parse(ddd));
        } catch (Exception e) {
        }
        return null;
    }

}
