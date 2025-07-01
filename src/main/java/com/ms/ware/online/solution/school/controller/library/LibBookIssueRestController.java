package com.ms.ware.online.solution.school.controller.library;

import com.ms.ware.online.solution.school.dto.BookIssueReq;
import com.ms.ware.online.solution.school.service.library.LibBookIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/Library/BookIssue")
public class LibBookIssueRestController {

    @Autowired
    private LibBookIssueService service;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> index(@RequestParam String bookId) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getAll(bookId));
    }

    @GetMapping("/Return")
    public ResponseEntity<List<Map<String, Object>>> indexReturn(@RequestParam String bookId) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findBookReturn(bookId));
    }

    @PostMapping
    public ResponseEntity<String> doSave(@RequestBody BookIssueReq req) {
        return ResponseEntity.status(HttpStatus.OK).body(service.save(req));
    }

    @PutMapping
    public ResponseEntity<String> doUpdate(@RequestParam String receiveDate, @RequestBody List<String> bookIds) {
        return ResponseEntity.status(HttpStatus.OK).body(service.update(receiveDate, bookIds));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> doDelete(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.delete(id));
    }

    @PostMapping("BookIssueReport")
    public ResponseEntity<List<Map<String, Object>>> bookIssueReport() {
        return ResponseEntity.status(HttpStatus.OK).body(service.bookIssueReport());
    }

}
