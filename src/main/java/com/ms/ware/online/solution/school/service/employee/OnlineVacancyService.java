package com.ms.ware.online.solution.school.service.employee;

import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.dao.employee.EmployeeInfoDao;
import com.ms.ware.online.solution.school.model.DatabaseName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Objects;
import java.util.UUID;

@Service
public class OnlineVacancyService {
    @Autowired
    private EmployeeInfoDao da;
    @Autowired
    private Message message;
    public Object uploadFiles(HttpServletRequest request, long id, MultipartFile photo, MultipartFile cv) {
        
        String location = message.getFilepath(DatabaseName.getDocumentUrl());
        String filePath = "/Vacancy/Files/";
        String sql, fileName;
        File f = new File(location + filePath);
        try {
            if (!f.exists()) {
                f.mkdirs();
            }
        } catch (Exception e) {
            return false;
        }

        try {
            if (photo.getSize() > 10) {
                fileName = filePath + id + "-photo" + UUID.randomUUID().toString() + ".png";
                f = new File(location + fileName);
                photo.transferTo(f);
                sql = "UPDATE online_vacancy set PHOTO ='" + fileName + "' where id=" + id;
                da.delete(sql);
            }
        } catch (Exception ignored) {
        }
        try {
            if (cv.getSize() > 10) {
                fileName = filePath + id + "-cv-" + Objects.requireNonNull(cv.getOriginalFilename()).replace(" ", "");
                f = new File(location + fileName);
                cv.transferTo(f);
                sql = "UPDATE online_vacancy set CV ='" + fileName + "' where id=" + id;
                da.delete(sql);
            }
        } catch (Exception ignored) {
        }
        return message.respondWithMessage("Success");
    }
}
