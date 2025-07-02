package com.ms.ware.online.solution.school.controller;

import com.ms.ware.online.solution.school.service.billing.ReceiptUploadService;
import com.ms.ware.online.solution.school.entity.student.StudentHomework;
import com.ms.ware.online.solution.school.service.billing.OpeningUploadService;
import com.ms.ware.online.solution.school.service.employee.EmployeeInfoService;
import com.ms.ware.online.solution.school.service.employee.OnlineVacancyService;
import com.ms.ware.online.solution.school.service.student.HomeworkService;
import com.ms.ware.online.solution.school.service.student.StudentInfoService;
import com.ms.ware.online.solution.school.service.utility.OrganizationMasterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/multipart/")
public class MultipartFileUploadController {

    private final OpeningUploadService openingUploadService;
    private final OrganizationMasterService organizationMasterService;
    private final StudentInfoService studentInfoService;
    private final HomeworkService homeworkService;
    private final EmployeeInfoService employeeInfoService;
    private final ReceiptUploadService receiptUploadService;
    private final OnlineVacancyService onlineVacancyService;

    @PostMapping("/opening")
    public String markUpload(@RequestParam MultipartFile opening) throws IOException {
        return openingUploadService.markUpload(opening);
    }

    @PostMapping("/upload-logo")
    public ResponseEntity<String> doLogo( @RequestParam(required = false) MultipartFile logo, @RequestParam(required = false) MultipartFile billBackground, @RequestParam(required = false) MultipartFile idCardLogo, @RequestParam(required = false) MultipartFile principleSignature) {
        return ResponseEntity.status(HttpStatus.OK).body(organizationMasterService.logo(logo, idCardLogo, principleSignature, billBackground));
    }

    @PostMapping("/student/photo")
    public Object doSavePhoto(HttpServletRequest request, @RequestParam long studentId, @RequestParam(required = false) MultipartFile photo, @RequestParam(required = false) MultipartFile certificate1, @RequestParam(required = false) MultipartFile certificate2) {
        return studentInfoService.save(request, studentId, photo, certificate1, certificate2);
    }

    @PostMapping("student/homework")
    public Object homeworkSubmit(HttpServletRequest request, @RequestParam(required = false) MultipartFile answerFile, @RequestParam(required = false) MultipartFile answerFile1, @RequestParam(required = false) MultipartFile answerFile2, @RequestParam(required = false) MultipartFile answerFile3, @RequestParam(required = false) MultipartFile answerFile4, @RequestParam(required = false) MultipartFile answerFile5, @ModelAttribute StudentHomework obj) {
        return homeworkService.homeworkSubmit(request, answerFile, answerFile1, answerFile2, answerFile3, answerFile4, answerFile5, obj);
    }

    @PostMapping("employee/photo")
    public String doSavePhoto(@RequestParam Long empId, @RequestParam MultipartFile photo) {
        employeeInfoService.doSavePhoto(empId, photo);
        return "{\"message\":\"Success\"}";
    }

    @PostMapping("/billing/receipt-upload")
    public Object doUpload(HttpServletRequest request, @RequestParam MultipartFile receipt, @RequestParam long academicYear, @RequestParam long fiscalYear) throws IOException {
        return receiptUploadService.doUpload(request, receipt, academicYear, fiscalYear);
    }

    @PutMapping("/online/vacancy/{id}")
    public Object uploadFiles(HttpServletRequest request, @PathVariable Long id, @RequestParam(required = false) MultipartFile photo, @RequestParam(required = false) MultipartFile cv) {
        return onlineVacancyService.uploadFiles(request, id, photo, cv);
    }
}
