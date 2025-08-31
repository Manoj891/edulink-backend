package com.ms.ware.online.solution.school.controller.utility;

import com.ms.ware.online.solution.school.config.AESUtils;
import com.ms.ware.online.solution.school.config.DateConverted;
import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.config.SmsService;
import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.setup.BillMasterDao;
import com.ms.ware.online.solution.school.entity.utility.SmsConfiguration;
import com.ms.ware.online.solution.school.model.DatabaseName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("api/utility/SmsConfiguration")
public class SmsConfigurationController {
    @Autowired
    private BillMasterDao da;
    @Autowired
    private AuthenticationFacade facade;
    @Autowired
    private SmsService service;

    @PostMapping
    public Object doSave(HttpServletRequest request, @RequestBody SmsConfiguration obj) throws IOException {
        AuthenticatedUser td = facade.getAuthentication();;
        if (!td.getUserId().equalsIgnoreCase("1")) {
            return new Message().respondWithMessage("Permission Denied");
        }
        String url = request.getRequestURL().toString();
        String uri = request.getRequestURI();
        String host = (url.substring(0, url.indexOf(uri)));
        obj.setId(1);
        obj.setRequestIp(AESUtils.encrypt(host));
        obj.setToken(AESUtils.encrypt(obj.getToken()));
        obj.setContext(AESUtils.encrypt(DatabaseName.getDocumentUrl()));
        obj.setCreatedBy(td.getUserName());
        obj.setCreatedDate(DateConverted.now());
        da.save(obj);
        service.setConfigured(obj.getToken());
        return new Message().respondWithMessage("Success");
    }

}
