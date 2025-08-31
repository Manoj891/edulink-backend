package com.ms.ware.online.solution.school.dao.exam;
import  javax.persistence.PersistenceException;
import java.util.List;
import java.util.ArrayList;

import com.ms.ware.online.solution.school.model.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.Transaction;
import com.ms.ware.online.solution.school.entity.exam.ExamResultPublish;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class ExamResultPublishDaoImp implements ExamResultPublishDao {
    @Autowired
    private HibernateUtil util;
    String msg = "";
    int row = 1;


    @Override
    public List<ExamResultPublish> getAll(String hql) {
        msg = "";
        Session session = util.getSession();
        List<ExamResultPublish> list = new ArrayList<>();
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
    public int save(ExamResultPublish obj) {
        Session session = util.getSession();
        Transaction tr = session.beginTransaction();
        msg = "";
        row = 1;
        try {
            session.save(obj);
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
    public int update(ExamResultPublish obj) {
        Session session = util.getSession();
        Transaction tr = session.beginTransaction();
        row = 1;
        msg = "";
        try {
            session.update(obj);
            tr.commit();
        } catch (PersistenceException e) {
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
