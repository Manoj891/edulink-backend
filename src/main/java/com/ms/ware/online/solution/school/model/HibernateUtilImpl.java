package com.ms.ware.online.solution.school.model;


import com.ms.ware.online.solution.school.entity.account.*;
import com.ms.ware.online.solution.school.entity.billing.BillingDeleteDetail;
import com.ms.ware.online.solution.school.entity.billing.BillingDeleteMaster;
import com.ms.ware.online.solution.school.entity.billing.StuBillingDetail;
import com.ms.ware.online.solution.school.entity.billing.StuBillingMaster;
import com.ms.ware.online.solution.school.entity.employee.*;
import com.ms.ware.online.solution.school.entity.exam.*;
import com.ms.ware.online.solution.school.entity.inventory.InventoryLedger;
import com.ms.ware.online.solution.school.entity.inventory.PurchaseOrder;
import com.ms.ware.online.solution.school.entity.inventory.PurchaseOrderDetail;
import com.ms.ware.online.solution.school.entity.inventory.SundryCreditors;
import com.ms.ware.online.solution.school.entity.library.BookRemoved;
import com.ms.ware.online.solution.school.entity.library.LibBookIssue;
import com.ms.ware.online.solution.school.entity.library.LibBookStock;
import com.ms.ware.online.solution.school.entity.library.LibBookType;
import com.ms.ware.online.solution.school.entity.setup.*;
import com.ms.ware.online.solution.school.entity.student.*;
import com.ms.ware.online.solution.school.entity.teacherpanel.OnlineClass;
import com.ms.ware.online.solution.school.entity.teacherpanel.TeachersHomework;
import com.ms.ware.online.solution.school.entity.teacherpanel.UploadTeachersVideo;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Service;
import com.ms.ware.online.solution.school.entity.utility.*;

import java.util.Properties;


@Service
public class HibernateUtilImpl implements HibernateUtil{
    private static SessionFactory sessionFactory;

    //CREATE USER 'schoolking'@'localhost' IDENTIFIED BY 'SchoolKing@123';
//GRANT ALL PRIVILEGES ON * . * TO 'schoolking'@'localhost';

