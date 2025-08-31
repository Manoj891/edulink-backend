package com.ms.ware.online.solution.school.dao.exam;
import javax.validation.ConstraintViolationException;
import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.entity.exam.ExamSchedule;
import com.ms.ware.online.solution.school.model.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExamScheduleDaoImpl implements ExamScheduleDao {
    private String msg = "";

    @Override
    public List<ExamSchedule> getAll(String hql) {
        msg = "";
        Session session = HibernateUtil.getSession();
        List<ExamSchedule> list = new ArrayList<>();
        Transaction tr = session.beginTransaction();
        try {
            list = session.createQuery(hql).list();
            tr.commit();
        } catch (HibernateException e) {
            msg = Message.exceptionMsg(e);
            tr.rollback();
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
        Session session = HibernateUtil.getSession();
        Transaction tr = session.beginTransaction();
        msg = "";
        row = 1;
        try {
            session.saveOrUpdate(obj);
            tr.commit();
        } catch (Exception e) {
            tr.rollback();
            msg = Message.exceptionMsg(e);
            row = 0;
        }
        try {
            session.close();
        } catch (HibernateException ignored) {
        }
        return row;

    }

    @Override
    public int update(ExamSchedule obj) {

        Session session = HibernateUtil.getSession();
        Transaction tr = session.beginTransaction();
        int row = 1;
        msg = "";
        try {
            session.update(obj);
            tr.commit();
        } catch (ConstraintViolationException e) {
            tr.rollback();
            msg = Message.exceptionMsg(e);
        }
        try {
            session.close();
        } catch (HibernateException ignored) {
        }
        return row;
    }

    @Override
    public int delete(String sql) {
        Session session = HibernateUtil.getSession();
        Transaction tr = session.beginTransaction();
        msg = "";
        int row = 0;
        try {
            row = session.createSQLQuery(sql).executeUpdate();
            tr.commit();
        } catch (ConstraintViolationException e) {
            tr.rollback();
            msg = Message.exceptionMsg(e);
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
