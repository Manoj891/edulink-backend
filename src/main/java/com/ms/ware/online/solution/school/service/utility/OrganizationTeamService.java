package com.ms.ware.online.solution.school.service.utility;

import org.springframework.http.ResponseEntity;
import com.ms.ware.online.solution.school.entity.utility.OrganizationTeam;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

public interface OrganizationTeamService {

    public ResponseEntity getAll();

    public ResponseEntity save(HttpServletRequest request, MultipartFile memberPhoto, OrganizationTeam obj);

    public ResponseEntity update(HttpServletRequest request, MultipartFile memberPhoto, OrganizationTeam obj, long id);

    public ResponseEntity delete(String id);

}