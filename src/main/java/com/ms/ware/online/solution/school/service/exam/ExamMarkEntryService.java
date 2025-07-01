package com.ms.ware.online.solution.school.service.exam;



import org.springframework.web.multipart.MultipartFile;
import com.ms.ware.online.solution.school.dto.CashSystem;
import com.ms.ware.online.solution.school.dto.ExamMarkEntryReq;
import com.ms.ware.online.solution.school.dto.GpaWise;
import com.ms.ware.online.solution.school.dto.SubjectWise;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

public interface ExamMarkEntryService {

    Object getAll(Long exam, Long program, Long classId, Long subjectGroup, Long subject, String section, String order);

    Object save(ExamMarkEntryReq req);

    Object entryTeacher(ExamMarkEntryReq req);

    Object doApprove(String jsonData);

    Object delete(String id);

    Object getApprove(Long exam, Long program, Long classId, Long subjectGroup, Long subject);

    Object getReport(int system, Long exam, Long program, Long classId, Long subjectGroup, Long regNo, String section,String result);

    Object getFinalReport(int system, Long exam, Long program, Long classId, Long subjectGroup, Long regNo);


    Object markUpdate(Long exam, Long regNo);

    Object markUpdate(List<MarkUpdateReq> req);

    Object markUpload(HttpServletRequest request, MultipartFile markUpload, Long exam) throws IOException;

    Object markUpload(Long exam);

    Object markUpdateConfig(Long exam);

    Object markApprove(Long exam);

    CashSystem cashSystem(Long exam, Long program, Long classId, Long subjectGroup, Long regNo, String section);

    List<GpaWise> gpaWiseReport(Integer system, Long exam, Long program, Long subjectGroup);

    List<SubjectWise> subjectWiseReport(Integer system, Long exam, Long program, Long classId, Long subjectGroup);
}