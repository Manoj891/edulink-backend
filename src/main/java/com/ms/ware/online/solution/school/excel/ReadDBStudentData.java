/*
DELETE FROM class_transfer;
DELETE FROM student_transportation;
DELETE FROM stu_billing_detail;
DELETE FROM stu_billing_master;
DELETE FROM student_info;
 */
package com.ms.ware.online.solution.school.excel;

import com.ms.ware.online.solution.school.config.DB;
import com.ms.ware.online.solution.school.config.DateConverted;
import com.ms.ware.online.solution.school.dao.student.StudentInfoDao;
import com.ms.ware.online.solution.school.dao.student.StudentInfoDaoImp;
import com.ms.ware.online.solution.school.entity.student.StudentInfo;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ReadDBStudentData {
    @Autowired
    private DB db;
    @Autowired
    private  StudentInfoDao da;
    public void doImport(String fileName) {


        String gender, sql;
        try {
            File file = new File(fileName);

            FileInputStream inputStream = new FileInputStream(file);
            Workbook excel = new XSSFWorkbook(inputStream);


            StudentInfo obj = new StudentInfo();

            long id, classId;

//            sql = "ALTER TABLE student_info ADD link_student VARCHAR(2)";
//            da.delete(sql);
//            sql = "ALTER TABLE student_info ADD reg_no_link BIGINT";
//            da.delete(sql);
//            sql = "CREATE TABLE temp_not_import (`ID` BIGINT, `MSG` TEXT);";
//            da.delete(sql);
//            sql = "insert into religion_master(ID, NAME) values (1,'N/A')";
//            da.delete(sql);
//            sql = "insert into cast_ethnicity_master(ID, NAME) values (1,'N/A')";
//            da.delete(sql);
//            sql = "INSERT INTO program_master (ID, NAME) VALUES (1, 'General');";
//            da.delete(sql);
//            sql = "INSERT INTO subject_group (ID, NAME) VALUES (1, 'General Group');";
//            da.delete(sql);
//            sql = "DELETE FROM temp_not_import";
//            da.delete(sql);

            Date date = new Date();

            Sheet excelSheet = excel.getSheet("science");
            int rollNo = 1;
            for (Row row : excelSheet) {

                try {
                    id = (long) row.getCell(2).getNumericCellValue();
                    obj.setId(id);
                    obj.setAcademicYear(78L);
                    obj.setClassId(11L);
                    obj.setSection("A");
                    obj.setRollNo((int) row.getCell(0).getNumericCellValue());
//                    System.out.println(obj.getRollNo());
//                    if(true)continue;
                    obj.setStuName(row.getCell(1).getStringCellValue());
                    try {
                        obj.setDateOfBirth(DateConverted.toString(row.getCell(6).getDateCellValue()));
                    } catch (Exception e) {
                    }
                    try {
                        gender = row.getCell(4).getStringCellValue();
                        if (gender.contains("Female"))
                            obj.setGender("F");
                        else if (gender.contains("Male")) obj.setGender("M");
                    } catch (Exception e) {
                    }
                    try {
                        obj.setMobileNo(row.getCell(9).getStringCellValue());
                    } catch (Exception e) {
                    }
                    try {
                        obj.setEmail(row.getCell(3).getStringCellValue());
                    } catch (Exception e) {
                    }
                    try {
                        obj.setFathersName(row.getCell(7).getStringCellValue());
                    } catch (Exception e) {
                    }
                    try {
                        obj.setFathersOccupation(row.getCell(8).getStringCellValue());
                    } catch (Exception e) {
                    }
                    try {
                        obj.setFathersMobile(row.getCell(9).getStringCellValue());
                    } catch (Exception e) {
                    }
                    try {
                        obj.setMothersName(row.getCell(10).getStringCellValue());
                    } catch (Exception e) {
                    }
                    try {
                        obj.setMothersOccupation(row.getCell(11).getStringCellValue());
                    } catch (Exception e) {
                    }
                    try {
                        obj.setMothersMobile(row.getCell(12).getStringCellValue());
                    } catch (Exception e) {
                    }
                    try {
                        obj.setTol(row.getCell(14).getStringCellValue());
                    } catch (Exception e) {
                    }

                    obj.setDistrict("");
                    obj.setMunicipal("");
                    obj.setWardNo("");
                    obj.setAdmissionYear("78");
                    obj.setEnterBy("SYSTEM");
                    obj.setStatus("Y");
                    obj.setSubjectGroup(1L);
                    obj.setReligion(1);
                    obj.setCastEthnicity(1);
                    obj.setProgram(2L);
                    obj.setEnterDateAD(date);


                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    continue;
                }
//                System.out.println(obj);
//                if (true) continue;

                if (da.save(obj) != 1) {
                    sql = "INSERT INTO temp_not_import (ID,MSG) VALUES (" + id + ", '" + da.getMsg().replace("'", " ") + "')";
                    da.delete(sql);
                }
            }

        } catch (Exception e) {
        }

        sql = "INSERT INTO class_transfer (ACADEMIC_YEAR, STUDENT_ID, CLASS_ID, PROGRAM,  SUBJECT_GROUP,ROLL_NO, SECTION) SELECT ACADEMIC_YEAR,ID,CLASS_ID,PROGRAM,SUBJECT_GROUP,ROLL_NO,SECTION FROM student_info S WHERE S.`ID` NOT IN(SELECT CLL.`STUDENT_ID` FROM class_transfer CLL)";
        da.delete(sql);
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

    String getCLassName(int id) {
        switch (id) {
            case 1:
                return "One";
            case 2:
                return "Two";
            case 3:
                return "Three";
            case 4:
                return "Four";
            case 5:
                return "Five";
            case 6:
                return "Six";
            case 7:
                return "Seven";
            case 8:
                return "Eight";
            case 9:
                return "Nine";
            case 10:
                return "Ten";
            case 11:
                return "Eleven";
            case 12:
                return "Twelve";
            case 13:
                return "Nursery";
            case 14:
                return "LKG";
            case 15:
                return "UKG";
            default:
                break;
        }
        return "";
    }


    long chargeId;
    float chargeAmount;
    Map map;

    void saveStudentTransportation(int i, String busStop, String importDate, long regNo) {

        try {
            busStop = busStop.replace("'", "");
            String sql = "SELECT ID id,CHARGE_AMOUNT chargeAmount FROM bus_station_master  WHERE NAME='" + busStop + "'";
            List ll = db.getRecord(sql);
            if (ll.isEmpty()) {
                sql = "SELECT IFNULL(MAX(ID),0)+1 id FROM bus_station_master";
                Map map = (Map) db.getRecord(sql).get(0);
                long chargeId = Long.parseLong(map.get("id").toString());
                chargeAmount = 0;
                sql = "INSERT INTO bus_station_master (ID, NAME, CHARGE_AMOUNT) VALUES (" + chargeId + ", '" + busStop + "', " + chargeAmount + ");";
                db.save(sql);
            } else {
                map = (Map) ll.get(0);
                chargeId = Long.parseLong(map.get("id").toString());
                chargeAmount = Float.parseFloat(map.get("chargeAmount").toString());
            }
            sql = "INSERT INTO student_transportation (ID, STATION, MONTHLY_CHARGE, REG_NO, START_DATE, STATUS, BILL_ID) VALUES (" + i + ", " + chargeId + ", " + chargeAmount + " , " + regNo + ", '" + importDate + "', 'Y', -1);";
            db.delete(sql);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    void saveSchoolHostel(int i, String hostelType, String importDate, long regNo) {

        try {
            String sql = "INSERT INTO school_hostal (ID, BILL_ID, MONTHLY_CHARGE, REG_NO, START_DATE, STATUS, HOSTEL_TYPE) VALUES (" + i + ", -2, 0, " + regNo + ", '" + importDate + "', 'Y', " + hostelType + ");";
            db.delete(sql);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    int getReligionId(String name) {
        name = name.toLowerCase();
        if (name.contains("hin")) {
            return 1;
        } else if (name.contains("bud")) {
            return 2;
        } else if (name.contains("is") || name.contains("mu")) {
            return 3;
        } else if (name.contains("chri") || name.contains("kri") || name.contains("ris")) {
            return 4;
        } else if (name.contains("si") || name.contains("se")) {
            return 5;
        } else if (name.contains("jain")) {
            return 6;
        } else if (name.contains("zoro")) {
            return 7;
        } else if (name.contains("bud")) {
            return 8;
        } else if (name.contains("other")) {
            return 9;
        } else {
            return 0;
        }
    }

    long getCLassID(String name) {

        if (name.equalsIgnoreCase("1") || name.contains("One") || name.equalsIgnoreCase("I")) {
            return 1l;
        } else if (name.equalsIgnoreCase("2") || name.contains("Two") || name.equalsIgnoreCase("II")) {
            return 2l;
        } else if (name.equalsIgnoreCase("3") || name.contains("Three") || name.equalsIgnoreCase("III")) {
            return 3l;
        } else if (name.equalsIgnoreCase("4") || name.contains("Four") || name.equalsIgnoreCase("IV")) {
            return 4l;
        } else if (name.equalsIgnoreCase("5") || name.contains("Five") || name.equalsIgnoreCase("V")) {
            return 5l;
        } else if (name.equalsIgnoreCase("6") || name.contains("Six") || name.equalsIgnoreCase("VI")) {
            return 6l;
        } else if (name.equalsIgnoreCase("7") || name.contains("Seven") || name.equalsIgnoreCase("VII")) {
            return 7l;
        } else if (name.equalsIgnoreCase("8") || name.contains("Eight") || name.equalsIgnoreCase("VIII")) {
            return 8l;
        } else if (name.equalsIgnoreCase("9") || name.contains("Nine") || name.equalsIgnoreCase("IX")) {
            return 9l;
        } else if (name.equalsIgnoreCase("10") || name.contains("Ten") || name.equalsIgnoreCase("X")) {
            return 10l;
        } else if (name.equalsIgnoreCase("11") || name.contains("Eleven") || name.equalsIgnoreCase("XI")) {
            return 11l;
        } else if (name.equalsIgnoreCase("12") || name.equalsIgnoreCase("Twelve") || name.equalsIgnoreCase("XII")) {
            return 12l;
        } else if (name.equalsIgnoreCase("NUR") || name.equalsIgnoreCase("Nursery") || name.equalsIgnoreCase("NUR")) {
            return 13l;
        } else if (name.equalsIgnoreCase("LKG") || name.equalsIgnoreCase("LKG") || name.equalsIgnoreCase("LKG")) {
            return 14l;
        } else if (name.equalsIgnoreCase("UKG") || name.equalsIgnoreCase("UKG") || name.equalsIgnoreCase("UKG")) {
            return 15l;
        }
        return 0;
    }

    void saveOpening(Long regNo, int index, Long academcYear, Long program, Long classId, Long subjectGroup, Long
            fiscalYear, String date, double amount) {
        String sql = "INSERT INTO stu_billing_master(BILL_NO,BILL_SN,BILL_TYPE,REG_NO,ACADEMIC_YEAR,PROGRAM,CLASS_ID,SUBJECT_GROPU,FISCAL_YEAR,ENTER_BY,ENTER_DATE,APPROVE_BY,APPROVE_DATE,AUTO_GENERATE,BILL_AMOUNT) "
                + "VALUES('" + regNo + "'," + index + ",'CR', '" + regNo + "', '" + academcYear + "'," + program + ",'" + classId + "','" + subjectGroup + "','" + fiscalYear + "','SYSTEM','" + date + "','SYSTEM','" + date + "','N','" + amount + "');";
        int row = db.save(sql);
        sql = "INSERT INTO stu_billing_detail(BILL_NO,BILL_SN,REG_NO,BILL_ID,ACADEMIC_YEAR,PROGRAM,CLASS_ID,CR,DR,PAYMENT_DATE)"
                + " VALUES('" + regNo + "',1,'" + regNo + "','0','" + academcYear + "','" + program + "','" + classId + "','" + amount + "','0','" + date + "');";
        row = db.save(sql);
    }
}
