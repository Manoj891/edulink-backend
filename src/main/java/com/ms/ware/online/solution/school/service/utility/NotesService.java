package com.ms.ware.online.solution.school.service.utility;

import com.ms.ware.online.solution.school.entity.utility.Notes;

public interface NotesService {

    Object getAll();

    Object save(Notes obj);

    Object update(Notes obj, long id);

    Object delete(String id);

}