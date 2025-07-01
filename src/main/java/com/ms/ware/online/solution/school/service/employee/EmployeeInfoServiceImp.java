/*    map = new com.fasterxml.jackson.databind.ObjectMapper().readValue(jsonData, new com.fasterxml.jackson.core.type.TypeReference<>() {});
 */
package com.ms.ware.online.solution.school.service.employee;


import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.employee.EmployeeInfoDao;
import com.ms.ware.online.solution.school.entity.account.ChartOfAccount;
import com.ms.ware.online.solution.school.entity.employee.EmployeeInfo;
import com.ms.ware.online.solution.school.exception.CustomException;
import com.ms.ware.online.solution.school.model.DatabaseName;
import com.ms.ware.online.solution.school.service.account.ChartOfAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeInfoServiceImp implements EmployeeInfoService {

    @Autowired
    private EmployeeInfoDao da;
    @Autowired
    private ChartOfAccountService service;
    @Autowired
    private AuthenticationFacade facade;

    @Override
    public List<EmployeeInfo> getAll() {
        return da.getAll("from EmployeeInfo");
    }

    @Override
    public void save(EmployeeInfo obj) {
        String msg = "", sql;
        try {
            if (obj.getPanNo().isEmpty()) {
                obj.setPanNo(null);
            }
            if (obj.getCode().isEmpty()) obj.setCode(null);
            sql = "SELECT IFNULL(SUNDRY_CREDITORS,'') creditors FROM organization_master";
            Map<String, Object> map = da.getRecord(sql).get(0);
            String mgrCode = map.get("creditors").toString();
            if (mgrCode.length() < 3) {
                throw new CustomException("Please define SUNDRY CREDITORS");
            }
            sql = "SELECT ifnull(MAX(ID),0)+1 AS id FROM employee_info";
            map = da.getRecord(sql).get(0);
            obj.setId(Long.parseLong(map.get("id").toString()));
            int row = da.save(obj);
            msg = da.getMsg();
            if (row > 0) {
                return;
            } else if (msg.contains("Duplicate entry")) {
                msg = "This record already exist";
            }
            throw new CustomException(msg);

        } catch (Exception e) {
            throw new CustomException(msg);
        }
    }

    @Override
    public void update(EmployeeInfo obj, long id) {
        int row;
        if (obj.getPanNo().isEmpty()) {
            obj.setPanNo(null);
        }
        if (obj.getCode().isEmpty()) obj.setCode(null);
        String msg, sql = "SELECT ifnull(ac_code,'') acCode FROM employee_info where id=" + id;
        Map<String, Object> map = da.getRecord(sql).get(0);
        String acCode = map.get("acCode").toString();
        obj.setId(id);
        try {
            if (obj.getPanNo().isEmpty()) {
                obj.setPanNo(null);
            }
        } catch (Exception e) {
            obj.setPanNo(null);
        }
        String acName = obj.getFirstName() + " " + obj.getMiddleName() + " " + obj.getLastName() + " [" + obj.getCode() + "]";

        if (acCode.length() > 3) {
            obj.setAcCode(acCode);
            updateChartOfAccount(acCode, acName);
        } else {
            sql = "SELECT IFNULL(SUNDRY_CREDITORS,'') creditors FROM organization_master";
            map = da.getRecord(sql).get(0);
            String mgrCode = map.get("creditors").toString();
            if (mgrCode.length() < 3) {
                throw new CustomException("Please define SUNDRY CREDITORS");
            }
            sql = "SELECT AC_CODE acCode FROM chart_of_account where AC_NAME='" + acName + "' AND MGR_CODE='" + mgrCode + "'";
            List<Map<String, Object>> list = da.getRecord(sql);
            if (!list.isEmpty()) {
                map = list.get(0);
                acCode = map.get("acCode").toString();
            } else {
                acCode = saveChartOfAccount(mgrCode, acName);
            }
            obj.setAcCode(acCode);
        }
        row = da.update(obj);
        msg = da.getMsg();
        if (row > 0) {
            return;
        } else if (msg.contains("Duplicate entry")) {
            msg = "This record already exist";
        } else if (msg.contains("foreign key")) {
            msg = "this record already used in reference tables, Cannot delete of this record";
        }
        throw new CustomException(msg);
    }

    @Override
    public void delete(String id) {
        int row;
        String msg, sql, oldEmail;
        sql = "select email from employee_info where id=" + id;
        Map<String, Object> map = da.getRecord(sql).get(0);
        oldEmail = map.get("email").toString();
        sql = "delete from employee_info where id='" + id + "'";
        da.delete(sql);
        sql = "delete from employee_info where email='" + oldEmail + "'";
        row = da.delete(sql);
        msg = da.getMsg();
        if (row > 0) {
            return;
        } else if (msg.contains("foreign key")) {
            msg = "this record already used in reference tables, Cannot delete of this record";
        }
        throw new CustomException(msg);
    }

    @Override
    public void doDeviceIdMap(long id, Long biometricCompanyId, Long biometricEmpId) {
        int row;
        String sql = "UPDATE employee_info SET BIOMETRIC_COMPANY_ID='" + biometricCompanyId + "',BIOMETRIC_EMP_ID='" + biometricEmpId + "' WHERE id='" + id + "';", msg;
        row = da.delete(sql);
        msg = da.getMsg();
        if (row > 0) {
            return;
        } else if (msg.contains("foreign key")) {
            msg = "this record already used in reference tables, Cannot delete of this record";
        }
        throw new CustomException(msg);
    }


    @Override
    public void doSavePhoto(Long empId, MultipartFile photo) {
        File f;
        Message message = new Message();
        String location = message.getFilepath(DatabaseName.getDocumentUrl());

        try {
            if (photo.getSize() < 100) {
                throw new CustomException("Please provide file");
            }

            f = new File(location + "/Employee/");
            try {
                if (!f.exists()) {
                    f.mkdirs();
                }
            } catch (Exception e) {
                throw new CustomException(e.getMessage());
            }
            String fileName = "/Employee/EMPLOYEE-ID-" + empId + ".png";
            f = new File(location + fileName);
            photo.transferTo(f);
            String sql = "UPDATE employee_info SET PHOTO='" + DatabaseName.getDocumentUrl() + "Document" + fileName + "' WHERE ID='" + empId + "'";
                    da.delete(sql);

        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
    }

    String saveChartOfAccount(String mgrCode, String acName) {
        ChartOfAccount chart = new ChartOfAccount();
        chart.setMgrCode(mgrCode);
        chart.setAcName(acName);
        chart.setTransact("Y");
        Map m = (Map) service.save(chart);
        return m.get("data").toString();

    }

    void updateChartOfAccount(String acCode, String acName) {
        ChartOfAccount chart = new ChartOfAccount();
        chart.setAcName(acName);
        chart.setAcCode(acCode);
        chart.setTransact("Y");
        service.update(chart);
    }

    @Override
    public List<Map<String, Object>> findTeacher() {
        String sql = "SELECT id,CONCAT(first_name,' ',middle_name,' ',last_name) as name,mobile mobileNo FROM employee_info where EMP_TYPE ='Teaching'";
        return da.getRecord(sql);
    }
}
