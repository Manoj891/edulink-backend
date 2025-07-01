package com.ms.ware.online.solution.school.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import com.ms.ware.online.solution.school.model.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

@Setter
@Getter
@Service
public class DB {

    String msg;

    public int save(String sql) {
        Session session = HibernateUtil.getSession();
        Transaction tr = session.beginTransaction();
        int a = 0;
        try {
            a = session.createSQLQuery(sql).executeUpdate();
            tr.commit();
            setMsg("Success");
        } catch (Exception e) {
            setMsg(Message.exceptionMsg(e));
            tr.rollback();
        }
        try {
            session.close();
        } catch (Exception ignored) {
        }
        return a;
    }

    public int save(String sql, String[] parameterValue) {
        Session session = HibernateUtil.getSession();
        Transaction tr = session.beginTransaction();
        int a = 0;
        try {
            Query query = session.createSQLQuery(sql);
            for (int i = 0; i < parameterValue.length; i++) {
                query.setParameter(i, parameterValue[i]);
            }
            a = query.executeUpdate();
            tr.commit();
            setMsg("Success");
        } catch (Exception e) {
            setMsg(Message.exceptionMsg(e));
            tr.rollback();
        }
        try {
            session.close();
        } catch (Exception e) {
        }
        return a;
    }

    public int save(String sql, String[] parameter, String[] parameterValue) {
        Session session = HibernateUtil.getSession();
        Transaction tr = session.beginTransaction();
        int a = 0;
        try {
            Query query = session.createSQLQuery(sql);
            for (int i = 0; i < parameter.length; i++) {
                query.setParameter(parameter[i], parameterValue[i]);
            }
            a = query.executeUpdate();
            tr.commit();
            setMsg("Success");
        } catch (Exception e) {
            setMsg(Message.exceptionMsg(e));
            tr.rollback();
        }
        try {
            session.close();
        } catch (Exception e) {
        }
        return a;
    }

    public int delete(String sql) {
        int a = 0;
        Session session = HibernateUtil.getSession();
        Transaction tr = session.beginTransaction();
        try {
            a = session.createSQLQuery(sql).executeUpdate();
            tr.commit();
            setMsg("Success");
        } catch (Exception e) {
            setMsg(Message.exceptionMsg(e));
            tr.rollback();
        }
        try {
            session.close();
        } catch (Exception e) {
        }
        return a;
    }

    public List<Map<String, Object>> getMapRecord(String sql) {
        Session session = HibernateUtil.getSession();
        try {
            List list = session.createSQLQuery(sql).setResultTransformer(org.hibernate.Criteria.ALIAS_TO_ENTITY_MAP).list();
            session.close();
            return list;
        } catch (Exception e) {
            msg = Message.exceptionMsg(e);
            System.out.println(msg);
        }
        try {
            session.close();
        } catch (Exception e) {
        }
        return null;
    }

    public List<Map<String, Object>> getRecord(String sql) {
        Session session = HibernateUtil.getSession();
        try {
            List<Map<String, Object>> list = session.createSQLQuery(sql).setResultTransformer(org.hibernate.Criteria.ALIAS_TO_ENTITY_MAP).list();
            session.close();
            return list;
        } catch (Exception e) {
            msg = Message.exceptionMsg(e);
            System.out.println(msg);
        }
        try {
            session.close();
        } catch (Exception e) {
        }
        return new ArrayList<>();
    }

}
