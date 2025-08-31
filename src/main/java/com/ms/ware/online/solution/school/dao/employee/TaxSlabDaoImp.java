package com.ms.ware.online.solution.school.dao.employee;

import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.entity.employee.TaxSlab;
import com.ms.ware.online.solution.school.model.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;


@Component
public class TaxSlabDaoImp implements TaxSlabDao {

    String msg = "";
    int row = 1;


    @Override
    public List<TaxSlab> getAll(String hql) {
        msg = "";
        Session session = HibernateUtil.getSession();
        List<TaxSlab> list = new ArrayList<>();
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
        } catch (HibernateException e) {
        }
        return list;
    }

    @Override
    public int save(TaxSlab obj) {
        Session session = HibernateUtil.getSession();
        Transaction tr = session.beginTransaction();
        msg = "";
        row = 1;
        try {
            session.save(obj);
            tr.commit();
        } catch (Exception e) {
            tr.rollback();
            msg = Message.exceptionMsg(e);
            row = 0;
        }
        try {
            session.close();
        } catch (HibernateException e) {
        }
        return row;
    }

    @Override
    public int update(TaxSlab obj) {
        Session session = HibernateUtil.getSession();
        Transaction tr = session.beginTransaction();
        row = 1;
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
        } catch (ConstraintViolationException e) {
            tr.rollback();
            msg = Message.exceptionMsg(e);
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
        Session session = HibernateUtil.getSession();
Transaction tr = session.beginTransaction();
        List list = new ArrayList();
        try {
            list = session.createSQLQuery(sql).setResultTransformer(org.hibernate.Criteria.ALIAS_TO_ENTITY_MAP).list();
         tr.commit();
} catch (HibernateException e) {
       tr.rollback();
      msg = Message.exceptionMsg(e);
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