    public static void init() {
        String url = "jdbc:mysql://localhost:" + DatabaseName.getPort() + "/" + DatabaseName.getDatabase() + "?allowPublicKeyRetrieval=true&useSSL=false&autoReconnect=true&useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&useSSL=false";
        Properties prop = new Properties();
//        prop.setProperty("hibernate.show_sql", "true");
//        prop.setProperty("hibernate.hbm2ddl.auto","update");
        prop.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
        prop.setProperty("hibernate.connection.url", url);
        prop.setProperty("hibernate.connection.username", DatabaseName.getUsername());
        prop.setProperty("hibernate.connection.password", DatabaseName.getPassword());
        prop.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        prop.setProperty("hibernate.temp.use_jdbc_metadata_defaults", "false");
        prop.setProperty("javax.persistence.validation.mode", "none");
        try {
            sessionFactory = new Configuration()
                    .addProperties(prop)
                    .addAnnotatedClass(OnlineClass.class)
                    .addAnnotatedClass(Routing.class)
                    .addAnnotatedClass(OrganizationTeam.class)
                    .addAnnotatedClass(OnlineAdmission.class)
                    .addAnnotatedClass(AboutApp.class)
                    .addAnnotatedClass(EmployeeAttendance.class)
                    .addAnnotatedClass(MenuMaster.class)
                    .addAnnotatedClass(MenuUserAccess.class)
                    .addAnnotatedClass(EmpLevelMaster.class)
                    .addAnnotatedClass(DepartmentMaster.class)
                    .addAnnotatedClass(CashBill.class)
                    .addAnnotatedClass(CashBillDetail.class)
                    .addAnnotatedClass(CastEthnicityMaster.class)
                    .addAnnotatedClass(AdBsCalender.class)
                    .addAnnotatedClass(BusMaster.class)
                    .addAnnotatedClass(BusStationTime.class)
                    .addAnnotatedClass(ReligionMaster.class)
                    .addAnnotatedClass(OrganizationMaster.class)
                    .addAnnotatedClass(OrganizationUserInfo.class)
                    .addAnnotatedClass(ProgramMaster.class)
                    .addAnnotatedClass(BillMaster.class)
                    .addAnnotatedClass(SubjectMaster.class)
                    .addAnnotatedClass(SubjectGroup.class)
                    .addAnnotatedClass(SubjectGroupDetail.class)
                    .addAnnotatedClass(AcademicYear.class)
                    .addAnnotatedClass(DistrictMaster.class)
                    .addAnnotatedClass(MunicipalMaster.class)
                    .addAnnotatedClass(ClassMaster.class)
                    .addAnnotatedClass(SchoolClassSession.class)
                    .addAnnotatedClass(SchoolClassSessionBillDate.class)
                    .addAnnotatedClass(FeeSetup.class)
                    .addAnnotatedClass(ChartOfAccount.class)
                    .addAnnotatedClass(FiscalYear.class)
                    .addAnnotatedClass(StuBillingMaster.class)
                    .addAnnotatedClass(StuBillingDetail.class)
                    .addAnnotatedClass(PreAdmission.class)
                    .addAnnotatedClass(StudentInfo.class)
                    .addAnnotatedClass(ClassTransfer.class)
                    .addAnnotatedClass(Voucher.class)
                    .addAnnotatedClass(VoucherDetail.class)
                    .addAnnotatedClass(VoucherDelete.class)
                    .addAnnotatedClass(Ledger.class)
                    .addAnnotatedClass(ExamTerminal.class)
                    .addAnnotatedClass(ExamMaster.class)
                    .addAnnotatedClass(GradingSystem.class)
                    .addAnnotatedClass(GradingSystemTwo.class)
                    .addAnnotatedClass(ExamStudentRegistration.class)
                    .addAnnotatedClass(ExamMarkEntry.class)
                    .addAnnotatedClass(ExamResultPublish.class)
                    .addAnnotatedClass(ExamResultPublishSubject.class)
                    .addAnnotatedClass(StudentTransportation.class)
                    .addAnnotatedClass(SchoolHostal.class)
                    .addAnnotatedClass(SundryCreditors.class)
                    .addAnnotatedClass(PurchaseOrder.class)
                    .addAnnotatedClass(PurchaseOrderDetail.class)
                    .addAnnotatedClass(InventoryLedger.class)
                    .addAnnotatedClass(BusStationMaster.class)
                    .addAnnotatedClass(HostalTypeMaster.class)
                    .addAnnotatedClass(NoticeBoard.class)
                    .addAnnotatedClass(StudentAttendance.class)
                    .addAnnotatedClass(TeachersClassSubject.class)
                    .addAnnotatedClass(TeachersHomework.class)
                    .addAnnotatedClass(StudentHomework.class)
                    .addAnnotatedClass(PreviousEducation.class)
                    .addAnnotatedClass(Annex4bMaster.class)
                    .addAnnotatedClass(Annex4bDetail.class)
                    .addAnnotatedClass(LibBookType.class)
                    .addAnnotatedClass(LibBookStock.class)
                    .addAnnotatedClass(LibBookIssue.class)
                    .addAnnotatedClass(BookRemoved.class)
                    .addAnnotatedClass(UploadTeachersVideo.class)
                    .addAnnotatedClass(BillingDeleteMaster.class)
                    .addAnnotatedClass(BillingDeleteDetail.class)
                    .addAnnotatedClass(EmployeeInfo.class)
                    .addAnnotatedClass(OnlineVacancy.class)
                    .addAnnotatedClass(EmpWorkingHour.class)
                    .addAnnotatedClass(TaxSlab.class)
                    .addAnnotatedClass(EmployeeSalaryInfo.class)
                    .addAnnotatedClass(LeaveApplication.class)
                    .addAnnotatedClass(EmpLeaveDetail.class)
                    .addAnnotatedClass(EmpMonthlySalary.class)
                    .addAnnotatedClass(PercentageSystem.class)
                    .addAnnotatedClass(AllowanceMaster.class)
                    .addAnnotatedClass(MonthlyAllowance.class)
                    .addAnnotatedClass(RegularAllowance.class)
                    .addAnnotatedClass(CharacterIssue.class)
                    .addAnnotatedClass(EmailNotificationService.class)
                    .addAnnotatedClass(ShareHolder.class)
                    .addAnnotatedClass(SmsCreditAmount.class)
                    .addAnnotatedClass(SentSms.class)
                    .addAnnotatedClass(SenderEmail.class)
                    .addAnnotatedClass(Notes.class)
                    .addAnnotatedClass(SmsConfiguration.class)
                    .addAnnotatedClass(Section.class)
                    .addAnnotatedClass(BiometricDeviceMap.class)
                    .addAnnotatedClass(BiometricLog.class)
                    .addAnnotatedClass(StudentImport.class)
                    .addAnnotatedClass(CertificateData.class)
                    .addAnnotatedClass(ExamSchedule.class)
                    .buildSessionFactory();
        } catch (HibernateException ignored) {
        }
    }

    public static Session getSession() {
        return getSessionFactory().openSession();
    }


    public static SessionFactory getSessionFactory() {
        try {
            if (sessionFactory == null || sessionFactory.isClosed()) {
                init();
            }
        } catch (Exception e) {
            init();
        }
        return sessionFactory;
    }
}
