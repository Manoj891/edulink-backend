/*    map = new com.fasterxml.jackson.databind.ObjectMapper().readValue(jsonData, new com.fasterxml.jackson.core.type.TypeReference<>() {});
 */
package com.ms.ware.online.solution.school.service.utility;

import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.utility.AdBsCalenderDao;
import com.ms.ware.online.solution.school.entity.utility.AdBsCalender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdBsCalenderServiceImp implements AdBsCalenderService {

    @Autowired
    private AdBsCalenderDao da;
    @Autowired
    private Message message;
    String msg = "";
    int row;
    @Autowired
    private AuthenticationFacade facade;

    @Override
    public Object getAll(String yearMonth) {
        return da.getAll("from AdBsCalender where bsDate like '" + yearMonth + "%'");
    }

    @Override
    public Object update(AdBsCalender obj) {
        AuthenticatedUser td = facade.getAuthentication();
        ;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        row = da.update(obj);
        msg = da.getMsg();
        if (row > 0) {
            return message.respondWithMessage("Success");
        } else if (msg.contains("Duplicate entry")) {
            msg = "This record already exist";
        } else if (msg.contains("foreign key")) {
            msg = "this record already used in reference tables, Cannot delete of this record";
        }
        return message.respondWithError(msg);
    }

}
