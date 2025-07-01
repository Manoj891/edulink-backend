/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.service.configure;

import com.ms.ware.online.solution.school.config.DateConverted;
import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.excel.ReadJetkingExcelData;
import com.ms.ware.online.solution.school.model.DatabaseName;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


@Service
public class ImportJetkingServiceImpl {


    public Object importedRecord() {
        java.sql.Connection con = null;

        Message message = new Message();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = java.sql.DriverManager.getConnection("jdbc:mysql://localhost:" + DatabaseName.getPort() + "/" + DatabaseName.getDatabase(), DatabaseName.getUsername(), DatabaseName.getPassword());
            String sql;
            sql = "SELECT COUNT(ID) FROM student_info";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            rs.next();
            int total = rs.getInt(1);
            con.close();
            ps.close();
            rs.close();
            if (total > 0) {
                return message.respondWithMessage(total + " Record imported of " + ReadJetkingExcelData.totalRecord);
            } else {
                return message.respondWithMessage("Database configuration starting");
            }
        } catch (Exception e) {
            try {
                con.close();
            } catch (Exception ex) {
            }
        }
        return message.respondWithMessage("Database configuration starting");
    }

    public Object importStudentData(String effectDate, MultipartFile excelFile, long academicYear) {
        Message message = new Message();
        try {
            if (excelFile.getSize() < 100) {
                return message.respondWithError("Please provide file");
            }


            String filePath =message.getFilepath( DatabaseName.getDocumentUrl());
            File f = new File(filePath);
            try {
                if (!f.exists()) {
                    f.mkdirs();
                }
            } catch (Exception e) {
                return message.respondWithError(e.getMessage());
            }
            String fileName = filePath + "/" + excelFile.getOriginalFilename();

            f = new File(fileName);
            excelFile.transferTo(f);

            effectDate = DateConverted.bsToAd(effectDate);
            return new ReadJetkingExcelData().doImport(fileName, effectDate, academicYear);
        } catch (Exception e) {
            return message.respondWithError(e.getMessage());
        }

    }


    public Object doConfigNew(String effectDate, String webapps, MultipartFile excelFile, long academicYear) {
        Message message = new Message();
        try {
            if (excelFile.getSize() < 100) {
                return message.respondWithError("Please provide file");
            }

            webapps = webapps.replace("\\", "/");
            String filePath = webapps + DatabaseName.getDocumentUrl() + "Document/";
            File f = new File(filePath);
            try {
                if (!f.exists()) {
                    f.mkdirs();
                }
            } catch (Exception e) {
                return message.respondWithError(e.getMessage());
            }
            String fileName = filePath + "/" + excelFile.getOriginalFilename();
            fileName = fileName.replace("//", "/");
            f = new File(fileName);
            try {
                if (f.exists()) {
                    f.deleteOnExit();
                }
            } catch (Exception e) {
            }
            f = new File(fileName);
            excelFile.transferTo(f);

            effectDate = DateConverted.bsToAd(effectDate);
            return new ReadJetkingExcelData().doImport(fileName, effectDate, academicYear);
        } catch (Exception e) {
            return message.respondWithError(e.getMessage());
        }

    }
}
