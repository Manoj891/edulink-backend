package com.ms.ware.online.solution.school.dao.inventory;

import com.ms.ware.online.solution.school.entity.inventory.SundryCreditors;
import com.ms.ware.online.solution.school.model.HibernateUtil;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import  javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;


@Service
public class SundryCreditorsDaoImp implements SundryCreditorsDao {

    String msg = "";
    int row = 1;
    @Autowired
    private HibernateUtil util;

    @Override
    public List<SundryCreditors> getAll(String hql) {
        msg = "";
        Session session = util.getSession();
        List<SundryCreditors> list = new ArrayList<>();
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
        } catch (HibernateException ignored) {
        }
        return list;
    }

    @Override
    public int save(SundryCreditors obj) {
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
        } catch (HibernateException ignored) {
        }
        return row;
    }

    @Override
    public int update(SundryCreditors obj) {
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
        } catch (HibernateException ignored) {
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
        } catch (HibernateException ignored) {
        }
        return list;
    }
    @Override
    public String getMsg() {
        return msg;
    }

}
