package com.ms.ware.online.solution.school.service.utility;

import com.ms.ware.online.solution.school.entity.utility.OrganizationMaster;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

public interface OrganizationMasterService {

    Object getAll();

    Object save(OrganizationMaster obj);

    String logo(HttpServletRequest request, MultipartFile logo, MultipartFile idCardLogo, MultipartFile principleSignature, MultipartFile billBackground);

}
