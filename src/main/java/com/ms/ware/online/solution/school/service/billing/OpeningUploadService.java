package com.ms.ware.online.solution.school.service.billing;

import com.ms.ware.online.solution.school.config.DateConverted;
import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.billing.StuBillingMasterDao;
import com.ms.ware.online.solution.school.entity.billing.StuBillingDetail;
import com.ms.ware.online.solution.school.entity.billing.StuBillingDetailPK;
import com.ms.ware.online.solution.school.entity.billing.StuBillingMaster;
import com.ms.ware.online.solution.school.exception.CustomException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class OpeningUploadService {
    @Autowired
    private AuthenticationFacade facade;
    @Autowired
    private StuBillingMasterDao dao;


    public String markUpload(@RequestParam MultipartFile opening) throws IOException {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();;
        String username = td.getUserName();

        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        } else if (!td.getUserType().equals("ADM")) {
            return message.respondWithError("invalid token");
        }
        List<Map<String, Object>> fy = dao.getRecord("select id from fiscal_year where '" + DateConverted.today() + "' between START_DATE and END_DATE");
        if (fy.isEmpty()) throw new CustomException("Fiscal Year not defined");
        long fiscalYear = Long.parseLong(fy.get(0).get("id").toString());
        String location = "/home/tomcat/webapps/ROOT";
        File f = new File(location + "/billing/");
        try {
            if (!f.exists()) {
                f.mkdirs();
            }
        } catch (Exception e) {
            return message.respondWithError(e.getMessage());
        }
        f = new File(location + "/billing/opening-bill.xlsx");
        opening.transferTo(f);
        Workbook excel = new XSSFWorkbook(Files.newInputStream(f.toPath()));
        Date date = new Date();
        Long id;
        double amount;

        long classId, program, subjectGroup, academicYear;
        String firstName, lastName;

        List<StuBillingDetail> details;
        List<StuBillingMaster> data = new ArrayList<>();
        StringBuilder error = new StringBuilder();
        for (Row row : excel.getSheetAt(0)) {
            amount = 0;
            try {
                try {
                    id = Long.parseLong(row.getCell(0).getStringCellValue());

                } catch (Exception e) {
                    try {
                        id = (long) row.getCell(0).getNumericCellValue();
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                        continue;
                    }
                }
                firstName = row.getCell(1).getStringCellValue();
                lastName = row.getCell(2).getStringCellValue();
//                className = row.getCell(3).getStringCellValue();
                try {
                    amount = row.getCell(4).getNumericCellValue();
                } catch (Exception e) {
                    amount = Double.parseDouble(row.getCell(4).getStringCellValue());
                }
            } catch (Exception e) {
                continue;
            }
            if (amount == 0) continue;
            int count = 1;
            try {
                List<Map<String, Object>> list = dao.getRecord("select class_id,program,subject_group,academic_year from student_info where id=" + id);
                if (list.size() == 1) {
                    String billNo = "OPN-" + id + "-" + count;

                    details = new ArrayList<>();

                    classId = Long.parseLong(list.get(0).get("class_id").toString());
                    program = Long.parseLong(list.get(0).get("program").toString());
                    subjectGroup = Long.parseLong(list.get(0).get("subject_group").toString());
                    academicYear = Long.parseLong(list.get(0).get("academic_year").toString());
                    details.add(StuBillingDetail.builder().
                            regNo(id).
                            academicYear(academicYear).
                            program(program).
                            classId(classId).
                            billId(0).
                            dr(0).
                            cr(amount).
                            paymentDate(new Date()).
                            isExtra("N")
                            .pk(StuBillingDetailPK.builder().billNo(billNo).billSn(-100).build()).build());
                    data.add(StuBillingMaster.builder().
                            billNo(billNo).
                            billSn(id.intValue()).
                            program(program).
                            classId(classId).
                            academicYear(academicYear).
                            fiscalYear(fiscalYear).
                            subjectGropu(subjectGroup).
                            billType("OPN").
                            regNo(id).
                            studentName(firstName + " " + lastName).
                            enterBy(username).
                            enterDate(date).
                            billAmount(amount).
                            autoGenerate("N").
                            remark("OPENING").detail(details).
                            createAt(date).
                            build());
                    count += 1;
                } else {
                    error.append(id).append(" Not found in  student list").append(",");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }


        data.forEach(d -> {
            List<StuBillingDetail> list = d.getDetail();
            d.setDetail(new ArrayList<>());
            dao.save(d);
            dao.save(list.get(0));
        });
        if (error.length() > 2) {
            throw new CustomException(error.toString());
        }
        return message.respondWithMessage(data.size() + " Record saved");
    }
}
