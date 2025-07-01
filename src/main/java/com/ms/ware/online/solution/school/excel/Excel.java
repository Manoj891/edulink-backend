/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.excel;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Excel {

    public Object[][] read(String fileName, int totalCol, int sheet) {
        //Create an object of File class to open xlsx file
        Object[][] data = new Object[0][0];
        try {
            File file = new File(fileName);
            Workbook excel = null;
//            String fileExtensionName = fileName.substring(fileName.indexOf("."), fileName.length());
//            System.out.println("----------\n"+fileExtensionName+"\n");
            FileInputStream inputStream = new FileInputStream(file);
//            if (fileExtensionName.equals(".xlsx")) {
            excel = new XSSFWorkbook(inputStream);
//            } else if (fileExtensionName.equals(".xls")) {
//                excel = new HSSFWorkbook(inputStream);
//            }

            try {
                Sheet excelSheet = excel.getSheetAt(sheet);
                int rowCount = excelSheet.getLastRowNum() - excelSheet.getFirstRowNum() + 2;
                System.out.println("rowCount " + rowCount);
                int startRow = 0;
                data = new Object[rowCount - startRow][totalCol];
                for (int i = startRow; i <= rowCount + 1; i++) {
                    try {
                        Row row = excelSheet.getRow(i);

                        for (int j = 0; j < row.getLastCellNum(); j++) {
//                            if (j == 12 || j == 19) {
//                                try {
//                                    data[i - startRow][j] = row.getCell(j).getDateCellValue();
//                                    continue;
//                                } catch (Exception ex) {
//                                }
//                            }
                            try {
                                data[i - startRow][j] = numToString(row.getCell(j).getNumericCellValue());
                                continue;
                            } catch (Exception ex) {
                            }

                            try {
                                data[i - startRow][j] = (row.getCell(j).getRichStringCellValue());
                                if (data[i - startRow][j].toString().length() == 0) {
                                    data[i - startRow][j] = "";
                                }
                                continue;
                            } catch (Exception e) {
                                data[i - startRow][j] = "";
                            }
                        }
                    } catch (Exception e) {
                    }

                }
            } catch (Exception e) {System.out.println(e);}
            inputStream.close();
        } catch (Exception e) {System.out.println(e);}
        return data;
    }

    public static String dateToString(Date date) {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }

    public static String numToString(double date) {

        DecimalFormat df = new DecimalFormat("#,####");
        String bbb = df.format(date);
        bbb = bbb.replaceAll(",", "");
        return bbb;
    }

}
