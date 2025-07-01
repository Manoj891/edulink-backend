
package com.ms.ware.online.solution.school.controller.utility;

import com.ms.ware.online.solution.school.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import com.ms.ware.online.solution.school.model.DatabaseName;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/api/utility/backup")
public class BackupRestController {


    @GetMapping
    public List<String> backup() {
        List<String> file = new ArrayList<>();
        String database = DatabaseName.getDatabase() + ".sql";
        for (String day : Objects.requireNonNull(new File("/home/mysql/backup/").list())) {
            for (String filename : Objects.requireNonNull(new File("/home/mysql/backup/" + day).list())) {
                if (filename.endsWith(database)) {
                    file.add(day + "/" + filename);
                }
            }
        }
        return file;
    }

    @PostMapping
    public Map<String, String> backup(@RequestBody String fileName) {
        File file = new File("/home/mysql/backup/" + fileName);
        String filename = UUID.randomUUID().toString();
        String password = filename.substring(filename.lastIndexOf("-") + 1);
        zip("/home/mysql/backup/" + fileName, "/home/tomcat/webapps/ROOT/" + filename + ".zip", password);
        Map<String, String> map = new HashMap<>();
        map.put("filename", filename + ".zip");
        map.put("password", password);
        file.delete();
        return map;
    }

    private void zip(String sql, String zip, String zipPassword) {
        try {
            ZipParameters zipParameters = new ZipParameters();
            zipParameters.setEncryptFiles(true);
            zipParameters.setCompressionLevel(CompressionLevel.HIGHER);
            zipParameters.setEncryptionMethod(EncryptionMethod.AES);
            ZipFile zipFile = new ZipFile(zip, zipPassword.toCharArray());
            zipFile.addFile(sql, zipParameters);
            zipFile.close();
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
    }


    public void unzipBackup(String zip, String password, String filePath) {
        try {
            ZipParameters zipParameters = new ZipParameters();
            zipParameters.setEncryptFiles(true);
            zipParameters.setCompressionLevel(CompressionLevel.HIGHER);
            zipParameters.setEncryptionMethod(EncryptionMethod.AES);
            ZipFile zipFile = new ZipFile(zip, password.toCharArray());
            zipFile.extractAll(filePath);
            log.info("Unzip successfully");
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
    }

}
