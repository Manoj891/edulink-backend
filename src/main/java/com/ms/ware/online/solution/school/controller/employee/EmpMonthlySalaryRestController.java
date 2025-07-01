/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.controller.employee;

import com.ms.ware.online.solution.school.config.DateConverted;
import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.employee.EmpMonthlySalaryDao;
import com.ms.ware.online.solution.school.dao.employee.EmployeeSalaryInfoDao;
import com.ms.ware.online.solution.school.entity.account.Voucher;
import com.ms.ware.online.solution.school.entity.account.VoucherDetail;
import com.ms.ware.online.solution.school.entity.employee.EmpMonthlySalary;
import com.ms.ware.online.solution.school.entity.employee.EmpMonthlySalaryPK;
import com.ms.ware.online.solution.school.entity.employee.EmployeeSalaryInfo;
import com.ms.ware.online.solution.school.entity.employee.MonthlySalaryReq;
import com.ms.ware.online.solution.school.exception.CustomException;
import com.ms.ware.online.solution.school.service.account.VoucherEntryService;
import com.ms.ware.online.solution.school.service.account.VoucherPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * @author Manoj
 */
@RestController
@RequestMapping("api/Employee/MonthlySalary")
public class EmpMonthlySalaryRestController {
    @Autowired
    private AuthenticationFacade facade;
    @Autowired
    private EmpMonthlySalaryDao dao;
    @Autowired
    EmployeeSalaryInfoDao salaryInfoDao;
    @Autowired
    VoucherEntryService service;
    private List<TaxSlabRes> TaxSlabRess = new ArrayList<>();

