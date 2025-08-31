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
import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.dao.student.StudentInfoDao;
import com.ms.ware.online.solution.school.dao.student.StudentInfoDaoImp;
import com.ms.ware.online.solution.school.entity.student.StudentInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ReadJetkingExcelData {
    private DB db = new DB();
    public static int totalRecord = 0;

    private Message message=new Message();
//
//    public static void main(String[] args) {
//        Object[][] data = new Excel().read("E:\\ms\\ware\\student.xlsx", 60, 0);
//        int i;
//        long id;
//        double opening;
//        ReadJetkingExcelData d = new ReadJetkingExcelData();
//        for (i = 0; i < data.length; i++) {
//
//            try {
//                id = Long.parseLong(data[i][0].toString());
//                try {
//                    opening = Double.parseDouble(data[i][39].toString());
//                } catch (Exception e) {
//                    opening = 0;
//                }
//                if (opening > 0) {
//                    id = Long.parseLong(data[i][0].toString());
//
//
//                    d.saveOpening(id, i, 80L, 1L, d.getCLassID(data[i][6].toString()), 1L, 8081L, "2023-08-17", opening);
//                }
//            } catch (Exception e) {
//            }
//        }
//    }

    public Object doImport(String fileName, String importDate, long academicYear) {
        try {
            importDate = DateConverted.bsToAd(importDate);
            StudentInfoDao da = new StudentInfoDaoImp();
            double opening;
            int totalColumn = 60, sheet = 0, count = 0, i;
            Object[][] data = new Excel().read(fileName, totalColumn, sheet);

            ReadJetkingExcelData.totalRecord = data.length;
            StudentInfo obj = new StudentInfo();
            int province;
            String district = "", municipal = "", wardNo = "", sql;
            sql = "SELECT IFNULL(PROVINCE,'') province,IFNULL(DISTRICT,'') district,IFNULL(MUNICIPAL,'') municipal,IFNULL(WARD_NO,'') wardNo FROM organization_master ";
            Map map = da.getRecord(sql).get(0);
            province = Integer.parseInt(map.get("province").toString());
            district = map.get("district").toString();
            municipal = map.get("municipal").toString();
            wardNo = map.get("wardNo").toString();
            long id;
            int row;

            String msg, hostelStatus, religion;
            try {
                sql = "INSERT INTO class_master (ID, NAME) VALUES (16, 'PG');";
                da.delete(sql);

                sql = "INSERT INTO program_master (ID, NAME) VALUES (1, 'General');";
                da.delete(sql);
                sql = "INSERT INTO subject_group (ID, NAME) VALUES (1, 'General Group');";
                da.delete(sql);


//                sql = "DELETE FROM class_transfer";
//                da.delete(sql);
//                sql = "DELETE FROM student_info";
//                da.delete(sql);
                sql = "DELETE FROM temp_not_import";
                da.delete(sql);
            } catch (Exception e) {

            }
            Date date = new Date();
            String busStop;
            int totalRow = data.length;
            for (i = 0; i < totalRow; i++) {

                try {
                    id = Long.parseLong(data[i][0].toString());
                    obj.setId(id);
                    obj.setStuName(data[i][4] + " " + data[i][5]);
                    obj.setProgram(1L);
                    obj.setSubjectGroup(1L);
                    obj.setAcademicYear(academicYear);
                    obj.setClassId(getClassID(data[i][6].toString()));
                    try {
                        obj.setSection(data[i][7].toString());
                        if (obj.getSection().length() != 1) obj.setSection("A");
                    } catch (Exception e) {
                        obj.setSection("A");
                    }
                    try {
                        obj.setRollNo(Integer.parseInt(data[i][8].toString()));
                    } catch (Exception e) {
                        obj.setRollNo(i + 1);
                    }
                    if (data[i][9].toString().contains("FALSE")) {
                        obj.setGender("F");
                    } else {
                        obj.setGender("M");
                    }
                    hostelStatus = data[i][10].toString();
                    obj.setDateOfBirth(toDate(data[i][12].toString()));
                    religion = data[i][15].toString();
                    obj.setReligion(getReligionId(religion));
                    obj.setPreSchool(data[i][17].toString());
                    busStop = data[i][18].toString();
                    obj.setEnterDateAD(toDate(data[i][19].toString()));
                    obj.setFathersName(data[i][21].toString());
                    obj.setMothersName(data[i][22].toString());
                    obj.setTol(data[i][23].toString());
                    obj.setMobileNo(data[i][24].toString());
                    obj.setCastEthnicity(0);
                    obj.setEnterBy("SYSTEM");
                    obj.setStatus("Y");
                    obj.setProvince(province);
                    obj.setDistrict(district);
                    obj.setMunicipal(municipal);
                    obj.setWardNo(wardNo);
                    obj.setProvincet(province);
                    obj.setDistrictt(district);
                    obj.setMunicipalt(municipal);
                    obj.setWardNot(wardNo);
                    obj.setTolt(obj.getTol());
                    obj.setAdmissionYear(obj.getAcademicYear().toString());
                    try {
                        opening = Double.parseDouble(data[i][39].toString());
                    } catch (Exception e) {
                        opening = 0;
                    }
                    try {
                        if (obj.getEnterDate().length() != 10) {
                            obj.setEnterDateAD(date);
                        }
                    } catch (Exception e) {
                        obj.setEnterDateAD(date);
                    }
                    row = da.save(obj);
                    msg = da.getMsg().replace("`", "").toLowerCase();
                    if (row == 0) {
                        if (msg.contains("roll_no") || msg.indexOf("roll_no") >= 0) {
                            obj.setSection("B");
                            obj.setRollNo(Integer.parseInt(i + "0" + 1));
                            row = da.save(obj);
                            msg = da.getMsg().replace("`", "").toLowerCase();
                        }
                    }
                    if (row == 0) {
                       
                        if (msg.contains("foreign key") && msg.contains("academic_year")) {
                            sql = "INSERT INTO academic_year (ID, STATUS,YEAR) VALUES (" + obj.getAcademicYear() + ", 'N', ' 20" + obj.getAcademicYear() + "');";
                            da.delete(sql);
                            row = da.save(obj);
                            msg = da.getMsg().replace("", "").toLowerCase();
                        }
                        if (msg.contains("foreign key") && msg.contains("cast_ethnicity_master")) {
                            sql = "INSERT INTO cast_ethnicity_master (ID, NAME) VALUES (0, 'N/A');";
                            da.delete(sql);
                            row = da.save(obj);
                            msg = da.getMsg().replace("", "").toLowerCase();
                        }
                        if (msg.contains("foreign key") && msg.contains("religion_master")) {
                            sql = "INSERT INTO religion_master (ID, NAME) VALUES (0, 'N/A');";
                            da.delete(sql);
                            row = da.save(obj);
                            msg = da.getMsg().replace("", "").toLowerCase();
                        }
                        if (msg.contains("foreign key") && msg.contains("class_master")) {
                            if (obj.getClassId() != 0) {
                                sql = "INSERT INTO class_master (ID, NAME) VALUES (" + obj.getClassId() + ", '" + getCLassName(obj.getClassId()) + "');";
                                da.delete(sql);
                                row = da.save(obj);
                            }
                            msg = da.getMsg().replace("`", "").toLowerCase();
                        }
                        if (msg.contains("foreign key") && msg.contains("subject_group")) {
                            if (obj.getClassId() != 0) {
                                sql = "INSERT INTO subject_group (ID,NAME) VALUES (1, 'General');";
                                da.delete(sql);
                                obj.setSubjectGroup(1l);
                                row = da.save(obj);
                            }
                            msg = da.getMsg().replace("`", "").toLowerCase();
                        }
                    }
                    if (row == 1) {
                        if (busStop.length() > 2) {
                            saveStudentTransportation(i, busStop, importDate, id);
                        }
                        if (hostelStatus.equalsIgnoreCase("2")) {
                            saveSchoolHostel(i, "1", importDate, id);
                        } else if (hostelStatus.equalsIgnoreCase("3")) {
                            saveSchoolHostel(i, "2", importDate, id);
                        }
//                        void saveOpening(Long regNo, Long academcYear, Long program, Long classId, Long subjectGroup, Long fiscalYear, String date, double amount) {
                        if (opening > 0) {
                            saveOpening(id, i, obj.getAcademicYear(), obj.getProgram(), obj.getClassId(), obj.getSubjectGroup(), 8081L, obj.getEnterDate(), opening);
                        }

                        count++;

                    } else {
                        sql = "INSERT INTO temp_not_import (ID,MSG) VALUES (" + id + ", '" + msg.replace("'", " ") + "')";
                        da.delete(sql);
                    }
                    System.gc();
                } catch (Exception e) {
                    System.out.println(i + "  " + e.getMessage());
                }
            }

            sql = "INSERT INTO class_transfer (ACADEMIC_YEAR, STUDENT_ID, CLASS_ID, PROGRAM,  SUBJECT_GROUP,ROLL_NO, SECTION) SELECT ACADEMIC_YEAR,ID,CLASS_ID,PROGRAM,SUBJECT_GROUP,ROLL_NO,SECTION FROM student_info S WHERE S.`ID` NOT IN(SELECT CLL.`STUDENT_ID` FROM class_transfer CLL)";
            da.delete(sql);
            return message.respondWithMessage(count + " Record Saved!!");
        } catch (Exception e) {
            return message.respondWithError(e.getMessage());
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

    String getCLassName(long id) {
        if (id == 1) {
            return "One";
        } else if (id == 2) {
            return "Two";
        } else if (id == 3) {
            return "Three";
        } else if (id == 4) {
            return "Four";
        } else if (id == 5) {
            return "Five";
        } else if (id == 6) {
            return "Six";
        } else if (id == 7) {
            return "Seven";
        } else if (id == 8) {
            return "Eight";
        } else if (id == 9) {
            return "Nine";
        } else if (id == 10) {
            return "Ten";
        } else if (id == 11) {
            return "Eleven";
        } else if (id == 12) {
            return "Twelve";
        } else if (id == 13) {
            return "Nursery";
        } else if (id == 14) {
            return "LKG";
        } else if (id == 15) {
            return "UKG";
        } else if (id == 16) {
            return "PG";
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
            List<Map<String, Object>>  ll = db.getRecord(sql);
            if (ll.isEmpty()) {
                sql = "SELECT IFNULL(MAX(ID),0)+1 id FROM bus_station_master";
                Map<String, Object> map =  db.getRecord(sql).get(0);
                long chargeId = Long.parseLong(map.get("id").toString());
                chargeAmount = 0;
                sql = "INSERT INTO bus_station_master (ID, NAME, CHARGE_AMOUNT) VALUES (" + chargeId + ", '" + busStop + "', " + chargeAmount + ");";
                db.save(sql);
            } else {
                map =  ll.get(0);
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
        }else if (name.contains("other")) {
            return 9;
        } else {
            return 0;
        }
    }

   public long getClassID(String name) {

        if (name.equalsIgnoreCase("1") || name.contains("One") || name.equalsIgnoreCase("I")) {
            return 1L;
        } else if (name.equalsIgnoreCase("2") || name.contains("Two") || name.equalsIgnoreCase("II")) {
            return 2L;
        } else if (name.equalsIgnoreCase("3") || name.contains("Three") || name.equalsIgnoreCase("III")) {
            return 3L;
        } else if (name.equalsIgnoreCase("4") || name.contains("Four") || name.equalsIgnoreCase("IV")) {
            return 4L;
        } else if (name.equalsIgnoreCase("5") || name.contains("Five") || name.equalsIgnoreCase("V")) {
            return 5L;
        } else if (name.equalsIgnoreCase("6") || name.contains("Six") || name.equalsIgnoreCase("VI")) {
            return 6L;
        } else if (name.equalsIgnoreCase("7") || name.contains("Seven") || name.equalsIgnoreCase("VII")) {
            return 7L;
        } else if (name.equalsIgnoreCase("8") || name.contains("Eight") || name.equalsIgnoreCase("VIII")) {
            return 8L;
        } else if (name.equalsIgnoreCase("9") || name.contains("Nine") || name.equalsIgnoreCase("IX")) {
            return 9L;
        } else if (name.equalsIgnoreCase("10") || name.contains("Ten") || name.equalsIgnoreCase("X")) {
            return 10L;
        } else if (name.equalsIgnoreCase("11") || name.contains("Eleven") || name.equalsIgnoreCase("XI")) {
            return 11L;
        } else if (name.equalsIgnoreCase("12") || name.equalsIgnoreCase("Twelve") || name.equalsIgnoreCase("XII")) {
            return 12L;
        } else if (name.equalsIgnoreCase("NUR") || name.equalsIgnoreCase("NUR") || name.equalsIgnoreCase("NUR")) {
            return 13L;
        } else if (name.equalsIgnoreCase("LKG") || name.equalsIgnoreCase("LKG") || name.equalsIgnoreCase("LKG")) {
            return 14L;
        } else if (name.equalsIgnoreCase("UKG") || name.equalsIgnoreCase("UKG") || name.equalsIgnoreCase("UKG")) {
            return 15L;
        } else if (name.equalsIgnoreCase("PG")) {
            return 16L;
        }
        return 0;
    }

    void saveOpening(Long regNo, int index, Long academcYear, Long program, Long classId, Long subjectGroup, Long fiscalYear, String date, double amount) {
        String sql = "INSERT INTO stu_billing_master(BILL_NO,BILL_SN,BILL_TYPE,REG_NO,ACADEMIC_YEAR,PROGRAM,CLASS_ID,SUBJECT_GROPU,FISCAL_YEAR,ENTER_BY,ENTER_DATE,APPROVE_BY,APPROVE_DATE,AUTO_GENERATE,BILL_AMOUNT) "
                + "VALUES('" + regNo + "'," + index + ",'CR', '" + regNo + "', '" + academcYear + "'," + program + ",'" + classId + "','" + subjectGroup + "','" + fiscalYear + "','SYSTEM','" + date + "','SYSTEM','" + date + "','N','" + amount + "');";
        int row = db.save(sql);
        sql = "INSERT INTO stu_billing_detail(BILL_NO,BILL_SN,REG_NO,BILL_ID,ACADEMIC_YEAR,PROGRAM,CLASS_ID,CR,DR,PAYMENT_DATE)"
                + " VALUES('" + regNo + "',1,'" + regNo + "','0','" + academcYear + "','" + program + "','" + classId + "','" + amount + "','0','" + date + "');";
        row = db.save(sql);
    }
}
