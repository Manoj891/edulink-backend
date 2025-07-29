package com.ms.ware.online.solution.school.service.billing;

import com.ms.ware.online.solution.school.config.DB;
import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.excel.ReadJetkingExcelData;
import com.ms.ware.online.solution.school.model.DatabaseName;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ReceiptUploadService {

    public Object doUpload(MultipartFile receipt, long academicYear, long fiscalYear) {
        AtomicInteger count = new AtomicInteger();
        int error = 0;
        ReadJetkingExcelData dd = new ReadJetkingExcelData();
        Message message = new Message();
        if (receipt.getSize() < 100) {
            return message.respondWithError("Please provide file");
        }

        File f;
        String location = message.getFilepath(DatabaseName.getDocumentUrl());
        f = new File(location + "/Organization");
        try {
            if (!f.exists()) {
                f.mkdirs();
            }
        } catch (Exception e) {
            return message.respondWithError(e.getMessage());
        }
        f = new File(location + "/Organization/receipt.xlsx");
        try {
            receipt.transferTo(f);
            Sheet sheet = new XSSFWorkbook(location + "/Organization/receipt.xlsx").getSheetAt(0);


            Integer billSn;
            String billNo;
            List<Map<Integer, Object>> list = new ArrayList<>();
            for (Row cells : sheet) {
                Map<Integer, Object> map = new HashMap<>();
                AtomicInteger jj = new AtomicInteger(0);
                cells.forEach(cell -> map.put(jj.getAndIncrement(), cell));
                list.add(map);
            }

            DB db = new DB();
            for (Map<Integer, Object> a : list) {
                try {
                    billSn = (int) Double.parseDouble(a.get(0).toString());
                    billNo = String.valueOf(billSn);
                    String date = a.get(1).toString().replace("/", "-");
                    String regNo = a.get(2).toString();
                    double amount = Double.parseDouble(a.get(8).toString());
                    long classId = dd.getClassID(a.get(4).toString());
                    try {
                        String sql = "insert into stu_billing_master(bill_no, academic_year, approve_by, approve_date, auto_generate, bill_amount, bill_sn,bill_type, class_id, create_at, enter_by, enter_date, fiscal_year, program, reg_no,remark, subject_gropu) value ('" + billNo + "','" + academicYear + "','SYSTEM',now(),'N'," + amount + "," + billSn + ",'DR'," + classId + ",now(),'SYSTEM','" + date + "'," + fiscalYear + ",1," + regNo + ",'Excel Import',1);" +
                                "\ninsert into stu_billing_detail(bill_no,bill_sn,academic_year,bill_id,class_id,cr,dr,is_extra,payment_date,program,reg_no) value ('" + billNo + "',1," + academicYear + ",0," + classId + "," + amount + ",0,'Y','" + date + "',1,'" + regNo + "');" +
                                "\ninsert into stu_billing_detail(bill_no,bill_sn,academic_year,bill_id,class_id,cr,dr,is_extra,payment_date,program,reg_no) value ('" + billNo + "',2," + academicYear + ",0," + classId + ",0," + amount + ",'Y','" + date + "',1,'" + regNo + "')";
                        db.save(sql);

                    } catch (Exception e) {
                        System.out.println();
                    }

                } catch (Exception ignored) {

                }
            }

            System.out.println(list.size());
        } catch (Exception ignored) {

        }

        return message.respondWithMessage(count.get() + " record saved. " + error + " record error");
    }
}
