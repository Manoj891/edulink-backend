package com.ms.ware.online.solution.school.service.library;

import com.ms.ware.online.solution.school.dto.BookIssueReq;

import java.util.List;
import java.util.Map;


public interface LibBookIssueService {

    List<Map<String, Object>> getAll(String bookId);

    String save(BookIssueReq req);

    String update(String receiveDate, List<String> bookIds);

    String delete(String id);

    List<Map<String, Object>> findBookReturn(String bookId);

    List<Map<String, Object>> bookIssueReport();
}