    @GetMapping("/report")
    public ResponseEntity<List<EmpMonthlySalary>> salaryReport(@RequestParam String month, HttpServletRequest request) throws IOException {
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            throw new CustomException("Invalid Token");
        }
        String[] tt = month.split("-");
        return ResponseEntity.status(200).body(dao.getAll("from EmpMonthlySalary where year=" + tt[0] + " AND month='" + tt[1] + "'"));
    }

    @GetMapping
    public Object generateSalary(@RequestParam String month, HttpServletRequest request) throws IOException {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return ResponseEntity.status(200).body(message.respondWithError("invalid token"));
        }
        String[] tt = month.split("-");
        String date = month + "-01";
        date = DateConverted.bsToAd(date);
        String sql = "SELECT E.id empId, CONCAT(E.first_name,' ',E.middle_name,' ',E.last_name) AS empName,S.BASIC_SALARY basicSalary,S.LEVEL_UPGRADE levelUpgrade,GRADE_UPGRADE gradeUpgrade,IFNULL(CIT,0) cit,IFNULL(EMPLOYER_CIT,0) employerCit,CIT_TYPE citType,IFNULL(PF,0) pf,PF_TYPE pfType,IFNULL(EMPLOYER_PF,0) employerPf  FROM employee_info E ,employee_salary_info S where E.id=S.EMP_ID  AND '" + date + "' between  EFFECTIVE_DATE_FROM AND IFNULL(EFFECTIVE_DATE_TO,'" + date + "') AND working_status='Y' AND E.ID NOT IN(SELECT EMP_ID FROM emp_monthly_salary WHERE YEAR=" + tt[0] + " AND MONTH='" + tt[1] + "')";

        Map map = new HashMap();
        map.put("data", dao.getRecord(sql));
        map.put("generated", dao.getAll("from EmpMonthlySalary where year=" + tt[0] + " AND month='" + tt[1] + "' AND approveDate is null"));
        map.put("year", tt[0]);
        map.put("month", tt[1]);
        return map;
    }

    @PostMapping
    public Object generateSalary(@RequestBody MonthlySalaryReq jsonData) {

        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return ResponseEntity.status(200).body(message.respondWithError("invalid token"));
        }
        Map map;
        String maritalStatus, sql, enterBy = td.getUserName();

        Date date = new Date();
        try {
            int totalDayOfMonth = 0, yy, MM, fiscalYearMonth;
            long empId = 0, fiscalYear;

            String year = jsonData.getYear();
            String month = jsonData.getMonth();
            fiscalYearMonth = findFiscalYearMonth(month);

            if (month.length() == 1) {
                month = "0" + month;
            }
            map = (Map) dao.getRecord("SELECT COUNT(AD_DATE) as totalDayOfMonth FROM ad_bs_calender WHERE BS_DATE LIKE '" + year + "-" + month + "%'").get(0);
            totalDayOfMonth = Integer.parseInt(map.get("totalDayOfMonth").toString());
            String monthEndDate = DateConverted.bsToAd(year + "-" + month + "-" + totalDayOfMonth);
            map = (Map) dao.getRecord("SELECT ID AS fiscalYear FROM fiscal_year WHERE '" + monthEndDate + "' BETWEEN START_DATE AND END_DATE").get(0);
            fiscalYear = Integer.parseInt(map.get("fiscalYear").toString());
            yy = Integer.parseInt(year);
            MM = Integer.parseInt(month);
            double nonRegularAllowance = 0, regularAllowance = 0, leaveDeductionDay, leaveDeductionAmount, oneDaySalary;
            double projectionSalary, basicSalary, level, grade, insurance, pf = 0, cit = 0, eef = 0, pfE = 0, citE = 0, eefE = 0, grossSalary, pfCitDeduction;
            sql = "SELECT PERCENTAGE percentage,COUPLE_INCOME couple,INDIVIDUL_INCOME individual FROM tax_slab WHERE FISCAL_YEAR='" + fiscalYear + "' ORDER BY PERCENTAGE";
            TaxSlabRess = new ArrayList<>();
            dao.getRecord(sql).forEach(o -> {
                Map m = (Map) o;
                TaxSlabRess.add(new TaxSlabRes(Double.parseDouble(m.get("percentage").toString()), Double.parseDouble(m.get("couple").toString()), Double.parseDouble(m.get("individual").toString())));
            });
            for (Map<String, Object> emp : jsonData.getObj()) {
                leaveDeductionDay = Double.parseDouble(emp.get("ldd").toString());
                if (leaveDeductionDay > totalDayOfMonth) {
                    return ResponseEntity.status(200).body(message.respondWithError("Leave Deduction Day Not Support more then " + totalDayOfMonth));
                }
                empId = Long.parseLong(emp.get("empId").toString());
                List<EmployeeSalaryInfo> salaryInfos = salaryInfoDao.getAll("from EmployeeSalaryInfo where employeeId=" + empId + " and '" + monthEndDate + "' BETWEEN effectiveDateFrom AND IFNULL(effectiveDateTo,'" + monthEndDate + "') ORDER BY id desc");
                if (salaryInfos.isEmpty()) {
                    return message.respondWithError("Please define salary first");
                }

                sql = "SELECT IFNULL(SUM(AMOUNT),0) nonRegularAllowance FROM monthly_allowance WHERE `EMP_ID`=" + empId + " AND `YEAR`=" + yy + " AND `MONTH`=" + MM;
                for (Object o : dao.getRecord(sql)) {
                    Map m = (Map) o;
                    nonRegularAllowance += (double) m.get("nonRegularAllowance");
                }
                sql = "SELECT IFNULL(SUM(AMOUNT),0) regularAllowance FROM regular_allowance WHERE `EMP_ID`=" + empId;
                for (Object o : dao.getRecord(sql)) {
                    Map m = (Map) o;
                    regularAllowance += (double) m.get("regularAllowance");
                }
                EmployeeSalaryInfo info = salaryInfos.get(0);
                maritalStatus = info.getEmpId().getMaritalStatus();
                basicSalary = info.getBasicSalary();
                level = info.getLevelUpgrade();
                grade = info.getGradeUpgrade();
                oneDaySalary = (basicSalary + regularAllowance) / totalDayOfMonth;
                leaveDeductionAmount = oneDaySalary * leaveDeductionDay;
                insurance = info.getHealthInsurance() + info.getPersonalInsurance();
                if (insurance > 25000) {
                    insurance = 25000;
                }
                if (info.getPfType().equalsIgnoreCase("P")) {
                    pf = ((basicSalary + grade + level) * info.getPf()) / 100;
                    pfE = ((basicSalary + grade + level) * info.getEmployerPf()) / 100;
                } else {
                    pf = info.getPf();
                    pfE = info.getEmployerPf();
                }
                if (info.getCitType().equalsIgnoreCase("P")) {
                    cit = ((basicSalary + grade + level) * info.getCit()) / 100;
                    citE = ((basicSalary + grade + level) * info.getEmployerCit()) / 100;
                } else {
                    cit = info.getCit();
                    citE = info.getEmployerCit();
                }
                if (info.getCitType().equalsIgnoreCase("P")) {
                    eef = ((basicSalary + grade + level) * info.getEmployeesFund()) / 100;
                    eefE = ((basicSalary + grade + level) * info.getEmployerEmployeesFund()) / 100;
                } else {
                    eef = info.getEmployeesFund();
                    eefE = info.getEmployerEmployeesFund();
                }
                grossSalary = basicSalary + regularAllowance + nonRegularAllowance + level + grade + pfE + citE + eefE - leaveDeductionAmount;
//                pfCitDeduction = pf + cit + eef + pfE + citE + eefE;
                pfCitDeduction = pf + cit + pfE + citE;

                sql = "SELECT IFNULL(SUM(BASIC_SALARY+GRADE_UPGRADE+LEVEL_UPGRADE+REGULAR_ALLOWANCE+NON_REGULAR_ALLOWANCE),0) preSalary,"
                        + "IFNULL(SUM(EMPLOYEES_CIT+EMPLOYERS_CIT+EMPLOYEES_PF+EMPLOYERS_PF),0) prePfCit,IFNULL(SUM(sst_payable),0) sstPayable,IFNULL(SUM(income_tax_payable),0) incomeTaxPayable FROM emp_monthly_salary WHERE EMP_ID=" + empId + " AND FISCAL_YEAR=" + fiscalYear + " AND FISCAL_YEAR_MONTH<" + fiscalYearMonth;
                double preSalary = 0;
                double prePfCit = 0, preSSTPayable = 0, preIncomeTax = 0;
                for (Object o : dao.getRecord(sql)) {
                    Map m = (Map) o;
                    preSalary += (double) m.get("preSalary");
                    prePfCit += (double) m.get("prePfCit");
                    preIncomeTax += (double) m.get("incomeTaxPayable");
                    preSSTPayable += (double) m.get("sstPayable");
                }
                pfCitDeduction = ((13 - fiscalYearMonth) * pfCitDeduction) + prePfCit;
                projectionSalary = ((13 - fiscalYearMonth)) * grossSalary + preSalary;
                double oneThird = (projectionSalary / 3);
                if (pfCitDeduction > 300000) {
                    pfCitDeduction = 300000;
                }
                if (pfCitDeduction > oneThird) {
                    pfCitDeduction = oneThird;
                }
                double taxableSalary = projectionSalary - pfCitDeduction - insurance;
//                double tdsAmount = (tdsAmount(taxableSalary, maritalStatus) - preTds); / (13 - fiscalYearMonth);
                tdsAmount(taxableSalary, maritalStatus);
                sstPayable = (sstPayable - preSSTPayable) / (13 - fiscalYearMonth);
                incomeTaxPayable = (incomeTaxPayable - preIncomeTax) / (13 - fiscalYearMonth);
//                if (sstPayable < 0) {
//                    sstPayable = sstPayable + incomeTaxPayable;
//                    incomeTaxPayable=0;
//                }
                EmpMonthlySalary obj = new EmpMonthlySalary(new EmpMonthlySalaryPK(empId, yy, MM));
                obj.setFiscalYear(fiscalYear);
                obj.setFiscalYearMonth(fiscalYearMonth);
                obj.setBasicSalary(basicSalary);
                obj.setLevelUpgrade(level);
                obj.setGradeUpgrade(grade);
                obj.setRegularAllowance(regularAllowance);
                obj.setNonRegularAllowance(nonRegularAllowance);
                obj.setTotalSalary(obj.getBasicSalary() + obj.getLevelUpgrade() + obj.getGradeUpgrade() + obj.getRegularAllowance() + obj.getNonRegularAllowance() - leaveDeductionAmount);
                obj.setEmployersFund(eefE);
                obj.setEmployersCit(citE);
                obj.setEmployersPf(pfE);
                obj.setTotalEarning(obj.getTotalSalary() + eefE + citE + pfE);
                obj.setProjectingSalary(projectionSalary);
                obj.setProjectedPFCIT(pfCitDeduction);
                obj.setEmployeesCit(cit);
                obj.setEmployeesPf(pf);
                obj.setEmployeesFund(eef);
                obj.setInsurance(insurance);
                obj.setTaxableSalary(taxableSalary);
                obj.setSstPayable(sstPayable);
                obj.setIncomeTaxPayable(incomeTaxPayable);
                obj.setEnterBy(enterBy);
                obj.setEnterDate(date);
                obj.setLeaveDeductionAmount(leaveDeductionAmount);
                obj.setLeaveDeductionDay(leaveDeductionDay);
                obj.setNetPayable(obj.getTotalSalary() - (obj.getEmployeesFund() + obj.getEmployeesPf() + obj.getEmployeesCit() + sstPayable + incomeTaxPayable));
                dao.save(obj);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return message.respondWithMessage("Success");
    }

    @PutMapping
    public ResponseEntity<?> approveSalary(@RequestParam Integer year, @RequestParam Integer month, @RequestParam Long empId, HttpServletRequest request) {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return ResponseEntity.status(200).body(message.respondWithError("invalid token"));
        }
        Date date = new Date();
        String sql = "SELECT IFNULL(ac_code,'') ac_code,CONCAT(IFNULL(first_name,''),' ',IFNULL(middle_name,''),' ',IFNULL(last_name,''),'[',IFNULL(code,''),']') AS empName FROM employee_info WHERE id=" + empId;
        List l = dao.getRecord(sql);
        System.out.println(l);
        if (l.isEmpty()) {
            return ResponseEntity.status(200).body(message.respondWithError("Please define organization master"));
        }
        Map m = (Map) l.get(0);
        String empAcCode = m.get("ac_code").toString();
        if (empAcCode.length() < 3) {
            return ResponseEntity.status(200).body(message.respondWithError("Please define organization master"));
        }
        String empName = m.get("empName").toString();
        sql = "SELECT cit_payable citPayable,employee_fund_payable employeeFundPayable,pf_payable pfPayable, income_tax_payable incomeTaxPayable,sst_payable sstPayable,salary_expenses salaryExpenses FROM organization_master";
        l = dao.getRecord(sql);
        if (l.isEmpty()) {
            return ResponseEntity.status(200).body(message.respondWithError("Please define organization master"));
        }
        m = (Map) l.get(0);
        String citPayable = m.get("citPayable").toString();
        String employeeFundPayable = m.get("employeeFundPayable").toString();
        String pfPayable = m.get("pfPayable").toString();
        String incomeTaxPayable = m.get("incomeTaxPayable").toString();
        String sstPayable = m.get("sstPayable").toString();
        String salaryExpenses = m.get("salaryExpenses").toString();

        dao.getAll("from EmpMonthlySalary where empId=" + empId + " AND year=" + year + " AND month=" + month).forEach(obj -> {
            if (obj.getApproveDate() != null) {
                throw new CustomException("Record Already Approved");
            }

            List<VoucherDetail> detail = new ArrayList<>();
            detail.add(new VoucherDetail(empAcCode, "Being Salary Receive in your account", 0d, obj.getNetPayable()));
            detail.add(new VoucherDetail(sstPayable, "Being amount receive from " + empName, 0d, obj.getSstPayable()));

            detail.add(new VoucherDetail(incomeTaxPayable, "Being amount receive from " + empName, 0d, obj.getIncomeTaxPayable()));
            if ((obj.getEmployeesCit() + obj.getEmployersCit()) > 0) {
                detail.add(new VoucherDetail(citPayable, "Being amount receive from " + empName, 0d, obj.getEmployeesCit() + obj.getEmployersCit()));
            }
            if ((obj.getEmployersPf() + obj.getEmployeesPf()) > 0) {
                detail.add(new VoucherDetail(pfPayable, "Being amount receive from " + empName, 0d, obj.getEmployeesPf() + obj.getEmployersPf()));
            }
            if ((obj.getEmployeesFund() + obj.getEmployersFund()) > 0) {
                detail.add(new VoucherDetail(employeeFundPayable, "Being amount receive from " + empName, 0d, obj.getEmployeesFund() + obj.getEmployersFund()));
            }
            double salary = obj.getNetPayable()
                    + obj.getSstPayable()
                    + obj.getIncomeTaxPayable()
                    + obj.getEmployeesCit() + obj.getEmployersCit()
                    + obj.getEmployersPf() + obj.getEmployeesPf()
                    + obj.getEmployeesFund() + obj.getEmployersFund();
            detail.add(new VoucherDetail(salaryExpenses, "Being Salary Receive in your account", salary, 0d));
            Voucher v = new Voucher();
            v.setDetail(detail);
            v.setNarration("Being Salary for " + empName + " of year " + obj.getYear() + " month " + obj.getMonth());
            v.setEnterDateAd(date);
            Map map = (Map) service.journalVoucher(v, "JVR");
            String voucherNo = map.get("data").toString();
            obj.setVoucherNo(voucherNo);
            obj.setApproveBy(td.getUserName());
            obj.setApproveDate(date);
            dao.save(obj);
            new VoucherPost().post(new String[]{voucherNo}, td.getUserName(), DateConverted.toString(date));
        });
        return ResponseEntity.status(200).body(message.respondWithMessage("Success"));
    }

    @PatchMapping
    public ResponseEntity<?> removeSalary(@RequestParam Integer year, @RequestParam Integer month, @RequestParam Long empId, HttpServletRequest request) {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return ResponseEntity.status(200).body(message.respondWithError("invalid token"));
        }
        dao.getAll("from EmpMonthlySalary where empId=" + empId + " AND year=" + year + " AND month=" + month).forEach(empMonthlySalary -> {
            if (empMonthlySalary.getApproveDate() != null) {
                throw new CustomException("Record Already Approved");
            }
            dao.delete("DELETE FROM emp_monthly_salary where  emp_id=" + empId + " AND year=" + year + " AND month=" + month);
        });
        return ResponseEntity.status(200).body(message.respondWithMessage("Success"));
    }

    private double sstPayable;
    private double incomeTaxPayable;

    private void tdsAmount(double taxableSalary, String maritalStatus) {
        int i = 0;
        double tds = 0, calculateAmount = 0;
        sstPayable = 0;
        incomeTaxPayable = 0;
        if (maritalStatus.equalsIgnoreCase("Unmarried")) {
            for (TaxSlabRes TaxSlabRes : TaxSlabRess) {
                if (TaxSlabRes.getIndividual() > taxableSalary) {
                    tds += (taxableSalary - calculateAmount) * TaxSlabRes.getPercentage() / 100;
                    break;
                } else {
                    tds += (TaxSlabRes.getIndividual() - calculateAmount) * TaxSlabRes.getPercentage() / 100;
                    calculateAmount = TaxSlabRes.getIndividual();
                }
                if (i == 0) {
                    sstPayable = tds;
                }
                i++;
            }
        } else {
            for (TaxSlabRes TaxSlabRes : TaxSlabRess) {
                if (TaxSlabRes.getCouple() > taxableSalary) {
                    tds += (taxableSalary - calculateAmount) * TaxSlabRes.getPercentage() / 100;
                    break;
                } else {
                    tds += (TaxSlabRes.getCouple() - calculateAmount) * TaxSlabRes.getPercentage() / 100;
                    calculateAmount = TaxSlabRes.getCouple();
                }
                if (i == 0) {
                    sstPayable = tds;
                }
                i++;
            }
        }
        if (i == 0) {
            sstPayable = tds;
        } else if (tds > sstPayable) {
            incomeTaxPayable = tds - sstPayable;
        } else {
            incomeTaxPayable = 0;
        }
    }

    private int findFiscalYearMonth(String month) {
        if (month.equals("04")) {
            return 1;
        } else if (month.equals("05")) {
            return 2;
        } else if (month.equals("06")) {
            return 3;
        } else if (month.equals("07")) {
            return 4;
        } else if (month.equals("08")) {
            return 5;
        } else if (month.equals("09")) {
            return 6;
        } else if (month.equals("10")) {
            return 7;
        } else if (month.equals("11")) {
            return 8;
        } else if (month.equals("12")) {
            return 9;
        } else if (month.equals("1") || month.equals("01")) {
            return 10;
        } else if (month.equals("2") || month.equals("02")) {
            return 11;
        } else if (month.equals("3") || month.equals("03")) {
            return 12;
        }

        throw new CustomException("Invalid FIscal year month");
    }

    float round(double val) {
        if (val >= 0) {
            return Float.parseFloat("0" + Math.round(val));
        }

        return Float.parseFloat("" + Math.round(val));
    }
}
