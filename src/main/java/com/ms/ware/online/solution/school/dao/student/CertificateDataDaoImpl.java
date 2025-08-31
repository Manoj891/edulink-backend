package com.ms.ware.online.solution.school.dao.student;
import javax.validation.ConstraintViolationException;
import com.ms.ware.online.solution.school.model.HibernateUtil;
import com.ms.ware.online.solution.school.entity.student.CertificateData;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;
import com.ms.ware.online.solution.school.config.Message;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CertificateDataDaoImpl implements CertificateDataDao {

    private String msg;
    private int row = 1;

    @Override
    public List<CertificateData> getAll(String hql) {
        Session session = HibernateUtil.getSession();
        List<CertificateData> list = new ArrayList<>();
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
    public int save(CertificateData obj) {

        Session session = HibernateUtil.getSession();
        Transaction tr = session.beginTransaction();

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
        } catch (HibernateException ignored) {
        }
        return row;
    }

    @Override
    public int update(CertificateData obj) {

        Session session = HibernateUtil.getSession();
        Transaction tr = session.beginTransaction();
        row = 1;

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
        } catch (Exception ignored) {
        }
        return row;
    }

    @Override
    public List<Map<String,Object>> getRecord(String sql) {

        Session session = HibernateUtil.getSession();
        Transaction tr = session.beginTransaction();
        List<Map<String,Object>> list = new ArrayList<>();
        try {
            list = session.createSQLQuery(sql).setResultTransformer(org.hibernate.Criteria.ALIAS_TO_ENTITY_MAP).list();
            tr.commit();
        } catch (HibernateException e) {
            tr.rollback();
            msg = Message.exceptionMsg(e);
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
