package com.ms.ware.online.solution.school.service.setup;

import com.ms.ware.online.solution.school.entity.setup.ClassMaster;

import java.util.List;
import java.util.Map;

public interface ClassMasterService {

    Object getAll();

    Object save(ClassMaster obj);

    Object update(ClassMaster obj, long id);

    Object delete(String id);

    Map<String,Object> getClassMaster(long id);
}