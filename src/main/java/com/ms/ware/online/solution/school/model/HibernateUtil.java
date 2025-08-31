package com.ms.ware.online.solution.school.model;

import org.hibernate.Session;

public interface HibernateUtil {
    void init();
    Session getSession();
}
