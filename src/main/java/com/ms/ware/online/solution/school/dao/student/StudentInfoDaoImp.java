package com.ms.ware.online.solution.school.dao.student;

import com.ms.ware.online.solution.school.entity.billing.StuBillingMaster;
import com.ms.ware.online.solution.school.entity.student.*;
import com.ms.ware.online.solution.school.model.HibernateUtil;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import  javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class StudentInfoDaoImp implements StudentInfoDao {

    String msg = "";
    int row = 1;
    @Autowired
    private HibernateUtil util;
    @Override
    public List<StudentInfo> getAll(String hql) {
        msg = "";
        Session session = util.getSession();
        List<StudentInfo> list = new ArrayList<>();
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
    public List<StudentImport> getStudentImport(String hql) {
        msg = "";
        Session session = util.getSession();
        List<StudentImport> list = new ArrayList<>();
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
    public int save(StudentInfo obj) {
        Session session = util.getSession();
        Transaction tr = session.beginTransaction();
        msg = "";
        row = 1;
        try {
            session.save(obj);
            System.out.println(obj.getId() + " Saved.");
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
    public int save(StudentImport obj) {
        Session session = util.getSession();
        Transaction tr = session.beginTransaction();
        msg = "";
        row = 1;
        try {
            session.save(obj);
            System.out.println(obj.getId() + " Saved.");
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
    public int update(StudentImport obj) {

        Session session = util.getSession();
        Transaction tr = session.beginTransaction();
        msg = "";
        row = 1;
        try {
            session.update(obj);
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
    public int update(StudentInfo obj) {
        Session session = util.getSession();
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
        Session session = util.getSession();
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
        Session session = util.getSession();
        Transaction tr = session.beginTransaction();
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            list = session.createSQLQuery(sql)
                    .setFetchSize(5000)
                    .setResultTransformer(org.hibernate.Criteria.ALIAS_TO_ENTITY_MAP).list();
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

    @Override
    public int save(ClassTransfer obj) {

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
        } catch (HibernateException e) {
        }
        return row;
    }

    @Override
    public int save(StuBillingMaster obj) {

        Session session = util.getSession();
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
    public int save(StudentHomework obj) {

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
        } catch (HibernateException e) {
        }
        return row;

    }

    @Override
    public int save(Annex4bMaster obj) {

        Session session = util.getSession();
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

}
