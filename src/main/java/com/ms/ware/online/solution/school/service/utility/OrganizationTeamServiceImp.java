/*    map = new com.fasterxml.jackson.databind.ObjectMapper().readValue(jsonData, new com.fasterxml.jackson.core.type.TypeReference<>() {});
 */
package com.ms.ware.online.solution.school.service.utility;


import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.utility.OrganizationTeamDao;
import com.ms.ware.online.solution.school.entity.utility.OrganizationTeam;
import com.ms.ware.online.solution.school.model.DatabaseName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Map;

@Service
public class OrganizationTeamServiceImp implements OrganizationTeamService {

    @Autowired
    private OrganizationTeamDao da;
    @Autowired
    private AuthenticationFacade facade;
    @Autowired
    private Message message;

    @Override
    public ResponseEntity getAll() {
        return ResponseEntity.status(200).body(da.getAll("from OrganizationTeam"));
    }

    @Override
    public ResponseEntity save(HttpServletRequest request, MultipartFile memberPhoto, OrganizationTeam obj) {

        AuthenticatedUser td = facade.getAuthentication();
        String msg = "", sql;
        try {
            try {
                sql = "SELECT ifnull(MAX(ID),0)+1 AS id FROM organization_team";
                message.map = (Map) da.getRecord(sql).get(0);
                obj.setId(Long.parseLong(message.map.get("id").toString()));
            } catch (Exception e) {
                return ResponseEntity.status(200).body(message.respondWithError("connection error or invalid table name"));
            }
            int row = da.save(obj);
            msg = da.getMsg();
            if (row > 0) {
                try {
                    if (memberPhoto.getSize() > 100) {
                        String location = message.getFilepath(DatabaseName.getDocumentUrl());
                        String filePath = DatabaseName.getDocumentUrl() + "Document/OrganizationTeam/";
                        File f = new File(location + filePath);
                        try {
                            if (!f.exists()) {
                                f.mkdirs();
                            }
                        } catch (Exception e) {
                            return ResponseEntity.status(200).body(message.respondWithError(e.getMessage()));
                        }

                        String fileName = filePath + obj.getId() + "-" + memberPhoto.getOriginalFilename();
                        f = new File(location + fileName);
                        try {
                            if (f.exists()) {
                                f.deleteOnExit();
                            }
                        } catch (Exception e) {
                        }
                        f = new File(location + fileName);
                        memberPhoto.transferTo(f);
                        sql = "UPDATE organization_team SET `PHOTO`='" + fileName + "' WHERE `ID`='" + obj.getId() + "'";
                        da.delete(sql);
                    }
                } catch (Exception e) {
                }
                return ResponseEntity.status(200).body(message.respondWithMessage("Success"));
            } else if (msg.contains("Duplicate entry")) {
                msg = "This record already exist";
            }
            return ResponseEntity.status(200).body(message.respondWithError(msg));

        } catch (Exception e) {
            return ResponseEntity.status(200).body(message.respondWithError(e.getMessage()));
        }
    }

    @Override
    public ResponseEntity update(HttpServletRequest request, MultipartFile memberPhoto, OrganizationTeam obj, long id) {

        AuthenticatedUser td = facade.getAuthentication();
        int row;
        String msg = "";
        obj.setId(id);
        row = da.update(obj);
        msg = da.getMsg();
        if (row > 0) {
            try {
                if (memberPhoto.getSize() > 100) {
                    String location = message.getFilepath(DatabaseName.getDocumentUrl());
                    String filePath = DatabaseName.getDocumentUrl() + "Document/OrganizationTeam/";
                    File f = new File(location + filePath);
                    try {
                        if (!f.exists()) {
                            f.mkdirs();
                        }
                    } catch (Exception e) {
                        return ResponseEntity.status(200).body(message.respondWithError(e.getMessage()));
                    }

                    String fileName = filePath + obj.getId() + "-" + memberPhoto.getOriginalFilename();
                    f = new File(location + fileName);
                    try {
                        if (f.exists()) {
                            f.deleteOnExit();
                        }
                    } catch (Exception ignored) {
                    }
                    f = new File(location + fileName);
                    memberPhoto.transferTo(f);
                    String sql = "UPDATE organization_team SET `PHOTO`='" + fileName + "' WHERE `ID`='" + obj.getId() + "'";
                    da.delete(sql);
                }
            } catch (Exception e) {
            }
            return ResponseEntity.status(200).body(message.respondWithMessage("Success"));
        } else if (msg.contains("Duplicate entry")) {
            msg = "This record already exist";
        } else if (msg.contains("foreign key")) {
            msg = "this record already used in reference tables, Cannot delete of this record";
        }
        return ResponseEntity.status(200).body(message.respondWithError(msg));
    }

    @Override
    public ResponseEntity delete(String id) {

        AuthenticatedUser td = facade.getAuthentication();
        int row;
        String msg = "", sql;
        sql = "DELETE FROM organization_team WHERE ID='" + id + "'";
        row = da.delete(sql);
        msg = da.getMsg();
        if (row > 0) {
            return ResponseEntity.status(200).body(message.respondWithMessage("Success"));
        } else if (msg.contains("foreign key")) {
            msg = "this record already used in reference tables, Cannot delete of this record";
        }
        return ResponseEntity.status(200).body(message.respondWithError(msg));
    }
}
