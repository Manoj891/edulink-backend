/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.dao.utility;

import com.ms.ware.online.solution.school.entity.utility.Routing;
import java.util.List;

/**
 *
 * @author MS
 */
public interface RoutingDao {

    public List<Routing> getAll(String hql);

    public int save(Routing obj);

    public int delete(String sql);

    public String getMsg();
}
