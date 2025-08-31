package com.ms.ware.online.solution.school.dao.setup;

import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.entity.setup.AcademicYear;
import com.ms.ware.online.solution.school.entity.setup.Section;
import com.ms.ware.online.solution.school.model.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import  javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Component
public class AcademicYearDaoImp implements AcademicYearDao {

    String msg = "";
    int row = 1;

    @Override
    public List<AcademicYear> getAll(String hql) {
        msg = "";
        Session session = HibernateUtil.getSession();
        List<AcademicYear> list = new ArrayList<>();
        Transaction tr = session.beginTransaction();
        try {
            list = session.createQuery(hql).list();
            tr.commit();
        } catch (HibernateException e) {
                     tr.rollback();session.close();
            throw new PersistenceException();
     
        }
        try {
            session.close();
        } catch (HibernateException e) {
        }
        return list;
    }

    @Override
    public int save(AcademicYear obj) {
        Session session = HibernateUtil.getSession();
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
    public int save(Section obj) {

        Session session = HibernateUtil.getSession();
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
        } catch (HibernateException ignored) {
        }
        return row;
    }

    @Override
    public int update(AcademicYear obj) {
        Session session = HibernateUtil.getSession();
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
        Session session = HibernateUtil.getSession();
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
    public List<Map<String, Object>> getRecord(String sql) {
        msg = "";
        Session session = HibernateUtil.getSession();
        Transaction tr = session.beginTransaction();
        List list = new ArrayList<>();
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
