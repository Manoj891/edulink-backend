/*    map = new com.fasterxml.jackson.databind.ObjectMapper().readValue(jsonData, new com.fasterxml.jackson.core.type.TypeReference<>() {});
 */
package com.ms.ware.online.solution.school.service.utility;

import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.utility.AboutAppDao;
import com.ms.ware.online.solution.school.entity.utility.AboutApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AboutAppServiceImp implements AboutAppService {
    @Autowired
    private AuthenticationFacade facade;
    @Autowired
    private AboutAppDao da;
    @Autowired
    private Message message;

    @Override
    public ResponseEntity getAll() {
        try {
            return ResponseEntity.status(200).body(da.getAll("from AboutApp").get(0));
        } catch (Exception e) {
        }

        return ResponseEntity.status(200).body(message.respondWithError("Record Not Found!!"));
    }

    @Override
    public ResponseEntity save(AboutApp obj) {

        AuthenticatedUser td = facade.getAuthentication();
        String msg = "";
        try {
            obj.setId(1l);
            int row = da.save(obj);
            msg = da.getMsg();
            if (row > 0) {
                return ResponseEntity.status(200).body(message.respondWithMessage("Success"));
            } else if (msg.contains("Duplicate entry")) {
                msg = "This record already exist";
            }
            return ResponseEntity.status(200).body(message.respondWithError(msg));

        } catch (Exception e) {
            return ResponseEntity.status(200).body(message.respondWithError(e.getMessage()));
        }
    }

}
