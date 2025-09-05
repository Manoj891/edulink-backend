package com.ms.ware.online.solution.school.config;

import com.ms.ware.online.solution.school.model.HibernateUtil;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@Service
public class DB {
    @Autowired
    private HibernateUtil util;
    String msg;

    public int save(String sql) {
        Session session = util.getSession();
        Transaction tr = session.beginTransaction();
        int a=0;
        try {
            a = session.createSQLQuery(sql).executeUpdate();
            tr.commit();
            session.close();
            setMsg("Success");
        } catch (Exception e) {
            tr.rollback();
            session.close();
            System.out.println(e.getMessage());
//            throw new PersistenceException();
        }

        return a;
    }

    public int save(String sql, String[] parameterValue) {
        Session session = util.getSession();
        Transaction tr = session.beginTransaction();
        int a = 0;
        try {
            Query query = session.createSQLQuery(sql);
            for (int i = 0; i < parameterValue.length; i++) {
                query.setParameter(i, parameterValue[i]);
            }
            a = query.executeUpdate();
            session.close();
            setMsg("Success");
        } catch (Exception e) {
            tr.rollback();
            session.close();
            throw new PersistenceException();
        }

        return a;
    }

    public int save(String sql, String[] parameter, String[] parameterValue) {
        Session session = util.getSession();
        Transaction tr = session.beginTransaction();
        int a = 0;
        try {
            Query query = session.createSQLQuery(sql);
            for (int i = 0; i < parameter.length; i++) {
                query.setParameter(parameter[i], parameterValue[i]);
            }
            a = query.executeUpdate();
            tr.commit();
            session.close();
            setMsg("Success");
        } catch (Exception e) {
            tr.rollback();
            session.close();
            throw new PersistenceException();
        }

        return a;
    }

    public int delete(String sql) {
        int a = 0;
        Session session = util.getSession();
        Transaction tr = session.beginTransaction();
        try {
            a = session.createSQLQuery(sql).executeUpdate();
            session.close();
            setMsg("Success");
        } catch (Exception e) {
            tr.rollback();
            session.close();
            throw new PersistenceException();
        }

        return a;
    }

    public List<Map<String, Object>> getMapRecord(String sql) {
        Session session = util.getSession();
        try {
            List list = session.createSQLQuery(sql).setResultTransformer(org.hibernate.Criteria.ALIAS_TO_ENTITY_MAP).list();
            session.close();
            return list;
        } catch (Exception e) {
            session.close();
            throw new PersistenceException();
        }

    }

    public List<Map<String, Object>> getRecord(String sql) {
        Session session = util.getSession();
        try {
            List<Map<String, Object>> list = session.createSQLQuery(sql).setResultTransformer(org.hibernate.Criteria.ALIAS_TO_ENTITY_MAP).list();
            session.close();
            return list;
        } catch (Exception e) {
            session.close();
            throw new PersistenceException();
        }
    }

}
