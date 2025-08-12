package com.ms.ware.online.solution.school.dao.billing;

import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.entity.account.Voucher;
import com.ms.ware.online.solution.school.entity.billing.BillingDeleteMaster;
import com.ms.ware.online.solution.school.entity.billing.StuBillingDetail;
import com.ms.ware.online.solution.school.entity.billing.StuBillingMaster;
import com.ms.ware.online.solution.school.model.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class StuBillingMasterDaoImp implements StuBillingMasterDao {

    String msg = "";
    int row = 1;

    @Override
    public List<StuBillingMaster> getAll(String hql) {
        msg = "";
        Session session = HibernateUtil.getSession();
        List<StuBillingMaster> list = new ArrayList<>();
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
    public int save(StuBillingMaster obj) {
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
            System.out.println(msg);
        }
        try {
            session.close();
        } catch (HibernateException ignored) {
        }

        return row;
    }

    @Override
    public int update(StuBillingMaster obj) {
        Session session = HibernateUtil.getSession();
        Transaction tr = session.beginTransaction();
        row = 1;
        msg = "";
        try {
            session.update(obj);
            tr.commit();
        } catch (HibernateException e) {
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
        row = 0;
        try {
            row = session.createSQLQuery(sql).executeUpdate();
            tr.commit();
        } catch (Exception e) {
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
    public List<Map<String, Object>> getRecord(String sql) {
        msg = "";
        Session session = HibernateUtil.getSession();
        Transaction tr = session.beginTransaction();
        List<Map<String, Object>> list = new ArrayList();
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

    @Override
    public int save(StuBillingDetail obj) {

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
    public int delete(StuBillingMaster obj) {

        Session session = HibernateUtil.getSession();
        Transaction tr = session.beginTransaction();
        row = 1;
        msg = "";
        try {
            session.delete(obj);
            tr.commit();
        } catch (HibernateException e) {
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
    public int save(BillingDeleteMaster obj, String voucherNo) {
        String billNo = obj.getBillNo();
        Session session = HibernateUtil.getSession();
        Transaction tr = session.beginTransaction();
        msg = "";
        row = 1;
        try {
            session.save(obj);
            session.createSQLQuery("delete from voucher_detail where voucher_no='" + voucherNo + "';" +
                    "update voucher set fee_receipt_no=null where voucher_no='" + voucherNo + "';" +
                    "delete from voucher where voucher_no='" + voucherNo + "';" +
                    "delete from stu_billing_detail where bill_no='" + billNo + "';" +
                    "delete from stu_billing_master where bill_no='" + billNo + "';").executeUpdate();

            tr.commit();
        } catch (Exception e) {
            tr.rollback();
            msg = Message.exceptionMsg(e);
            row = 0;
        }
        try {
            session.close();
            session.flush();
            session.clear();
        } catch (HibernateException e) {
        }

        return row;
    }

    @Override
    public List<Voucher> getVoucher(String hql) {

        msg = "";
        Session session = HibernateUtil.getSession();
        List<Voucher> list = new ArrayList<>();
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
    public int delete(Voucher obj) {

        Session session = HibernateUtil.getSession();
        Transaction tr = session.beginTransaction();
        row = 1;
        msg = "";
        try {
            session.delete(obj);
            tr.commit();
        } catch (HibernateException e) {
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

}
