/*
DELETE FROM class_transfer;
DELETE FROM student_transportation;
DELETE FROM stu_billing_detail;
DELETE FROM stu_billing_master;
DELETE FROM student_info;
 */
package com.ms.ware.online.solution.school.excel;

import com.ms.ware.online.solution.school.config.DB;
import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.dao.student.StudentInfoDao;
import com.ms.ware.online.solution.school.dao.student.StudentInfoDaoImp;
import com.ms.ware.online.solution.school.entity.student.StudentInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ExcelData {
    @Autowired
    private DB db;
    public static int totalRecord = 0;

    private Message message = new Message();

    public Object doImport(String fileName, String importDate) {
        try {
            StudentInfoDao da = new StudentInfoDaoImp();
            double opening = 0;
            int totalColumn = 30, sheet = 0, count = 0, i;
            Object[][] data = new Excel().read(fileName, totalColumn, sheet);

            ExcelData.totalRecord = data.length;
            StudentInfo obj = new StudentInfo();
            int province = 3;
            String district = "", municipal = "", wardNo = "", sql;
            sql = "SELECT IFNULL(PROVINCE,'') province,IFNULL(DISTRICT,'') district,IFNULL(MUNICIPAL,'') municipal,IFNULL(WARD_NO,'') wardNo FROM organization_master ";
            System.out.println(sql);
            Map map = (Map) da.getRecord(sql).get(0);
            province = Integer.parseInt(map.get("province").toString());
            district = map.get("district").toString();
            municipal = map.get("municipal").toString();
            wardNo = map.get("wardNo").toString();
            long id;
            int row;

            String msg, hostelStatus, religion;
            try {
                sql = "INSERT INTO program_master (ID, NAME) VALUES (1, 'General');";
                da.delete(sql);
                sql = "INSERT INTO subject_group (ID, NAME) VALUES (1, 'General Group');";
                da.delete(sql);
                sql = "DELETE FROM class_transfer";
                da.delete(sql);
                sql = "DELETE FROM student_info";
                da.delete(sql);
                sql = "DELETE FROM temp_not_import";
                da.delete(sql);
            } catch (Exception e) {

            }
            Date date = new Date();

            int totalRow = data.length;
            System.out.println("totalRow " + totalRow);
            for (i = 1; i < totalRow; i++) {
//                for (int j = 0; j < data[i].length; j++) {
//                    System.out.print(" " + data[i][j]);
//                }
//                System.out.println();
                try {
                    id = Long.parseLong(data[i][0].toString());
                    obj.setId(id);
                    try {
                        obj.setSn(Integer.parseInt(data[i][1].toString()));
                    } catch (Exception e) {
                    }
                    obj.setProgram(1l);
                    try {
                        obj.setAcademicYear(Long.parseLong(data[i][2].toString()));
                    } catch (Exception e) {
                        obj.setAcademicYear(77l);
                    }
                    obj.setSubjectGroup(1l);
                    obj.setClassId(Long.parseLong(data[i][3].toString()));
                    obj.setSection(data[i][4].toString());

                    try {
                        obj.setRollNo(Integer.parseInt(data[i][5].toString()));
                    } catch (Exception e) {
                        obj.setRollNo(i + 1);
                    }
                    obj.setStuName(data[i][6].toString());
                    try {
                        obj.setDateOfBirth(toDate(data[i][7].toString()));
                    } catch (Exception e) {
                    }
                    try {
                        if (data[i][8].toString().contains("Female")) {
                            obj.setGender("F");
                        } else {
                            obj.setGender("M");
                        }
                    } catch (Exception e) {
                    }
                    hostelStatus = "N";
                    try {
                        obj.setMobileNo(data[i][9].toString());
                    } catch (Exception e) {
                    }
                    try {
                        obj.setEmail(data[i][10].toString());
                    } catch (Exception e) {
                    }
                    try {
                        obj.setProvince(Integer.parseInt(data[i][11].toString()));
                        obj.setDistrict(data[i][12].toString());
                        obj.setMunicipal(data[i][13].toString());

                        obj.setWardNo(data[i][14].toString());
                    } catch (Exception e) {
                    }
                    try {
                        obj.setTol(data[i][15].toString());
                    } catch (Exception e) {
                    }
                    obj.setProvincet(obj.getProvince());
                    obj.setMunicipalt(obj.getMunicipal());
                    obj.setDistrictt(obj.getDistrict());
                    obj.setWardNot(obj.getWardNo());
                    obj.setTolt(obj.getTol());
                    try {
                        obj.setFathersName(data[i][16].toString());
                        obj.setFathersMobile(data[i][17].toString());
                        obj.setFathersOccupation(data[i][18].toString());
                        obj.setFathersQualification("");
                        obj.setFathersDesignation("");
                    } catch (Exception e) {
                    }
                    try {
                        obj.setMothersName(data[i][19].toString());
                        obj.setMothersMobile(data[i][20].toString());
                        obj.setMothersOccupation(data[i][21].toString());
                        obj.setMothersQualification("");
                        obj.setMothersDesignation("");
                    } catch (Exception e) {
                    }
                    try {
                        obj.setGuardiansName(obj.getFathersName());
                        obj.setGuardiansMobile(obj.getFathersMobile());
                        obj.setGuardiansRelation("Father");
                    } catch (Exception e) {
                    }
                    try {
                        religion = data[i][25].toString();
                        obj.setReligion(getReligionId(religion));
                    } catch (Exception e) {
                    }
                    try {
                        obj.setCastEthnicity(setCastEthnicity(data[i][26].toString()));
                    } catch (Exception e) {
                    }
                    try {
                        String d = toDate(data[i][27].toString());
                        if (d == null) {
                            obj.setEnterDateAD(new Date());
                        }
                    } catch (Exception e) {
                        obj.setEnterDateAD(new Date());
                    }
                    obj.setEnterBy("SYSTEM");
                    obj.setStatus("Y");

                    obj.setAdmissionYear(obj.getAcademicYear().toString());

                    try {
                        if (obj.getEnterDate().length() != 10) {
                            obj.setEnterDateAD(date);
                        }
                    } catch (Exception e) {
                        obj.setEnterDateAD(date);
                    }
                    obj.setDropOut("N");
                    obj.setAlternativeMobile("");
                    obj.setMaritalStatus("Unmarried");
                    obj.setDisability("Normal");
                    obj.setCitizenship("Nepalese");
                    obj.setGuardiansAddrress("");
                    obj.setPhoto("");
                    obj.setPreSchool("");

//                    obj.setG
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
//                   {
//                        sql = "INSERT INTO temp_not_import (ID,MSG) VALUES (" + id + ", '" + msg.replace("'", " ") + "')";
//                        da.delete(sql);
//                    }
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
            return d4.format(d3.parse(ddd));
        } catch (Exception e) {
        }
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
        return ddd;
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
        } else if (name.equalsIgnoreCase("NUR") || name.equalsIgnoreCase("NUR") || name.equalsIgnoreCase("NUR")) {
            return 13l;
        } else if (name.equalsIgnoreCase("LKG") || name.equalsIgnoreCase("LKG") || name.equalsIgnoreCase("LKG")) {
            return 14l;
        } else if (name.equalsIgnoreCase("UKG") || name.equalsIgnoreCase("UKG") || name.equalsIgnoreCase("UKG")) {
            return 15l;
        }
        return 0;
    }

    //    void saveOpening(Long regNo, int index, Long academcYear, Long program, Long classId, Long subjectGroup, Long fiscalYear, String date, double amount) {
//        String sql = "INSERT INTO stu_billing_master(BILL_NO,BILL_SN,BILL_TYPE,REG_NO,ACADEMIC_YEAR,PROGRAM,CLASS_ID,SUBJECT_GROPU,FISCAL_YEAR,ENTER_BY,ENTER_DATE,APPROVE_BY,APPROVE_DATE,AUTO_GENERATE,BILL_AMOUNT) "
//                + "VALUES('" + regNo + "'," + index + ",'CR', '" + regNo + "', '" + academcYear + "'," + program + ",'" + classId + "','" + subjectGroup + "','" + fiscalYear + "','SYSTEM','" + date + "','SYSTEM','" + date + "','N','" + amount + "');";
//        int row = db.save(sql);
//        sql = "INSERT INTO stu_billing_detail(BILL_NO,BILL_SN,REG_NO,BILL_ID,ACADEMIC_YEAR,PROGRAM,CLASS_ID,CR,DR,PAYMENT_DATE)"
//                + " VALUES('" + regNo + "',1,'" + regNo + "','0','" + academcYear + "','" + program + "','" + classId + "','" + amount + "','0','" + date + "');";
//        row = db.save(sql);
//    }
    private Integer setCastEthnicity(String name) {
        String sql = "SELECT `ID` id FROM cast_ethnicity_master WHERE `NAME` LIKE '%" + name + "%'";
        List list = db.getRecord(sql);
        Map map;
        try {
            if (list.isEmpty()) {
                sql = "SELECT IFNULL(MAX( `ID`),0)+1 id FROM cast_ethnicity_master";
                list = db.getRecord(sql);
                map = (Map) list.get(0);
                sql = "INSERT INTO cast_ethnicity_master (`ID`, `NAME`) VALUES (" + map.get("id") + ", '" + name + "')";
                if (db.save(sql) == 1) {
                    return Integer.parseInt(map.get("id").toString());
                }
            } else {
                map = (Map) list.get(0);
                return Integer.parseInt(map.get("id").toString());

            }

        } catch (Exception e) {
        }
        return null;
    }

//    public static void main(String[] args) {
//        new ExcelData().doImport("C:\\Users\\MS\\Desktop\\school.xlsx", "2021-02-14");
//    }
}
