package com.ms.ware.online.solution.school.service.utility;

import org.springframework.http.ResponseEntity;
import com.ms.ware.online.solution.school.entity.utility.NoticeBoard;

public interface NoticeBoardService {

    public ResponseEntity getAll();

    public ResponseEntity save(NoticeBoard obj);

    public ResponseEntity update(NoticeBoard obj,long id);

    public ResponseEntity delete(String id);

}