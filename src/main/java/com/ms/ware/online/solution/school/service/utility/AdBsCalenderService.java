package com.ms.ware.online.solution.school.service.utility;

import com.ms.ware.online.solution.school.entity.utility.AdBsCalender;


public interface AdBsCalenderService {

    Object getAll(String yearMonth);


    Object update(AdBsCalender obj);


}