package com.ms.ware.online.solution.school.dao.student;

import com.ms.ware.online.solution.school.entity.student.PreviousEducation;
import com.ms.ware.online.solution.school.model.HibernateUtil;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import  javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;


@Component
public class PreviousEducationDaoImp implements PreviousEducationDao {

    String msg = "";
    int row = 1;
    @Autowired
    private HibernateUtil util;

    @Override
    public List<PreviousEducation> getAll(String hql) {
        msg = "";
        Session session = util.getSession();
        List<PreviousEducation> list = new ArrayList<>();
        Transaction tr = session.beginTransaction();
        try {
            list = session.createQuery(hql).list();
            tr.commit();
        } catch (HibernateException e) {
              session.close();
                    tr.rollback(); session.close();throw new PersistenceException();
     
     
        }
        try {
            session.close();
        } catch (HibernateException e) {
        }
        return list;
    }

    @Override
    public int save(PreviousEducation obj) {
        Session session = util.getSession();
        Transaction tr = session.beginTransaction();
        msg = "";
        row = 1;
        try {
            session.saveOrUpdate(obj);
            tr.commit();
        } catch (Exception e) {
            tr.rollback();
              session.close();
            throw new PersistenceException();
     
        }
        try {
            session.close();
        } catch (HibernateException e) {
        }
        return row;
    }


    @Override
    public int delete(String sql) {
        Session session = util.getSession();
        Transaction tr = session.beginTransaction();
        msg = "";
        row = 0;
        try {
            row = session.createSQLQuery(sql).executeUpdate();
            tr.commit();
        } catch (PersistenceException e) {
            tr.rollback();
              session.close();
            throw new PersistenceException();
        }
        try {
            session.close();
        } catch (Exception e) {
        }
        return row;
    }

    @Override
    public List getRecord(String sql) {
        msg = "";
        Session session = util.getSession();
Transaction tr = session.beginTransaction();
        List list = new ArrayList();
        try {
            list = session.createSQLQuery(sql).setResultTransformer(org.hibernate.Criteria.ALIAS_TO_ENTITY_MAP).list();
         tr.commit();
} catch (HibernateException e) {
       tr.rollback();
        session.close();
            throw new PersistenceException();
        }
        try {
            session.close();
        } catch (HibernateException e) {
        }
        return list;
    }
    @Override
    public String getMsg() {
        return msg;
    }

}
