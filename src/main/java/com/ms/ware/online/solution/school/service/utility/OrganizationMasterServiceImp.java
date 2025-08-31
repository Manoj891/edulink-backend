/*    map = new com.fasterxml.jackson.databind.ObjectMapper().readValue(jsonData, new com.fasterxml.jackson.core.type.TypeReference<>() {});
 */
package com.ms.ware.online.solution.school.service.utility;


import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.utility.OrganizationMasterDao;
import com.ms.ware.online.solution.school.entity.utility.OrganizationMaster;
import com.ms.ware.online.solution.school.model.DatabaseName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
@Slf4j
public class OrganizationMasterServiceImp implements OrganizationMasterService {

    @Autowired
    private OrganizationMasterDao da;
    @Autowired
    private Message message;
    int row;
    @Autowired
    private AuthenticationFacade facade;

    @Override
    public Object getAll() {
        return da.getAll("from OrganizationMaster");
    }

    @Override
    public Object save(OrganizationMaster obj) {
        AuthenticatedUser td = facade.getAuthentication();;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        try {
            obj.setId(1l);
            row = da.save(obj);
            String msg = da.getMsg();
            if (row > 0) {
                return message.respondWithMessage("Success");
            } else if (msg.contains("Duplicate entry")) {
                da.update(obj);
                msg = "This record already exist";
            }
            return message.respondWithError(msg);

        } catch (Exception e) {
            return message.respondWithError(e.getMessage());
        }
    }

    @Override
    public String logo(MultipartFile logo, MultipartFile idCardLogo, MultipartFile principleSignature, MultipartFile billBackground) {


        String location = message.getFilepath(DatabaseName.getDocumentUrl());
        File f = new File(location + "/Organization");
        try {
            if (!f.exists()) {
                f.mkdirs();
            }
        } catch (Exception e) {
            return message.respondWithError(e.getMessage());
        }
        try {
            if (billBackground != null && billBackground.getSize() > 1) {
                f = new File(location + "/Organization/bill-background.png");
                billBackground.transferTo(f);
                log.info("Bill background image has been uploaded");
            }
        } catch (Exception ex) {
            return message.respondWithError(ex.getMessage());
        }
        try {
            if (idCardLogo != null && idCardLogo.getSize() > 1) {
                f = new File(location + "/Organization/idCardLogo.png");
                idCardLogo.transferTo(f);
                log.info("ID Card logo image has been uploaded");
            }
        } catch (Exception ex) {
            return message.respondWithError(ex.getMessage());
        }
        try {
            if (principleSignature != null && principleSignature.getSize() > 1) {
                principleSignature.transferTo(new File(location + "/Organization/principleSignature.png"));
                log.info("Principle signature image has been uploaded");
            }
        } catch (Exception ex) {
            return message.respondWithError(ex.getMessage());
        }
        try {
            if (logo != null && logo.getSize() > 1) {
                f = new File(location + "/Organization/Logo.png");
                logo.transferTo(f);
                log.info("Logo image has been uploaded");
            }
        } catch (Exception ex) {
            return message.respondWithError(ex.getMessage());
        }
        return message.respondWithMessage("Success");

    }

}
