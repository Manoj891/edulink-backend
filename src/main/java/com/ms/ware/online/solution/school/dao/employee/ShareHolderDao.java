/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.dao.employee;

import com.ms.ware.online.solution.school.entity.employee.ShareHolder;
import java.util.List;

/**
 *
 * @author DELL VOSTRO 3400
 */
public interface ShareHolderDao {

    public List<ShareHolder> getAll(String hql);

    public int save(ShareHolder obj);

    public int update(ShareHolder obj);

    public int delete(String sql);

    public String getMsg();
}
