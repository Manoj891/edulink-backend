/*    map = new com.fasterxml.jackson.databind.ObjectMapper().readValue(jsonData, new com.fasterxml.jackson.core.type.TypeReference<>() {});
 */
package com.ms.ware.online.solution.school.service.utility;


import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.utility.OrganizationMasterDao;
import com.ms.ware.online.solution.school.entity.utility.OrganizationMaster;
import com.ms.ware.online.solution.school.config.Message;

import java.io.File;
import javax.servlet.http.HttpServletRequest;

import com.ms.ware.online.solution.school.exception.CustomException;
import com.ms.ware.online.solution.school.model.DatabaseName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class OrganizationMasterServiceImp implements OrganizationMasterService {

    @Autowired
    OrganizationMasterDao da;
    Message message = new Message();
    String msg = "";
    int row;
    @Autowired
    private AuthenticationFacade facade;

    @Override
    public Object getAll() {
        return da.getAll("from OrganizationMaster");
    }

    @Override
    public Object save(OrganizationMaster obj) {
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        try {
            obj.setId(1l);
            row = da.save(obj);
            msg = da.getMsg();
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
        try {
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
                if (billBackground != null && billBackground.getSize() > 100) {
                    f = new File(location + "/Organization/bill-background.png");
                    billBackground.transferTo(f);
                }
            } catch (Exception ignored) {
            }
            try {
                if (idCardLogo != null && idCardLogo.getSize() > 100) {
                    f = new File(location + "/Organization/idCardLogo.png");
                    idCardLogo.transferTo(f);
                }
            } catch (Exception ignored) {
            }
            try {
                if (idCardLogo != null && idCardLogo.getSize() > 100) {
                    f = new File(location + "/Organization/principleSignature.png");
                    principleSignature.transferTo(f);
                }
            } catch (Exception ignored) {
            }
            try {
                if (idCardLogo != null && idCardLogo.getSize() > 100) {
                    f = new File(location + "/Organization/Logo.png");
                    logo.transferTo(f);
                }
            } catch (Exception ignored) {
            }
            return message.respondWithMessage("Success");
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
    }

}
