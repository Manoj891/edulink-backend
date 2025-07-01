package com.ms.ware.online.solution.school.controller.utility;

import com.ms.ware.online.solution.school.service.configure.ImportJetkingServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("api/utility/jenking-data")
public class JenkingDataImportController {
    @Autowired
    private ImportJetkingServiceImpl importJetking;

    @GetMapping
    public Object importedRecord() {
        return importJetking.importedRecord();
    }

    @PostMapping
    public Object importStudentData(@RequestParam String effectDate, @RequestParam MultipartFile excelFile, @RequestParam long academicYear) {
        return importJetking.importStudentData(effectDate, excelFile, academicYear);
    }
}
