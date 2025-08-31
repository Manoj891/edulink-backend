/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.dao.utility;

import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.entity.utility.Routing;
import com.ms.ware.online.solution.school.model.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class RoutingDaoImpl implements RoutingDao {

    String msg;

    @Override
    public List<Routing> getAll(String hql) {

        msg = "";
        Session session = HibernateUtil.getSession();
        List<Routing> list = new ArrayList<>();
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
    public int save(Routing obj) {

        Session session = HibernateUtil.getSession();
        Transaction tr = session.beginTransaction();
        msg = "";
        int row = 1;
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
        } catch (HibernateException e) {
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
        } catch (Exception e) {
        }
        return row;
    }

    @Override
    public String getMsg() {
        return msg;
    }

}
