package com.ms.ware.online.solution.school.service.student;

import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.student.StudentInfoDao;
import com.ms.ware.online.solution.school.dao.student.StudentInfoDaoImp;
import com.ms.ware.online.solution.school.entity.student.StudentHomework;
import com.ms.ware.online.solution.school.entity.student.StudentHomeworkPK;
import com.ms.ware.online.solution.school.model.DatabaseName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

@Service
public class HomeworkService {
    @Autowired
    private AuthenticationFacade facade;

    public Object homeworkSubmit(HttpServletRequest request, MultipartFile answerFile, MultipartFile answerFile1, MultipartFile answerFile2, MultipartFile answerFile3, MultipartFile answerFile4, MultipartFile answerFile5, StudentHomework obj) {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }

        String location = message.getFilepath(DatabaseName.getDocumentUrl());
        StudentInfoDao da = new StudentInfoDaoImp();
        int row;
        String msg;
        long homeworkId = obj.getHomework();
        String studentId = td.getUserId();
        try {
            if (saveFile(answerFile, homeworkId, location, studentId, 1)) {
                obj.setStuFile(getFileName());
            }
            if (saveFile(answerFile1, homeworkId, location, studentId, 2)) {
                obj.setStuFile1(getFileName());
            }
            if (saveFile(answerFile2, homeworkId, location, studentId, 3)) {
                obj.setStuFile2(getFileName());
            }
            if (saveFile(answerFile3, homeworkId, location, studentId, 4)) {
                obj.setStuFile3(getFileName());
            }
            if (saveFile(answerFile4, homeworkId, location, studentId, 5)) {
                obj.setStuFile4(getFileName());
            }
            if (saveFile(answerFile5, homeworkId, location, studentId, 6)) {
                obj.setStuFile5(getFileName());
            }

            obj.setPk(new StudentHomeworkPK(obj.getStuId(), obj.getHomework()));
            row = da.save(obj);
            msg = da.getMsg();
            if (row > 0) {
                return message.respondWithMessage("Success");
            } else if (msg.contains("Duplicate entry")) {
                msg = "This record already exist";
            }
            return message.respondWithError(msg);

        } catch (Exception e) {
            return message.respondWithError(e.getMessage());
        }

    }

    boolean saveFile(MultipartFile answerFile, Long homewworkId, String location, String studentId, long fileNo) {
        try {
            if (answerFile.getSize() > 100) {
                String filePath = "/Homework/" + homewworkId;
                File f = new File(location + filePath);
                try {
                    if (!f.exists()) {
                        f.mkdirs();
                    }
                } catch (Exception e) {
                    return false;
                }

                String fileName = filePath + "/Answer_" + studentId + "_" + fileNo + ".png";
                fileName = fileName.replace(" ", "");
                f = new File(location + fileName);
                try {
                    if (f.exists()) {
                        f.deleteOnExit();
                    }
                } catch (Exception e) {
                }
                f = new File(location + fileName);
                answerFile.transferTo(f);
                setFileName(fileName);
                return true;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    String fileName = "";

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
