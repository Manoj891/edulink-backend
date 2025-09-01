/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.service.configure;

import com.ms.ware.online.solution.school.config.DB;
import com.ms.ware.online.solution.school.config.DateConverted;
import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.model.DatabaseName;
import com.ms.ware.online.solution.school.model.HibernateUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Service
public class ConfigureServiceImpl {
    @Autowired
    private DistrictMunicipalData data;
    @Autowired
    private HibernateUtil util;
    @Autowired
    private DB db;
    @Autowired
    private Message message;

    public void functionConfigure() {
        function();
    }


    public Object databaseConfigure(@RequestParam String dbUser, @RequestParam String dbPassword) {
        String msg = "";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            java.sql.Connection con = java.sql.DriverManager.getConnection("jdbc:mysql://localhost:" + DatabaseName.getPort() + "/mysql", dbUser, dbPassword);
            String sql;

            sql = "CREATE USER '" + DatabaseName.getUsername() + "'@'localhost' IDENTIFIED BY '" + DatabaseName.getPassword() + "';";
            PreparedStatement ps = con.prepareStatement(sql);
            try {
                ps.executeUpdate();
                System.out.println("Success " + sql);
            } catch (Exception e) {
            }
            sql = "GRANT ALL PRIVILEGES ON *.* TO '" + DatabaseName.getUsername() + "'@'localhost';";
            try {
                ps.executeUpdate(sql);
                System.out.println("Success " + sql);
            } catch (Exception e) {
                msg = e.getMessage();

            }
            sql = "CREATE DATABASE " + DatabaseName.getDatabase();
            try {
                ps.executeUpdate(sql);
                System.out.println("Success " + sql);
            } catch (Exception e) {
                msg = e.getMessage();

            }
            con = java.sql.DriverManager.getConnection("jdbc:mysql://localhost:" + DatabaseName.getPort() + "/" + DatabaseName.getDatabase(), DatabaseName.getUsername(), DatabaseName.getPassword());
            sql = "CREATE TABLE temp_not_import (`ID` BIGINT, `MSG` TEXT);";
            ps = con.prepareStatement(sql);
            try {
                ps.executeUpdate();
            } catch (Exception e) {
                msg = e.getMessage();

            }
            ps.close();
            con.close();
            util.init();

            function();
            configure();
        } catch (Exception e) {
            return message.respondWithError(e.getMessage());
        }
        return message.respondWithMessage("Success");
    }

    public void configureDistrictMunicipality() {
        function();
        configure();
        data.setDistrict();
        data.setMunicipal();

    }

    void configure() {
       
        System.gc();
        String sql;
        sql = "INSERT INTO fiscal_year (ID, END_DATE, START_DATE, STATUS, YEAR) VALUES (8081, '2024-07-17', '2023-07-17', 'Y', '2080-2081');\n" +
                "INSERT INTO fiscal_year (ID, END_DATE, START_DATE, STATUS, YEAR) VALUES (8182, '2025-07-17', '2024-07-17', 'N', '2081-2082');";
        db.save(sql);
        sql = "INSERT INTO academic_year (ID, STATUS, YEAR) VALUES (81, 'N', '2081');";
        db.save(sql);
        String ss = DateConverted.adToBs(new Date());
        assert ss != null;
        sql = "UPDATE academic_year SET STATUS='Y' WHERE ID='" + ss.substring(2, 4) + "'";
        db.save(sql);
        sql = "INSERT INTO religion_master(ID,NAME)VALUES(0,'Not-Define');\n"
                + "INSERT INTO religion_master(ID,NAME)VALUES(1,'Hinduism');\n"
                + "INSERT INTO religion_master(ID,NAME)VALUES(2,'Buddhism');\n"
                + "INSERT INTO religion_master(ID,NAME)VALUES(3,'Islam');\n"
                + "INSERT INTO religion_master(ID,NAME)VALUES(4,'Christianity');\n"
                + "INSERT INTO religion_master(ID,NAME)VALUES(5,'Sikhism');\n"
                + "INSERT INTO religion_master(ID,NAME)VALUES(6,'Jainism');\n"
                + "INSERT INTO religion_master(ID,NAME)VALUES(7,'Zoroastrianism');\n"
                + "INSERT INTO religion_master(ID,NAME)VALUES(8,'Other');";
        db.save(sql);
        sql = "INSERT INTO cast_ethnicity_master (id, name) VALUES (0, 'Not-Define');\n" +
                "INSERT INTO cast_ethnicity_master (id, name) VALUES (1, 'Khas-Arya');\n" +
                "INSERT INTO cast_ethnicity_master (id, name) VALUES (2, 'Janajatis');\n" +
                "INSERT INTO cast_ethnicity_master (id, name) VALUES (3, 'Newars');\n" +
                "INSERT INTO cast_ethnicity_master (id, name) VALUES (4, 'Madhesi');\n" +
                "INSERT INTO cast_ethnicity_master (id, name) VALUES (5, 'Marwadi');\n";
        db.save(sql);
        sql = "INSERT INTO grading_system (ID, EFFECTIVE_DATE_FROM, EFFECTIVE_DATE_TO, GPA, GRADE, RANG_FROM, REMARK) VALUES (1, '2016-04-14', NULL, 4.0, 'A+', 90.0, 'Outstanding');\n"
                + "INSERT INTO grading_system (ID, EFFECTIVE_DATE_FROM, EFFECTIVE_DATE_TO, GPA, GRADE, RANG_FROM, REMARK) VALUES (2, '2016-04-14', NULL, 3.6, 'A', 80.0, 'Excellent');\n"
                + "INSERT INTO grading_system (ID, EFFECTIVE_DATE_FROM, EFFECTIVE_DATE_TO, GPA, GRADE, RANG_FROM, REMARK) VALUES (3, '2016-04-14', NULL, 3.2, 'B+', 70.0, 'Very Good');\n"
                + "INSERT INTO grading_system (ID, EFFECTIVE_DATE_FROM, EFFECTIVE_DATE_TO, GPA, GRADE, RANG_FROM, REMARK) VALUES (4, '2016-04-14', NULL, 2.8, 'B', 60.0, 'Good');\n"
                + "INSERT INTO grading_system (ID, EFFECTIVE_DATE_FROM, EFFECTIVE_DATE_TO, GPA, GRADE, RANG_FROM, REMARK) VALUES (5, '2016-04-14', NULL, 2.4, 'C+', 50.0, 'Above Average');\n"
                + "INSERT INTO grading_system (ID, EFFECTIVE_DATE_FROM, EFFECTIVE_DATE_TO, GPA, GRADE, RANG_FROM, REMARK) VALUES (6, '2016-04-14', NULL, 2.0, 'C', 40.0, 'Average');\n"
                + "INSERT INTO grading_system (ID, EFFECTIVE_DATE_FROM, EFFECTIVE_DATE_TO, GPA, GRADE, RANG_FROM, REMARK) VALUES (7, '2016-04-14', NULL, 1.6, 'D', 25.0, 'Below Average');\n"
                + "INSERT INTO grading_system (ID, EFFECTIVE_DATE_FROM, EFFECTIVE_DATE_TO, GPA, GRADE, RANG_FROM, REMARK) VALUES (8, '2016-04-14', NULL, 0.8, 'E', 1.0, 'Insufficient');\n"
                + "INSERT INTO grading_system (ID, EFFECTIVE_DATE_FROM, EFFECTIVE_DATE_TO, GPA, GRADE, RANG_FROM, REMARK) VALUES (9, '2016-04-14', NULL, 0.0, 'F', 0.0, 'Fail');";
        db.save(sql);
        sql = "INSERT INTO hostal_type_master (ID,NAME,AMOUNT,EFFECT) VALUES (1, 'day hostel', 0.0, 'Y');\n"
                + "INSERT INTO hostal_type_master (ID,NAME,AMOUNT,EFFECT) VALUES (2, 'Full Time hostel', 0.0, 'Y');";
        db.save(sql);

        sql = "CREATE TABLE ad_bs_calender (AD_DATE DATE NOT NULL, BS_DATE VARCHAR(10) NOT NULL, DAY VARCHAR(255), SCHOOL_HOLYDAY VARCHAR(1) DEFAULT N , STUDENT_HOLYDAY VARCHAR(1) DEFAULT N , EVENT VARCHAR(255), PRIMARY KEY (AD_DATE));";
        db.save(sql);
        sql = "INSERT INTO `chart_of_account` (`AC_CODE`, `AC_NAME`, `AC_SN`, `LEVEL`, `MGR_CODE`, `TRANSACT`) VALUES\n"
                + "('1', 'Assets', 1, 1, NULL, 'N'),\n"
                + "('101', 'Fixed Asset', 1, 2, '1', 'N'),\n"
                + "('10101', 'Land and Building', 1, 3, '101', 'N'),\n"
                + "('1010101', 'Land Purchase', 1, 4, '10101', 'Y'),\n"
                + "('1010102', 'Building Construction ', 2, 4, '10101', 'Y'),\n"
                + "('1010103', 'Building Renovation ', 3, 4, '10101', 'Y'),\n"
                + "('10102', 'Furniture ', 2, 3, '101', 'N'),\n"
                + "('1010201', 'Furniture Purchase', 1, 4, '10102', 'Y'),\n"
                + "('1010202', 'Furniture Repair & Maintenance ', 2, 4, '10102', 'Y'),\n"
                + "('10103', 'Office Equipment ', 3, 3, '101', 'N'),\n"
                + "('1010301', 'Computer Purchase', 1, 4, '10103', 'Y'),\n"
                + "('1010302', 'Software Purchase', 2, 4, '10103', 'Y'),\n"
                + "('10104', 'Share Investment ', 4, 3, '101', 'N'),\n"
                + "('1010401', 'Teacher Welfare Saving & Credit ', 1, 4, '10104', 'Y'),\n"
                + "('10105', 'Deposit & Surplus (Assets)', 5, 3, '101', 'N'),\n"
                + "('1010501', 'Nepal Telecom (Internet Deposit)', 1, 4, '10105', 'Y'),\n"
                + "('102', 'Current Asset ', 2, 2, '1', 'N'),\n"
                + "('10201', 'Inventory', 1, 3, '102', 'N'),\n"
                + "('1020101', 'Student Inventory', 1, 4, '10201', 'N'),\n"
                + "('102010101', 'Student Belt Inventory', 1, 4, '1020101', 'Y'),\n"
                + "('102010102', 'Student Tie Inventory', 2, 4, '1020101', 'Y'),\n"
                + "('10202', 'Cash & Bank Account', 2, 3, '102', 'N'),\n"
                + "('1020201', 'Cash Account', 1, 4, '10202', 'N'),\n"
                + "('102020101', 'Main Cash', 1, 5, '1020201', 'Y'),\n"
                + "('1020202', 'Bank Account', 2, 4, '10202', 'N'),\n"
                + "('10203', 'Sundry Debtors', 3, 3, '102', 'N'),\n"
                + "('1020301', 'Employee Advance', 1, 4, '10203', 'N'),\n"
                + "('1020302', 'Advance to Vendors ', 2, 4, '10203', 'N'),\n"
                + "('102030201', 'Procurement Committee', 1, 5, '1020302', 'Y'),\n"
                + "('10204', 'Petty Cash A/C', 4, 3, '102', 'N'),\n"
                + "('103', 'Advertisement Publicity', 3, 2, '1', 'Y'),\n"
                + "('2', 'Liabilities', 2, 1, NULL, 'N'),\n"
                + "('201', 'Current Liabilities ', 1, 2, '2', 'N'),\n"
                + "('20101', 'Sundry Creditors', 1, 3, '201', 'N'),\n"
                + "('2010102', 'VAT Payable of Venders  ', 2, 4, '20101', 'Y'),\n"
                + "('20102', 'Reserves and Surplus', 2, 3, '201', 'Y'),\n"
                + "('20103', 'PF Payable A/C', 3, 3, '201', 'Y'),\n"
                + "('20104', 'Tax Payable-Social Service Tax (1%)', 4, 3, '201', 'Y'),\n"
                + "('20105', 'Tax Payable-Pvt. Ltd. Tax (1.5%)', 5, 3, '201', 'Y'),\n"
                + "('20106', 'Tax Payable (15%)', 6, 3, '201', 'Y'),\n"
                + "('20107', 'Guarantee & Deposit of Suppliers  ', 7, 3, '201', 'Y'),\n"
                + "('202', 'Capital a/c', 2, 2, '2', 'N'),\n"
                + "('20201', 'Shareholder', 1, 3, '202', 'N'),\n"
                + "('3', 'Income', 3, 1, NULL, 'N'),\n"
                + "('301', 'Direct Income', 1, 2, '3', 'N'),\n"
                + "('30101', 'Student Fee Income', 1, 3, '301', 'N'),\n"
                + "('3010101', 'St. Other Fee Income', 1, 3, '30101', 'Y'),\n"
                + "('3010102', 'St. Hostal Charge Income', 2, 3, '30101', 'Y'),\n"
                + "('3010103', 'St. Transpotation Charge Income', 3, 3, '30101', 'Y'),\n"
                + "('3010104', 'St. Opening Balance Fee Income', 4, 3, '30101', 'Y'),\n"
                + "('3010105', 'St. Admission Form Fee Income', 5, 3, '30101', 'Y'),\n"
                + "('3010106', 'St. Tuition Fee Income', 6, 3, '30101', 'Y'),\n"
                + "('3010107', 'St. ID Card Income', 7, 3, '30101', 'Y'),\n"
                + "('3010108', 'St. Exam Fee Income', 8, 3, '30101', 'Y'),\n"
                + "('3010109', 'St. Admission Fee Income', 9, 3, '30101', 'Y'),\n"
                + "('30102', 'House Rent Income', 2, 3, '301', 'N'),\n"
                + "('302', 'Indirect Income', 2, 2, '3', 'N'),\n"
                + "('30201', 'Grand Received from UGC (Tracer) ', 1, 3, '302', 'Y'),\n"
                + "('30202', 'Grants Received from UGC (Online Teaching)', 2, 3, '302', 'Y'),\n"
                + "('30203', 'Grants Received from Bagmati Provence (Building Upgrade)', 3, 3, '302', 'Y'),\n"
                + "('30204', 'Grants Received from UGC (Small Procurement & Construction) )', 4, 3, '302', 'Y'),\n"
                + "('4', 'Expense', 4, 1, NULL, 'N'),\n"
                + "('401', 'Administration Expenses', 1, 2, '4', 'N'),\n"
                + "('40101', 'Annual Anniversary Exp', 1, 3, '401', 'Y'),\n"
                + "('40102', 'Audit Expenses ', 2, 3, '401', 'Y'),\n"
                + "('40103', 'Stationery Expenses', 3, 3, '401', 'Y'),\n"
                + "('40104', 'Repair & Maintenance Expenses (Office) ', 4, 3, '401', 'Y'),\n"
                + "('40105', 'Printing Expenses', 5, 3, '401', 'Y'),\n"
                + "('40106', 'Telephone / Internet Expenses', 6, 3, '401', 'Y'),\n"
                + "('40107', 'Design, Estimation & Consultancy Expenses', 7, 3, '401', 'Y'),\n"
                + "('402', 'Utility Expenses', 2, 2, '4', 'N'),\n"
                + "('40202', 'Electricity & Water Expenses ', 2, 3, '402', 'Y'),\n"
                + "('40204', 'News Paper Expenses', 4, 3, '402', 'Y'),\n"
                + "('40205', 'Meeting & Conference Exp. ', 5, 3, '402', 'Y'),\n"
                + "('404', 'Other Fringe and Benefits Exp. (STAFF)', 4, 2, '4', 'Y'),\n"
                + "('40401', 'Provident Fund (STAFF)', 1, 3, '404', 'Y'),\n"
                + "('40402', 'Gratuity Fund (STAFF)', 2, 3, '404', 'Y'),\n"
                + "('40403', 'Festival Allowance (STAFF)', 3, 3, '404', 'Y'),\n"
                + "('40404', 'Communication Allowance (STAFF)', 4, 3, '404', 'Y'),\n"
                + "('40406', 'Other Allowance (STAFF)', 6, 3, '404', 'Y'),\n"
                + "('405', 'Salary Expenses (STAFF)', 5, 2, '4', 'Y'),\n"
                + "('406', 'Tracer Study Expenses (STAFF)', 6, 2, '4', 'Y'),\n"
                + "('407', 'Extra Class Expenses (STAFF)', 7, 2, '4', 'Y');";
        db.save(sql);
        sql = "INSERT INTO organization_user_info (ID, CASH_ACCOUNT, EMAIL, EMP_NAME, LOGIN_ID, LOGIN_PASS, MOBILE, STATUS, TOKEN, USER_TYPE) VALUES (1, '102020101', 'msware9@gmail.com', 'ADMIN', 'ADMIN', CONCAT('*', UPPER(SHA1(UNHEX(SHA1('ADMIN@531'))))), '9876543210', 'Y', NULL, 'ADM');";
        db.save(sql);

        sql = "INSERT INTO organization_master (ID, ADDRESS, CASH_ACCOUNT, DISTRICT, EMAIL, ESTABLISH_YEAR, INVENTORY_ACCOUNT, MUNICIPAL, NAME, PROVINCE, RESERVES_AND_SURPLUS, SCHOOL_CODE, SMS_SEND_API, STUDENT_FEE_INCOME_ACCOUNT, STUDENT_INVENTORY_ACCOUNT, SUNDRY_CREDITORS, SUNDRY_DEBTORS, TEL, URL, WARD_NO) VALUES (1, 'New Baneshwor', '1020201', 'KATHMANDU',  'youremail@email.com', '2076', '10201', 'KATHMANDU METROPOLITAN', 'New Demo Secondary English School', '3', '20102', NULL, NULL, '30101', '1020101', '20101', '10203', '9842828027', 'www.yoururl.com', '34');";
        db.save(sql);
        sql = "INSERT INTO subject_group (ID, NAME) VALUES (1, 'General Group');";
        db.save(sql);
        sql = "INSERT INTO class_master (ID, NAME) VALUES (1, 'One');\n"
                + "INSERT INTO class_master (ID, NAME) VALUES (2, 'Two');\n"
                + "INSERT INTO class_master (ID, NAME) VALUES (3, 'Three');\n"
                + "INSERT INTO class_master (ID, NAME) VALUES (4, 'Four');\n"
                + "INSERT INTO class_master (ID, NAME) VALUES (5, 'Five');\n"
                + "INSERT INTO class_master (ID, NAME) VALUES (6, 'Six');\n"
                + "INSERT INTO class_master (ID, NAME) VALUES (7, 'Seven');\n"
                + "INSERT INTO class_master (ID, NAME) VALUES (8, 'Eight');\n"
                + "INSERT INTO class_master (ID, NAME) VALUES (9, 'Nine');\n"
                + "INSERT INTO class_master (ID, NAME) VALUES (10, 'Ten');\n"
                + "INSERT INTO class_master (ID, NAME) VALUES (11, 'Eleven ');\n"
                + "INSERT INTO class_master (ID, NAME) VALUES (12, 'Twelve');\n"
                + "INSERT INTO class_master (ID, NAME) VALUES (13, 'Nursery');\n"
                + "INSERT INTO class_master (ID, NAME) VALUES (14, 'LKG');\n"
                + "INSERT INTO class_master (ID, NAME) VALUES (15, 'UKG');";
        db.save(sql);
        sql = "INSERT INTO program_master (ID, NAME) VALUES (1, 'General');";
        db.save(sql);
        sql = "INSERT INTO bill_master (ID, AC_CODE, IS_INVENTORY, NAME, STATUS) VALUES (-3, '3010101', 'N', 'Others fee paid', 'Y');\n"
                + "INSERT INTO bill_master (ID, AC_CODE, IS_INVENTORY, NAME, STATUS) VALUES (-2, '3010102', 'N', 'Hostal Charge', 'N');\n"
                + "INSERT INTO bill_master (ID, AC_CODE, IS_INVENTORY, NAME, STATUS) VALUES (-1, '3010103', 'N', 'Transpotation Charge', 'N');\n"
                + "INSERT INTO bill_master (ID, AC_CODE, IS_INVENTORY, NAME, STATUS) VALUES (0, '3010104', 'N', 'St. Opening Balance', 'N');\n"
                + "INSERT INTO bill_master (ID, AC_CODE, IS_INVENTORY, NAME, STATUS) VALUES (1, '3010105', 'N', 'Admission Form Fee', 'Y');\n"
                + "INSERT INTO bill_master (ID, AC_CODE, IS_INVENTORY, NAME, STATUS) VALUES (2, '3010106', 'N', 'Tuition Fee', 'Y');\n"
                + "INSERT INTO bill_master (ID, AC_CODE, IS_INVENTORY, NAME, STATUS) VALUES (3, '3010107', 'N', 'ID Card', 'Y');\n"
                + "INSERT INTO bill_master (ID, AC_CODE, IS_INVENTORY, NAME, STATUS) VALUES (4, '3010108', 'N', 'Exam Fee', 'Y');\n"
                + "INSERT INTO bill_master (ID, AC_CODE, IS_INVENTORY, NAME, STATUS) VALUES (5, '102010101', 'Y', 'School Tie', 'Y');\n"
                + "INSERT INTO bill_master (ID, AC_CODE, IS_INVENTORY, NAME, STATUS) VALUES (6, '102010102', 'Y', 'School Belt', 'Y');";
        db.save(sql);
        sql = "INSERT INTO sundry_creditors (ID, AC_CODE, ADDRESS, CONTACT_NO, NAME, PAN_VAT_NO) VALUES (-1, '1', '', '', 'Student inventory Issue', '');"
                + "INSERT INTO sundry_creditors (ID, AC_CODE, ADDRESS, CONTACT_NO, NAME, PAN_VAT_NO) VALUES (0, '101', '', '', 'Direct Purchase', '');";
        db.save(sql);
        sql = "INSERT INTO purchase_order (ORDER_NO, APPROVE_BY, APPROVE_DATE, ENTER_BY, ENTER_DATE, FISCAL_YEAR, NARRATION, ORDER_SN, STATUS, SUPPLIER, WITHIN_DATE) VALUES (-1, '', '2020-03-08', '', '2020-03-08', 7677, '', 1, 'A', -1, '2020-03-08');\n"
                + "INSERT INTO purchase_order (ORDER_NO, APPROVE_BY, APPROVE_DATE, ENTER_BY, ENTER_DATE, FISCAL_YEAR, NARRATION, ORDER_SN, STATUS, SUPPLIER, WITHIN_DATE) VALUES (0, '', '2020-03-08', '', '2020-03-08', 7677, '', 1, 'A', 0, '2020-03-08');";
        db.save(sql);
        sql = "alter table stu_billing_master add unique key(REFERENCE_ID,REG_NO)";
        db.save(sql);
        sql = "delete from menu_user_access;\n" +
                "delete from menu_master;\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (1, 'Setup', 'Academic Year', '/Setup/AcademicYear');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (2, 'Student', 'Education Session', '/Student/SchoolClassSession');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (3, 'Account', 'Fiscal Year', '/Account/FiscalYear');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (4, 'Setup', 'Bill Master', '/Setup/BillMaster');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (5, 'Setup', 'Class Master', '/Setup/ClassMaster');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (6, 'Setup', 'Program Master', '/Setup/ProgramMaster');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (7, 'Setup', 'Section Master', '/Setup/Section');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (8, 'Setup', 'Subject Master ', '/Setup/SubjectMaster');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (9, 'Setup', 'Subject Group', '/Setup/SubjectGroup');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (10, 'Setup', 'Religion Master', '/Setup/ReligionMaster');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (11, 'Setup', 'Cast Ethnicity', '/Setup/CastEthnicityMaster');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (12, 'Setup', 'Hostel Type ', '/Setup/HostalTypeMaster');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (13, 'Setup', 'Bus Master ', '/Setup/BusMaster');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (14, 'Setup', 'Bus Station ', '/Setup/BusStationMaster');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (15, 'Setup', 'Bus Station Time', '/Setup/BusStationTime');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (16, 'Setup', 'Allowance Master', '/Setup/AllowanceMaster');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (18, 'Billing', 'Manage Class Fee(z)', '/Billing/ClassBillEffect');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (19, 'Billing', 'Waive Fee(Discount)(w)', '/Billing/StuWavFee');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (20, 'Billing', 'Manage Bill(m)', '/Billing/ManageCreditBill');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (21, 'Billing', 'Credit Bill(b)', '/Billing/CreditBill');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (22, 'Billing', 'Bill Collect(c)', '/Billing/StuBillingMaster');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (23, 'Billing', 'Other Bill Receipt', '/Billing/OthersBillReceipt');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (24, 'Billing', 'Bill Collect Report', '/Billing/BillCollectReport');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (25, 'Billing', 'Individual Fee Report', '/Billing/IndividualFeeReport');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (26, 'Billing', 'Fee Title Wise Report', '/Billing/FeeWiseReport');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (27, 'Billing', 'Waive Fee Report', '/Billing/WavFeeReport');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (28, 'Billing', 'Class Wise Report', '/Billing/ClassWiseReport');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (29, 'Billing', 'Cancel Bill Report', '/Billing/DeletedReport');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (30, 'Billing', 'Waive Ugc Report', '/Billing/WaiveUgcReport');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (32, 'Student', 'Pre Admission', '/Student/PreAdmission');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (33, 'Student', 'Student Admission (a)', '/Student/StudentInfo');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (34, 'Student', 'Previous Education', '/Student/PreviousEducation');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (35, 'Student', 'Class Transfer', '/Student/ClassTransfer');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (36, 'Student', 'Class Update', '/Student/ClassUpdate');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (37, 'Student', ' Date Wise Attendance', '/Student/DateWiseAttendance');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (38, 'Student', 'Monthly Attendance', '/Student/MonthlyAttendance');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (39, 'Student', 'Class Wise Report', '/Student/StudentReport');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (40, 'Student', 'Gender/Cast Wise Report', '/Student/GenderWise');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (41, 'Student', 'Transportation ', '/Student/Transportation');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (42, 'Student', 'Student Hostel', '/Student/SchoolHostel');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (43, 'Student', 'Drop out', '/Student/Dropout');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (44, 'Student', 'Approve Online Admission', '/Student/OnlineAdmissionApprove');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (45, 'Student', 'Print Student ID Card', '/Student/IDCard');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (46, 'Student', 'Print Certificates', '/Student/certificate');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (47, 'Student', ' Student Import', '/Student/Import');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (48, 'Utility', 'Biometric Device Map', '/Utility/BiometricDeviceMap');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (50, 'Inventory', 'Sundry Creditors', '/Inventory/SundryCreditors');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (51, 'Inventory', 'Purchase Order', '/Inventory/PurchaseOrder');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (52, 'Utility', 'Purchase Order Approved', '/Utility/PurchaseOrder');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (53, 'Inventory', 'Goods Receiving Note', '/Inventory/Ledger');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (54, 'Inventory', 'Current Stock', '/Inventory/Stock');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (55, 'Inventory', 'Issue to Student', '/Inventory/StudentIssue');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (56, 'Inventory', 'Issue to Department', '/Inventory/IssueInDepartment');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (57, 'Inventory', 'Purchase Order Report', '/Inventory/PurchaseOrderReport');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (58, 'Inventory', 'Goods Receiving Note Report', '/Inventory/GoodsReceivedNoteReport');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (59, 'Inventory', 'Department Wise Report', '/Inventory/DepartmentWiseReport');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (60, 'Inventory', 'Issue Report', '/Inventory/IssueReport');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (61, 'Inventory', 'Add Inventory Opening', '/Inventory/Opening');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (62, 'Inventory', 'Approve Opening Inventory', '/Inventory/OpeningApprove');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (64, 'Account', 'Journal Voucher', '/Account/JournalVoucher');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (65, 'Account', 'Receive Voucher', '/Account/ReceiveVoucher');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (66, 'Account', 'Payment Voucher', '/Account/PaymentVoucher');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (67, 'Account', 'Bank Reconciliation', '/Account/BankReconciliation');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (68, 'Account', 'Approve Pending Voucher', '/Account/PendingVoucher');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (69, 'Account', 'Edit Pending Voucher', '/Account/VoucherEdit');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (70, 'Billing', 'Cash Bill Receipt', '/Billing/CashBill');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (71, 'Account', 'Daily Transaction (Day Book)', '/Account/DailyTransaction');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (72, 'Account', 'Account Ledger', '/Account/AccountLedger');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (73, 'Account', 'Profit & Loss', '/Account/ProfitLoss');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (74, 'Account', 'Balance Sheet', '/Account/BalanceSheet');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (75, 'Account', 'Trial Balance', '/Account/TrailBalance');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (76, 'Account', 'Bank Reconciliation Report', '/Account/BankReconciliationReport');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (77, 'Account', 'Opening Voucher', '/Account/OpeningVoucher');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (79, 'Exam', 'Percentage System', '/Exam/PercentageSystem');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (80, 'Exam', 'Exam Terminal', '/Exam/ExamTerminal');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (81, 'Exam', 'Exam Master ', '/Exam/AddNewExam');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (82, 'Exam', 'Student Registration', '/Exam/ExamStudentRegistration');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (83, 'Exam', 'Approve Registration', '/Exam/ExamStudentRegistrationPending');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (84, 'Exam', 'Entrance Card', '/Exam/EntranceCard');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (85, 'Exam', 'Mark Entry ', '/Exam/MarkEntry');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (86, 'Exam', 'Approve Mark', '/Exam/MarkApprove');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (87, 'Exam', 'Mark Report ', '/Exam/MarkReport');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (88, 'Exam', 'Student Attendance', '/Exam/StudentAttendance');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (89, 'Exam', 'Publish Result', '/Exam/ExamResultPublish');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (90, 'Exam', 'Grade Statement', '/Exam/GradeStatement');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (91, 'Exam', 'Final Grade Statement', '/Exam/FinalGradeStatement');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (92, 'Exam', 'Character Certificate', '/Exam/CharacterCertificate');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (93, 'Exam', 'Mark Update ', '/Exam/MarkUpdate');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (95, 'Library', 'Book Entry ', '/Library/BookStock');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (96, 'Library', 'Book Search ', '/Library/BookSearch');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (97, 'Library', 'Book Report', '/Library/BookReport');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (98, 'Library', 'Issue Book', '/Library/BookIssue');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (99, 'Library', 'Return Book', '/Library/BookReturn');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (100, 'Library', 'Issue Report', '/Library/BookIssueReport');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (102, 'Employee', 'Level Master', '/Employee/LevelMaster');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (103, 'Employee', 'Employee Info', '/Employee/EmployeeInfo');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (104, 'Employee', 'Employee ID Card', '/Employee/IDCard');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (105, 'Employee', 'Online Vacancy', '/Employee/OnlineVacancy');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (106, 'Employee', 'Subject Class Teacher', '/Employee/TeachersClassSubject');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (108, 'Employee', 'Working Hour', '/Employee/EmpWorkingHour');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (109, 'Employee', 'Date Wise Attendance ', '/Employee/WdmsAttendance');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (111, 'Employee', 'Salary Info', '/Employee/EmployeeSalaryInfo');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (112, 'Employee', 'Tax Slab ', '/Employee/TaxSlab');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (113, 'Employee', 'Employee Allowance', '/Employee/RegularAllowance');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (114, 'Employee', 'Leave Application', '/Employee/LeaveApplication');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (115, 'Employee', 'Leave Report', '/Employee/LeaveReport');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (116, 'Employee', 'Monthly Salary', '/Employee/MonthlySalary');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (117, 'Employee', 'Salary Report', '/Employee/SalaryReport');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (119, 'Utility', 'Application User', '/Utility/OrganizationUserInfo');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (120, 'Utility', 'Organization Team', '/Utility/OrganizationTeam');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (121, 'Employee', 'Share Holder', '/Employee/ShareHolder');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (122, 'Utility', 'Calender ', '/Utility/AdBsCalender');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (123, 'Utility', 'Assign New Notice', '/Utility/NoticeBoard');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (124, 'Employee', 'Assign Routine', '/Employee/RoutingReport');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (125, 'Utility', 'Send SMS to Student', '/Utility/ClassWiseSMS');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (126, 'Utility', 'Send Custom SMS', '/Utility/OtherSMS');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (127, 'Utility', 'SMS Report ', '/Utility/SmsReport');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (128, 'Utility', 'Menu Control', '/Utility/MenuControl');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (129, 'Utility', 'Backup ', '/Utility/Backup');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (130, 'Setup', 'Municipal ', '/Setup/MunicipalMaster');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (131, 'Utility', 'Email Notification Service', '/Utility/EmailNotificationService');\n" +
                "INSERT INTO menu_master (id, menu, menu_item, URI) VALUES (132, 'Utility', 'Email Configuration', '/Utility/SenderEmail');\n";
        db.save(sql);
        System.gc();
        sql = "CREATE TABLE day_order_master (`ID` BIGINT NOT NULL, `NAME` VARCHAR(3), PRIMARY KEY (`ID`));";
        db.save(sql);
        sql = "INSERT INTO day_order_master (`ID`, `NAME`) VALUES (1, 'Sun');\n"
                + "INSERT INTO day_order_master (`ID`, `NAME`) VALUES (2, 'Mon');\n"
                + "INSERT INTO day_order_master (`ID`, `NAME`) VALUES (3, 'Tue');\n"
                + "INSERT INTO day_order_master (`ID`, `NAME`) VALUES (4, 'Wed');\n"
                + "INSERT INTO day_order_master (`ID`, `NAME`) VALUES (5, 'Thu');\n"
                + "INSERT INTO day_order_master (`ID`, `NAME`) VALUES (6, 'Fri');\n"
                + "INSERT INTO day_order_master (`ID`, `NAME`) VALUES (7, 'Sat');\n";
        db.save(sql);
        sql = "INSERT INTO grading_system_two (`ID`, `EFFECTIVE_DATE_FROM`, `EFFECTIVE_DATE_TO`, `GPA`, `GRADE`, `RANG_FROM`, `REMARK`) VALUES (1, '2016-04-14', NULL, 4.0, 'A', 90.0, 'Outstanding');\n"
                + "INSERT INTO grading_system_two (`ID`, `EFFECTIVE_DATE_FROM`, `EFFECTIVE_DATE_TO`, `GPA`, `GRADE`, `RANG_FROM`, `REMARK`) VALUES (2, '2016-04-14', NULL, 3.6, 'A-', 80.0, 'Excellent');\n"
                + "INSERT INTO grading_system_two (`ID`, `EFFECTIVE_DATE_FROM`, `EFFECTIVE_DATE_TO`, `GPA`, `GRADE`, `RANG_FROM`, `REMARK`) VALUES (3, '2016-04-14', NULL, 3.2, 'B', 70.0, 'Very Good');\n"
                + "INSERT INTO grading_system_two (`ID`, `EFFECTIVE_DATE_FROM`, `EFFECTIVE_DATE_TO`, `GPA`, `GRADE`, `RANG_FROM`, `REMARK`) VALUES (4, '2016-04-14', NULL, 2.8, 'B-', 60.0, 'Good');\n"
                + "INSERT INTO grading_system_two (`ID`, `EFFECTIVE_DATE_FROM`, `EFFECTIVE_DATE_TO`, `GPA`, `GRADE`, `RANG_FROM`, `REMARK`) VALUES (5, '2016-04-14', NULL, 2.4, 'C', 50.0, 'Above Average');\n"
                + "INSERT INTO grading_system_two (`ID`, `EFFECTIVE_DATE_FROM`, `EFFECTIVE_DATE_TO`, `GPA`, `GRADE`, `RANG_FROM`, `REMARK`) VALUES (6, '2016-04-14', NULL, 2.0, 'C-', 40.0, 'Average');\n"
                + "INSERT INTO grading_system_two (`ID`, `EFFECTIVE_DATE_FROM`, `EFFECTIVE_DATE_TO`, `GPA`, `GRADE`, `RANG_FROM`, `REMARK`) VALUES (7, '2016-04-14', NULL, 1.6, 'D', 25.0, 'Below Average');\n"
                + "INSERT INTO grading_system_two (`ID`, `EFFECTIVE_DATE_FROM`, `EFFECTIVE_DATE_TO`, `GPA`, `GRADE`, `RANG_FROM`, `REMARK`) VALUES (8, '2016-04-14', NULL, 0.8, 'E', 1.0, 'Insufficient');\n"
                + "INSERT INTO grading_system_two (`ID`, `EFFECTIVE_DATE_FROM`, `EFFECTIVE_DATE_TO`, `GPA`, `GRADE`, `RANG_FROM`, `REMARK`) VALUES (9, '2016-04-14', NULL, 0.0, 'F', 0.0, 'Fail');\n";
        db.save(sql);

        sql = "INSERT INTO percentage_system (`ID`, `EFFECTIVE_DATE_FROM`, `EFFECTIVE_DATE_TO`, `GPA`, `GRADE`, `RANG_FROM`, `REMARK`) VALUES (1, '2021-04-14', NULL, 0.0, 'Distraction', 80.0, 'Excellent.');\n"
                + "INSERT INTO percentage_system (`ID`, `EFFECTIVE_DATE_FROM`, `EFFECTIVE_DATE_TO`, `GPA`, `GRADE`, `RANG_FROM`, `REMARK`) VALUES (2, '2021-04-14', NULL, 0.0, 'First', 60.0, 'Very Good');\n"
                + "INSERT INTO percentage_system (`ID`, `EFFECTIVE_DATE_FROM`, `EFFECTIVE_DATE_TO`, `GPA`, `GRADE`, `RANG_FROM`, `REMARK`) VALUES (3, '2021-04-14', NULL, 0.0, 'Secoend', 45.0, 'Good');\n"
                + "INSERT INTO percentage_system (`ID`, `EFFECTIVE_DATE_FROM`, `EFFECTIVE_DATE_TO`, `GPA`, `GRADE`, `RANG_FROM`, `REMARK`) VALUES (4, '2021-04-14', NULL, 0.0, 'Third', 32.0, 'Satisfactory');\n"
                + "INSERT INTO percentage_system (`ID`, `EFFECTIVE_DATE_FROM`, `EFFECTIVE_DATE_TO`, `GPA`, `GRADE`, `RANG_FROM`, `REMARK`) VALUES (5, '2021-04-14', NULL, 0.0, 'Failed', 0.0, '**');";
        db.save(sql);
        sql = "insert into sender_email(id, email, host, password, port) values (1,'edu.link.np@gmail.com','smtp.gmail.com','uagc gfbz uzfw ahkk','587');";
        db.save(sql);
    }

    void function() {
        String sql;
       
        System.gc();
        sql = "SET GLOBAL log_bin_trust_function_creators = 1;";
        db.save(sql);
        sql = "DROP FUNCTION IF EXISTS GET_AD_DATE;";
        db.save(sql);
        sql = "DROP FUNCTION IF EXISTS GET_BS_DATE;";
        db.save(sql);
        sql = "DROP FUNCTION IF EXISTS GET_CR_BALANCE;";
        db.save(sql);
        sql = "DROP FUNCTION IF EXISTS GET_DR_BALANCE;";
        db.save(sql);
        sql = "DROP FUNCTION IF EXISTS GET_DUE_BALANCE;";
        db.save(sql);
        sql = "DROP FUNCTION IF EXISTS GET_OPENING_BALANCE;";
        db.save(sql);
        sql = "DROP FUNCTION IF EXISTS GET_PAID_AMOUNT;";
        db.save(sql);
        sql = "DROP FUNCTION GET_EMPLOYEE_ID;";
        db.save(sql);
        sql = "DROP FUNCTION GET_MALE_FEMALE;";
        db.save(sql);
        sql = " CREATE FUNCTION GET_AD_DATE \n"
                + "(BS VARCHAR(10)) \n"
                + " RETURNS date \n"
                + "BEGIN\n"
                + " DECLARE AD DATE;\n"
                + "SELECT AD_DATE INTO AD FROM ad_bs_calender WHERE BS_DATE=BS;\n"
                + " RETURN AD;\n"
                + "END;";
        db.save(sql);
        System.out.println(sql);
        sql = " CREATE FUNCTION GET_BS_DATE \n"
                + "(AD DATE) \n"
                + " RETURNS varchar(10) CHARSET latin1 \n"
                + "BEGIN\n"
                + " DECLARE BS VARCHAR(10);\n"
                + "SELECT BS_DATE INTO BS FROM ad_bs_calender WHERE AD_DATE=AD;\n"
                + " RETURN BS;       \n"
                + "END  ;";
        db.save(sql);
        System.out.println(sql);
        sql = " CREATE FUNCTION GET_CR_BALANCE \n"
                + "(acCode VARCHAR(30), dateFrom DATE, dateTo DATE) \n"
                + " RETURNS double \n"
                + "BEGIN DECLARE balance DOUBLE;\n"
                + " SELECT SUM(IFNULL(CR_AMT,0)) INTO balance FROM ledger WHERE CONCAT(AC_CODE,'') LIKE CONCAT(acCode,'%') AND ENTER_DATE >=dateFrom AND ENTER_DATE<=dateTo;\n"
                + " RETURN ROUND(IFNULL(balance,0),2);\n"
                + " END; ";
        db.save(sql);
        System.out.println(sql);
        sql = "  CREATE FUNCTION GET_DR_BALANCE \n"
                + "(acCode VARCHAR(30), dateFrom DATE, dateTo DATE) \n"
                + " RETURNS double \n"
                + "BEGIN DECLARE balance DOUBLE; \n"
                + "SELECT SUM(IFNULL(DR_AMT,0)) INTO balance FROM ledger WHERE CONCAT(AC_CODE,'') LIKE CONCAT(acCode,'%') AND ENTER_DATE >=dateFrom AND ENTER_DATE<=dateTo; \n"
                + "RETURN ROUND(IFNULL(balance,0),2);\n"
                + "END ;";
        db.save(sql);
        System.out.println(sql);
        sql = "CREATE FUNCTION GET_DUE_BALANCE \n"
                + "(regNo BIGINT,billType VARCHAR(3),paymentDate DATE) \n"
                + " RETURNS double \n"
                + "BEGIN DECLARE balance DOUBLE; \n"
                + "SELECT SUM(DR) INTO balance FROM stu_billing_detail D,stu_billing_master M WHERE M.BILL_NO=D.BILL_NO AND M.BILL_TYPE=billType AND M.REG_NO=regNo AND D.PAYMENT_DATE<=IFNULL(paymentDate,D.PAYMENT_DATE);\n"
                + "RETURN ROUND(IFNULL(balance,0),2);\n"
                + "END;";

        db.save(sql);
        System.out.println(sql);
        sql = " CREATE FUNCTION GET_OPENING_BALANCE \n"
                + "(acCode VARCHAR(30), dateFrom DATE) \n"
                + " RETURNS double \n"
                + "BEGIN DECLARE balance DOUBLE; \n"
                + "SELECT SUM(IFNULL(DR_AMT,0))-SUM(IFNULL(CR_AMT,0)) INTO balance FROM ledger WHERE CONCAT(AC_CODE,'') LIKE CONCAT(acCode,'%') AND ENTER_DATE < dateFrom; \n"
                + "RETURN ROUND(IFNULL(balance,0),2);\n"
                + "END;";
        db.save(sql);
        System.out.println(sql);
        sql = " CREATE FUNCTION GET_PAID_AMOUNT \n"
                + "(regNo BIGINT,feeId BIGINT,classId BIGINT,academicYear BIGINT) \n"
                + " RETURNS double \n"
                + "BEGIN DECLARE balance DOUBLE; \n"
                + "SELECT SUM(DR) INTO balance FROM stu_billing_detail D,stu_billing_master M \n"
                + "WHERE M.BILL_NO=D.BILL_NO AND M.REG_NO=regNo AND D.BILL_ID=feeId \n"
                + "AND D.CLASS_ID=IFNULL(classId,D.CLASS_ID) AND D.ACADEMIC_YEAR=IFNULL(academicYear,D.ACADEMIC_YEAR);\n"
                + "RETURN ROUND(IFNULL(balance,0),2);\n"
                + "END;";
        db.save(sql);
        System.out.println(sql);
        sql = "CREATE FUNCTION GET_EMPLOYEE_ID\n"
                + "(companyId BIGINT,deviceId BIGINT)\n"
                + "RETURNS BIGINT\n"
                + "BEGIN\n"
                + " DECLARE empId BIGINT;\n"
                + "SELECT id INTO empId FROM employee_info WHERE `BIOMETRIC_COMPANY_ID`=companyId AND `BIOMETRIC_EMP_ID`=deviceId;\n"
                + " RETURN empId;\n"
                + "END;";
        db.save(sql);
        System.out.println(sql);
        sql = "CREATE FUNCTION GET_MALE_FEMALE\n"
                + "(vAcademicYear BIGINT,vProgram BIGINT,vClassId BIGINT,vCast BIGINT)\n"
                + "RETURNS varchar(25) CHARSET latin1\n"
                + "BEGIN\n"
                + "DECLARE M INT;\n"
                + "DECLARE F INT;\n"
                + "SELECT COUNT(ID) INTO M FROM student_info where ACADEMIC_YEAR=IFNULL(vAcademicYear,ACADEMIC_YEAR) AND PROGRAM=IFNULL(vProgram,PROGRAM) AND CLASS_ID=IFNULL(vClassId,CLASS_ID) AND IFNULL(CAST_ETHNICITY,0)=IFNULL(vCast,IFNULL(CAST_ETHNICITY,0)) AND GENDER='M';\n"
                + "SELECT COUNT(ID) INTO F FROM student_info where ACADEMIC_YEAR=IFNULL(vAcademicYear,ACADEMIC_YEAR) AND PROGRAM=IFNULL(vProgram,PROGRAM) AND CLASS_ID=IFNULL(vClassId,CLASS_ID) AND IFNULL(CAST_ETHNICITY,0)=IFNULL(vCast,IFNULL(CAST_ETHNICITY,0)) AND GENDER='F';\n"
                + "RETURN CONCAT(IFNULL(M,0),',',IFNULL(F,0));\n"
                + "END;";
        db.save(sql);
        System.out.println(sql);
        System.gc();
    }


    public Object configureCalender() {
        java.sql.Connection con = null;

        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = java.sql.DriverManager.getConnection("jdbc:mysql://localhost:" + DatabaseName.getPort() + "/" + DatabaseName.getDatabase(), DatabaseName.getUsername(), DatabaseName.getPassword());
            String sql;
            sql = "select ifnull(max(ad_date),'') from ad_bs_calender ";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            rs.next();
            String date = rs.getString(1);
            con.close();
            ps.close();
            rs.close();
            return message.respondWithMessage(date + " till imported!! ");

        } catch (Exception e) {
            System.out.println(e);
            try {
                con.close();
            } catch (Exception ex) {
            }
        }
        return message.respondWithMessage("Database configuration starting");
    }

    @PostMapping("/Calender")
    public Object calender() {

        StringBuilder sql;
       
        Date date = DateConverted.toDate("1943-04-14");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        String adDate, bsDate;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat day = new SimpleDateFormat("EEE");

        try {
            Runtime.getRuntime().gc();
        } catch (Exception ignored) {
        }

        int row = 0;
        sql = new StringBuilder();
        while (true) {
            c.add(Calendar.DATE, 1);
            date = c.getTime();
            adDate = df.format(c.getTime());
            bsDate = DateConverted.adToBs(adDate);
            try {
                if (bsDate.contains("01-01")) {
                    row = row + db.save(sql.toString());
                    sql = new StringBuilder();

                    try {
                        Runtime.getRuntime().gc();
                    } catch (Exception e) {
                    }
                }
                sql.append("\nINSERT INTO ad_bs_calender (AD_DATE,BS_DATE,DAY) VALUES ('").append(adDate).append("', '").append(bsDate).append("', '").append(day.format(date)).append("');");

                if (adDate.equalsIgnoreCase("2034-04-13")) {
                    row = row + db.save(sql.toString());
                    break;
                }
            } catch (Exception e) {
            }
        }
        return message.respondWithMessage(adDate + "Till Created!!");
    }

    @PostMapping("/District")
    public Object districtConfig() {
        new DistrictMunicipalData().setDistrict();
        new DistrictMunicipalData().setMunicipal();
        return message.respondWithMessage("Success");
    }

}
/*



DROP FUNCTION IF EXISTS GET_AD_DATE;
DROP FUNCTION IF EXISTS GET_BS_DATE;
DROP FUNCTION IF EXISTS GET_CR_BALANCE;
DROP FUNCTION IF EXISTS GET_DR_BALANCE;
DROP FUNCTION IF EXISTS GET_DUE_BALANCE;
DROP FUNCTION IF EXISTS GET_OPENING_BALANCE;
DROP FUNCTION IF EXISTS GET_PAID_AMOUNT;

DELIMITER @@ 
 CREATE FUNCTION GET_AD_DATE 
(BS VARCHAR(10)) 
 RETURNS date 
BEGIN
 DECLARE AD DATE;
SELECT AD_DATE INTO AD FROM ad_bs_calender WHERE BS_DATE=BS;
 RETURN AD;       
    END @@ 
 DELIMITER ; 


DELIMITER @@ 
 CREATE FUNCTION GET_BS_DATE 
(AD DATE) 
 RETURNS varchar(10) CHARSET latin1 
BEGIN
 DECLARE BS VARCHAR(10);
SELECT BS_DATE INTO BS FROM ad_bs_calender WHERE AD_DATE=AD;
 RETURN BS;       
END @@ 
 DELIMITER ; 


DELIMITER @@ 
 CREATE FUNCTION GET_CR_BALANCE 
(acCode VARCHAR(30), dateFrom DATE, dateTo DATE) 
 RETURNS double 
BEGIN DECLARE balance DOUBLE;
 SELECT SUM(IFNULL(CR_AMT,0)) INTO balance FROM ledger WHERE CONCAT(AC_CODE,'') LIKE CONCAT(acCode,'%') AND ENTER_DATE >=dateFrom AND ENTER_DATE<=dateTo; RETURN IFNULL(balance,0); END @@ 
 DELIMITER ; 


DELIMITER @@ 
 CREATE FUNCTION GET_DR_BALANCE 
(acCode VARCHAR(30), dateFrom DATE, dateTo DATE) 
 RETURNS double 
BEGIN DECLARE balance DOUBLE; 
SELECT SUM(IFNULL(DR_AMT,0)) INTO balance FROM ledger WHERE CONCAT(AC_CODE,'') LIKE CONCAT(acCode,'%') AND ENTER_DATE >=dateFrom AND ENTER_DATE<=dateTo; RETURN IFNULL(balance,0); END @@ 
 DELIMITER ; 


DELIMITER @@ 
 CREATE FUNCTION GET_DUE_BALANCE 
(regNo BIGINT,billType VARCHAR(3),paymentDate DATE) 
 RETURNS double 
BEGIN DECLARE balance DOUBLE; 
SELECT SUM(DR) INTO balance FROM stu_billing_detail D,stu_billing_master M WHERE M.BILL_NO=D.BILL_NO AND M.BILL_TYPE=billType AND M.REG_NO=regNo AND D.PAYMENT_DATE<=IFNULL(paymentDate,D.PAYMENT_DATE);
RETURN IFNULL(balance,0);
END @@ 
 DELIMITER ; 


DELIMITER @@ 
 CREATE FUNCTION GET_OPENING_BALANCE 
(acCode VARCHAR(30), dateFrom DATE) 
 RETURNS double 
BEGIN DECLARE balance DOUBLE; 
SELECT SUM(IFNULL(DR_AMT,0))-SUM(IFNULL(CR_AMT,0)) INTO balance FROM ledger WHERE CONCAT(AC_CODE,'') LIKE CONCAT(acCode,'%') AND ENTER_DATE < dateFrom; RETURN IFNULL(balance,0); END @@ 
 DELIMITER ; 


DELIMITER @@ 
 CREATE FUNCTION GET_PAID_AMOUNT 
(regNo BIGINT,feeId BIGINT,classId BIGINT,academicYear BIGINT) 
 RETURNS double 
BEGIN DECLARE balance DOUBLE; 
SELECT SUM(DR) INTO balance FROM stu_billing_detail D,stu_billing_master M 
WHERE M.BILL_NO=D.BILL_NO AND M.REG_NO=regNo AND D.BILL_ID=feeId 
AND D.CLASS_ID=IFNULL(classId,D.CLASS_ID) AND D.ACADEMIC_YEAR=IFNULL(academicYear,D.ACADEMIC_YEAR);
RETURN IFNULL(balance,0);
END @@ 
 DELIMITER ; 


 */
