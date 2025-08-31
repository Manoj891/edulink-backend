/*    map = new com.fasterxml.jackson.databind.ObjectMapper().readValue(jsonData, new com.fasterxml.jackson.core.type.TypeReference<>() {});
 */
package com.ms.ware.online.solution.school.service.inventory;

import com.ms.ware.online.solution.school.config.DateConverted;
import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.inventory.PurchaseOrderDao;
import com.ms.ware.online.solution.school.entity.inventory.PurchaseOrder;
import com.ms.ware.online.solution.school.entity.inventory.PurchaseOrderDetail;
import com.ms.ware.online.solution.school.entity.inventory.PurchaseOrderDetailPK;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class PurchaseOrderServiceImp implements PurchaseOrderService {
    @Autowired
    private AuthenticationFacade facade;
    @Autowired
    private PurchaseOrderDao da;
    @Autowired
    private Message message;
    String msg = "", sql;
    int row;

    @Override
    public Object getAll() {
        return da.getAll("from PurchaseOrder where approveDate is null and orderNo>0");
    }

    @Override
    public Object save(PurchaseOrder obj) {
        AuthenticatedUser td = facade.getAuthentication();
        ;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        List list;
        try {
            Date date = obj.getEnterDateAd();
            String enterDate = DateConverted.toString(date);
            sql = "SELECT ID id FROM fiscal_year WHERE '" + enterDate + "' BETWEEN START_DATE AND END_DATE;";
            list = da.getRecord(sql);
            if (list.isEmpty()) {
                msg = "Please define fiscal year of " + obj.getEnterDate();
                return message.respondWithError(msg);
            }
            Map map = (Map) list.get(0);
            long fiscalYear = Long.parseLong(map.get("id").toString());

            sql = "SELECT ifnull(MAX(ORDER_SN),0)+1 AS orderSn FROM purchase_order WHERE FISCAL_YEAR='" + fiscalYear + "'";
            message.map = (Map) da.getRecord(sql).get(0);
            int orderSn = Integer.parseInt(message.map.get("orderSn").toString());
            long orderNo;
            if (orderSn < 10) {
                orderNo = Long.parseLong(fiscalYear + "0000" + orderSn);
            } else if (orderSn < 100) {
                orderNo = Long.parseLong(fiscalYear + "000" + orderSn);
            } else if (orderSn < 1000) {
                orderNo = Long.parseLong(fiscalYear + "00" + orderSn);
            } else if (orderSn < 10000) {
                orderNo = Long.parseLong(fiscalYear + "0" + orderSn);
            } else {
                orderNo = Long.parseLong(fiscalYear + "" + orderSn);
            }
            obj.setOrderNo(orderNo);
            obj.setOrderSn(orderSn);
            obj.setFiscalYear(fiscalYear);
            obj.setEnterBy(td.getUserName());
            obj.setOpening("N");
            List<PurchaseOrderDetail> detail = obj.getDetail();
            for (int i = 0; i < detail.size(); i++) {
                detail.get(i).setPk(new PurchaseOrderDetailPK(orderNo, detail.get(i).getAcCode().getAcCode()));
                detail.get(i).setTotal(detail.get(i).getOrderQty() * detail.get(i).getRate());
            }
            obj.setDetail(detail);
            row = da.save(obj);
            msg = da.getMsg();
            if (row > 0) {
                return message.respondWithMessage("Success");
            } else if (msg.contains("Duplicate entry")) {
                msg = "This record already exist";
            }
            return message.respondWithError(msg);

        } catch (Exception e) {
            return message.respondWithError(e.getMessage());
        }
    }

    @Override
    public Object update(String jsonData) {
        try {
            AuthenticatedUser td = facade.getAuthentication();
            ;
            if (!td.isStatus()) {
                return message.respondWithError("invalid token");
            }
            String jsonDataArray[] = message.jsonDataToStringArray(jsonData);
            try {
                row = 0;
                message.map = new com.fasterxml.jackson.databind.ObjectMapper().readValue(jsonDataArray[0], new com.fasterxml.jackson.core.type.TypeReference<>() {
                });
                String date = DateConverted.bsToAd(message.map.get("date").toString());
                String userName = td.getUserName();
                message.list = new com.fasterxml.jackson.databind.ObjectMapper().readValue(jsonDataArray[1], new com.fasterxml.jackson.core.type.TypeReference<List>() {
                });
                for (int i = 0; i < message.list.size(); i++) {
                    sql = "UPDATE purchase_order SET APPROVE_DATE='" + date + "',APPROVE_BY='" + userName + "',`STATUS`='A' WHERE ORDER_NO='" + message.list.get(i) + "'";
                    row += da.delete(sql);
                    msg = da.getMsg();
                }
            } catch (Exception ex) {
                msg = ex.getMessage();
            }
        } catch (Exception ex) {
            msg = ex.getMessage();
        }
        msg = da.getMsg();
        if (row > 0) {
            return message.respondWithMessage("Success");
        } else if (msg.contains("Duplicate entry")) {
            msg = "This record already exist";
        } else if (msg.contains("foreign key")) {
            msg = "this record already used in reference tables, Cannot delete of this record";
        }
        return message.respondWithError(msg);
    }

    @Override
    public Object delete(String id) {
        AuthenticatedUser td = facade.getAuthentication();
        ;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }

        id = "'" + id.replace(",", "','") + "'";
        sql = "DELETE FROM purchase_order WHERE ID IN (" + id + ")";
        row = da.delete(sql);
        msg = da.getMsg();
        if (row > 0) {
            return message.respondWithMessage("Success");
        } else if (msg.contains("foreign key")) {
            msg = "this record already used in reference tables, Cannot delete of this record";
        }
        return message.respondWithError(msg);
    }

    @Override
    public Object getAll(String orderNo) {
        List list = da.getAll("from PurchaseOrder where orderNo='" + orderNo + "'");
        if (list.isEmpty()) {
            return message.respondWithError("Invalid Order No");
        }
        return list.get(0);
    }

    @Override
    public Object getAllReport(Long sundryCreditors, String dateFrom, String dateTo) {
        dateFrom = DateConverted.bsToAd(dateFrom);
        dateTo = DateConverted.bsToAd(dateTo);
        return da.getAll("from PurchaseOrder where orderNo>0 and approveDate is not null and supplier=ifnull(" + sundryCreditors + ",supplier) and enterDate between '" + dateFrom + "' and '" + dateTo + "'");
    }

    @Override
    public Object opening(PurchaseOrder obj) {

        AuthenticatedUser td = facade.getAuthentication();
        ;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        Map map;
        List list;
        try {

            sql = "SELECT IFNULL(INSTALLATION_DATE,'')  installationDate FROM organization_master ";
            list = da.getRecord(sql);
            if (list.isEmpty()) {
                msg = "Please define installation Date organization master";
                return message.respondWithError(msg);
            }
            map = (Map) list.get(0);
            String enterDate = map.get("installationDate").toString();
            if (enterDate.length() < 10) {
                return message.respondWithError("Please define installation Date organization master");
            }
            sql = "SELECT ID id FROM fiscal_year WHERE '" + enterDate + "' BETWEEN START_DATE AND END_DATE;";
            list = da.getRecord(sql);
            if (list.isEmpty()) {
                msg = "Please define fiscal year of " + obj.getEnterDate();
                return message.respondWithError(msg);
            }
            map = (Map) list.get(0);
            long fiscalYear = Long.parseLong(map.get("id").toString());
            int orderSn = 0;
            long orderNo;
            if (orderSn < 10) {
                orderNo = Long.parseLong(fiscalYear + "0000" + orderSn);
            } else if (orderSn < 100) {
                orderNo = Long.parseLong(fiscalYear + "000" + orderSn);
            } else if (orderSn < 1000) {
                orderNo = Long.parseLong(fiscalYear + "00" + orderSn);
            } else if (orderSn < 10000) {
                orderNo = Long.parseLong(fiscalYear + "0" + orderSn);
            } else {
                orderNo = Long.parseLong(fiscalYear + "" + orderSn);
            }
            obj.setEnterDateAd(DateConverted.toDate(enterDate));
            obj.setWithinDateAd(obj.getEnterDateAd());
            obj.setSupplier(0l);
            obj.setOrderNo(orderNo);
            obj.setOrderSn(orderSn);
            obj.setFiscalYear(fiscalYear);
            obj.setEnterBy(td.getUserName());
            obj.setApproveBy(td.getUserName());
            obj.setApproveDate(obj.getEnterDateAd());
            obj.setStatus("Y");
            obj.setOpening("Y");
            List<PurchaseOrderDetail> detail = obj.getDetail();
            obj.setDetail(null);
            row = da.save(obj);
            msg = da.getMsg();
            if (row > 0) {

                for (int i = 0; i < detail.size(); i++) {
                    detail.get(i).setPk(new PurchaseOrderDetailPK(orderNo, detail.get(i).getAcCode().getAcCode()));
                    detail.get(i).setTotal(detail.get(i).getOrderQty() * detail.get(i).getRate());
                    da.save(detail.get(i));
                }
            }
            if (row > 0) {
                return message.respondWithMessage("Success");
            }
            msg = "This record already exist";
            return message.respondWithError(msg);

        } catch (Exception e) {
            return message.respondWithError(e.getMessage());
        }

    }

    @Override
    public Object doReject(String jsonData) {

        try {
            AuthenticatedUser td = facade.getAuthentication();
            ;
            if (!td.isStatus()) {
                return message.respondWithError("invalid token");
            }
            String jsonDataArray[] = message.jsonDataToStringArray(jsonData);
            try {
                row = 0;
                message.map = new com.fasterxml.jackson.databind.ObjectMapper().readValue(jsonDataArray[0], new com.fasterxml.jackson.core.type.TypeReference<>() {
                });

                message.list = new com.fasterxml.jackson.databind.ObjectMapper().readValue(jsonDataArray[1], new com.fasterxml.jackson.core.type.TypeReference<List>() {
                });
                for (int i = 0; i < message.list.size(); i++) {
                    sql = "DELETE FROM purchase_order_detail WHERE ORDER_NO='" + message.list.get(i) + "'";
                    da.delete(sql);
                    sql = "DELETE FROM purchase_order WHERE ORDER_NO='" + message.list.get(i) + "'";
                    row += da.delete(sql);
                    msg = da.getMsg();
                }
            } catch (IOException ex) {
                msg = ex.getMessage();
            }
        } catch (Exception ex) {
            msg = ex.getMessage();
        }
        msg = da.getMsg();
        if (row > 0) {
            return message.respondWithMessage("Success");
        } else if (msg.contains("Duplicate entry")) {
            msg = "This record already exist";
        } else if (msg.contains("foreign key")) {
            msg = "this record already used in reference tables, Cannot delete of this record";
        }
        return message.respondWithError(msg);
    }
}
