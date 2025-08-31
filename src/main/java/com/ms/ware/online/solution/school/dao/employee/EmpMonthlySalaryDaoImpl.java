/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.dao.employee;

import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.entity.employee.EmpMonthlySalary;
import com.ms.ware.online.solution.school.model.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import  javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Manoj
 */
@Component
public class EmpMonthlySalaryDaoImpl implements EmpMonthlySalaryDao {

    String msg = "";
    int row = 1;

    @Override
    public List<EmpMonthlySalary> getAll(String hql) {
        msg = "";
        Session session = HibernateUtil.getSession();
        List<EmpMonthlySalary> list = new ArrayList<>();
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
    public int save(EmpMonthlySalary obj) {
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
        } catch (HibernateException e) {
        }
        return row;
    }

    @Override
    public int save(List<EmpMonthlySalary> list) {
        Session session = HibernateUtil.getSession();
        Transaction tr = session.beginTransaction();
        msg = "";
        row = 0;
        try {
            list.forEach(obj -> {
                session.saveOrUpdate(obj);
                row++;
            });
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
