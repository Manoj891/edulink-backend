package com.ms.ware.online.solution.school.controller.library;

import com.ms.ware.online.solution.school.dto.LibBookStockRes;
import com.ms.ware.online.solution.school.service.library.LibBookStockService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("api/Library/BookStock")
public class LibBookStockRestController {

    @Autowired
    private LibBookStockService service;

    @GetMapping("total-stock")
    public ResponseEntity<List<Map<String, Object>>> bookStock() {
        return ResponseEntity.status(HttpStatus.OK).body(service.bookStock());
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> index(@RequestParam(defaultValue = "") String bookId, @RequestParam(defaultValue = "") String bookName, @RequestParam(defaultValue = "") String dateFrom, @RequestParam(defaultValue = "") String dateTo, @RequestParam(required = false) Long program, @RequestParam(required = false) Long classId, @RequestParam(required = false) Long subject) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getAll(bookId, dateFrom, dateTo, program, classId, subject, bookName));
    }

    @GetMapping("/Report")
    public ResponseEntity<List<Map<String, Object>>> bookReport(@RequestParam String dateFrom, @RequestParam String dateTo) {
        return ResponseEntity.status(HttpStatus.OK).body(service.bookReport(dateFrom, dateTo));
    }

    @GetMapping("/Edit")
    public Object indexEdit(@RequestParam String purchaseId) {
        return service.indexEdit(purchaseId);
    }

    @GetMapping("/{bookId}")
    public ResponseEntity index(@PathVariable String bookId) {
        return service.getAll(bookId);
    }

    @PostMapping
    public ResponseEntity<String> doSave(@RequestBody LibBookStockRes obj) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(service.save(obj));
    }

    @PutMapping
    public ResponseEntity<String> doUpdate(@RequestBody LibBookStockRes obj) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(service.update(obj));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> doDelete(@PathVariable long id) {
        service.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("{\"message\":\"Successfully deleted\"}");
    }
}
