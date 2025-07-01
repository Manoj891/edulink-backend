package com.ms.ware.online.solution.school.service.library;

import com.ms.ware.online.solution.school.dto.LibBookStockRes;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface LibBookStockService {

    List<Map<String, Object>> getAll(String bookId, String dateFrom, String dateTo, Long program, Long classId, Long subject, String bookName);

    ResponseEntity getAll(String bookId);

    String save(LibBookStockRes obj);

    String update(LibBookStockRes obj);

    void delete(long id);

    Object indexEdit(String purchaseId);

    List<Map<String, Object>>  bookReport(String dateFrom, String dateTo);

    List<Map<String, Object>> bookStock();
}
