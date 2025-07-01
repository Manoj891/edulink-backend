
package com.ms.ware.online.solution.school.dao.library;

import java.util.List;
import java.util.Map;

import com.ms.ware.online.solution.school.entity.library.BookRemoved;
import com.ms.ware.online.solution.school.entity.library.LibBookStock;

public interface LibBookStockDao {

    List<LibBookStock> getAll(String hql);

    int save(LibBookStock obj);

    int save(BookRemoved obj);

    int update(LibBookStock obj);

    int delete(String sql);

    List<Map<String, Object>> getRecord(String sql);

    String getMsg();
}
