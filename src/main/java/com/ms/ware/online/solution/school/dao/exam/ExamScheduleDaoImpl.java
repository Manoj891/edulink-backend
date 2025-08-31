package com.ms.ware.online.solution.school.dao.exam;
import  javax.persistence.PersistenceException;

import com.ms.ware.online.solution.school.entity.exam.ExamSchedule;
import com.ms.ware.online.solution.school.model.HibernateUtil;
import com.ms.ware.online.solution.school.model.HibernateUtilImpl;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExamScheduleDaoImpl implements ExamScheduleDao {
    private String msg = "";
    @Autowired
    private HibernateUtil util;
    @Override
    public List<ExamSchedule> getAll(String hql) {
        msg = "";
        Session session = util.getSession();
        List<ExamSchedule> list = new ArrayList<>();
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
        } catch (HibernateException ignored) {
        }
        return list;
    }

    @Override
    public int save(ExamSchedule obj) {

        int row = 1;
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
        } catch (HibernateException ignored) {
        }
        return row;

    }

    @Override
    public int update(ExamSchedule obj) {

        Session session = util.getSession();
        Transaction tr = session.beginTransaction();
        int row = 1;
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
        int row = 0;
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
        } catch (Exception ignored) {
        }
        return row;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
