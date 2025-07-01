package com.ms.ware.online.solution.school.service.teacherpanel;

import org.springframework.http.ResponseEntity;
import com.ms.ware.online.solution.school.entity.teacherpanel.TeachersHomework;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

public interface TeachersHomeworkService {

    public ResponseEntity getAll(Long academicYear, Long program, Long classId, Long subjectGroup);

    public Object save(HttpServletRequest request, TeachersHomework obj, MultipartFile photo);

    public Object update(HttpServletRequest request, TeachersHomework obj, long id, MultipartFile photo);

    public ResponseEntity delete(String id);

}
