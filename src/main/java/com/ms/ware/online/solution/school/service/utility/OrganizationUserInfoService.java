package com.ms.ware.online.solution.school.service.utility;

import com.ms.ware.online.solution.school.entity.utility.OrganizationUserInfo;
import javax.servlet.http.HttpServletRequest;

public interface OrganizationUserInfoService {

    public Object getAll();

    public Object save(OrganizationUserInfo obj, HttpServletRequest request);

    public Object update(OrganizationUserInfo obj, long id);

    public Object delete(String id);

    public Object resetPassword(long id, HttpServletRequest request);

}
