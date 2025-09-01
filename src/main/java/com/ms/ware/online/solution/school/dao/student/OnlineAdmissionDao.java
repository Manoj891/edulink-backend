/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.dao.student;

import com.ms.ware.online.solution.school.entity.student.OnlineAdmission;
import com.ms.ware.online.solution.school.model.HibernateUtil;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

@Service
public class OnlineAdmissionDao {
    String msg = "";

    @Autowired
    private HibernateUtil util;
    public int save(OnlineAdmission obj) {
        int row = 1;
        Session session = util.getSession();
        Transaction tr = session.beginTransaction();
        msg = "";
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


    public List<OnlineAdmission> getAll(String hql) {
        msg = "";
        Session session = util.getSession();
        List<OnlineAdmission> list = new ArrayList<>();
        Transaction tr = session.beginTransaction();
        try {
            list = session.createQuery(hql).list();
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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
