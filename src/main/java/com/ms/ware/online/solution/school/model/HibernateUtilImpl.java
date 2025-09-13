package com.ms.ware.online.solution.school.model;


import com.ms.ware.online.solution.school.entity.account.*;
import com.ms.ware.online.solution.school.entity.billing.*;
import com.ms.ware.online.solution.school.entity.employee.*;
import com.ms.ware.online.solution.school.entity.exam.*;
import com.ms.ware.online.solution.school.entity.inventory.*;
import com.ms.ware.online.solution.school.entity.library.*;
import com.ms.ware.online.solution.school.entity.setup.*;
import com.ms.ware.online.solution.school.entity.student.*;
import com.ms.ware.online.solution.school.entity.teacherpanel.*;
import com.ms.ware.online.solution.school.entity.utility.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Service;

import java.util.Properties;


@Service
public class HibernateUtilImpl implements HibernateUtil {
    private SessionFactory sessionFactory;


    //CREATE USER 'schoolking'@'localhost' IDENTIFIED BY 'SchoolKing@123';
//GRANT ALL PRIVILEGES ON * . * TO 'schoolking'@'localhost';
    @Override
    public synchronized void init() {
        try {
            if (sessionFactory != null && !sessionFactory.isClosed()) {
                return;
            }
        } catch (Exception ignored) {
        }
        String url = "jdbc:mysql://localhost:" + DatabaseName.getPort() + "/" + DatabaseName.getDatabase() + "?allowPublicKeyRetrieval=true&useSSL=false&autoReconnect=true&useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true";
        Properties prop = new Properties();
        prop.setProperty("hibernate.show_sql", "true");
        prop.setProperty("hibernate.format_sql", "false");
        prop.setProperty("hibernate.hbm2ddl.auto", "update");
        prop.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
        prop.setProperty("hibernate.connection.url", url);
        prop.setProperty("hibernate.connection.username", DatabaseName.getUsername());
        prop.setProperty("hibernate.connection.password", DatabaseName.getPassword());
        prop.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        prop.setProperty("hibernate.temp.use_jdbc_metadata_defaults", "true");
        prop.setProperty("javax.persistence.validation.mode", "none");
        final Configuration configuration = new Configuration();
        configuration.addProperties(prop);
        // ✅ Register all annotated classes here
        configuration.addAnnotatedClass(OnlineClass.class);
        configuration.addAnnotatedClass(Routing.class);
        configuration.addAnnotatedClass(OrganizationTeam.class);
        configuration.addAnnotatedClass(OnlineAdmission.class);
        configuration.addAnnotatedClass(AboutApp.class);
        configuration.addAnnotatedClass(EmployeeAttendance.class);
        configuration.addAnnotatedClass(MenuMaster.class);
        configuration.addAnnotatedClass(MenuUserAccess.class);
        configuration.addAnnotatedClass(EmpLevelMaster.class);
        configuration.addAnnotatedClass(DepartmentMaster.class);
        configuration.addAnnotatedClass(CashBill.class);
        configuration.addAnnotatedClass(CashBillDetail.class);
        configuration.addAnnotatedClass(CastEthnicityMaster.class);
        configuration.addAnnotatedClass(AdBsCalender.class);
        configuration.addAnnotatedClass(BusMaster.class);
        configuration.addAnnotatedClass(BusStationTime.class);
        configuration.addAnnotatedClass(ReligionMaster.class);
        configuration.addAnnotatedClass(OrganizationMaster.class);
        configuration.addAnnotatedClass(OrganizationUserInfo.class);
        configuration.addAnnotatedClass(ProgramMaster.class);
        configuration.addAnnotatedClass(BillMaster.class);
        configuration.addAnnotatedClass(SubjectMaster.class);
        configuration.addAnnotatedClass(SubjectGroup.class);
        configuration.addAnnotatedClass(SubjectGroupDetail.class);
        configuration.addAnnotatedClass(AcademicYear.class);
        configuration.addAnnotatedClass(DistrictMaster.class);
        configuration.addAnnotatedClass(MunicipalMaster.class);
        configuration.addAnnotatedClass(ClassMaster.class);
        configuration.addAnnotatedClass(SchoolClassSession.class);
        configuration.addAnnotatedClass(SchoolClassSessionBillDate.class);
        configuration.addAnnotatedClass(FeeSetup.class);
        configuration.addAnnotatedClass(ChartOfAccount.class);
        configuration.addAnnotatedClass(FiscalYear.class);
        configuration.addAnnotatedClass(StuBillingMaster.class);
        configuration.addAnnotatedClass(StuBillingDetail.class);
        configuration.addAnnotatedClass(PreAdmission.class);
        configuration.addAnnotatedClass(StudentInfo.class);
        configuration.addAnnotatedClass(ClassTransfer.class);
        configuration.addAnnotatedClass(Voucher.class);
        configuration.addAnnotatedClass(VoucherDetail.class);
        configuration.addAnnotatedClass(VoucherDelete.class);
        configuration.addAnnotatedClass(Ledger.class);
        configuration.addAnnotatedClass(ExamTerminal.class);
        configuration.addAnnotatedClass(ExamMaster.class);
        configuration.addAnnotatedClass(GradingSystem.class);
        configuration.addAnnotatedClass(GradingSystemTwo.class);
        configuration.addAnnotatedClass(ExamStudentRegistration.class);
        configuration.addAnnotatedClass(ExamMarkEntry.class);
        configuration.addAnnotatedClass(ExamResultPublish.class);
        configuration.addAnnotatedClass(ExamResultPublishSubject.class);
        configuration.addAnnotatedClass(StudentTransportation.class);
        configuration.addAnnotatedClass(SchoolHostal.class);
        configuration.addAnnotatedClass(SundryCreditors.class);
        configuration.addAnnotatedClass(PurchaseOrder.class);
        configuration.addAnnotatedClass(PurchaseOrderDetail.class);
        configuration.addAnnotatedClass(InventoryLedger.class);
        configuration.addAnnotatedClass(BusStationMaster.class);
        configuration.addAnnotatedClass(HostalTypeMaster.class);
        configuration.addAnnotatedClass(NoticeBoard.class);
        configuration.addAnnotatedClass(StudentAttendance.class);
        configuration.addAnnotatedClass(TeachersClassSubject.class);
        configuration.addAnnotatedClass(TeachersHomework.class);
        configuration.addAnnotatedClass(StudentHomework.class);
        configuration.addAnnotatedClass(PreviousEducation.class);
        configuration.addAnnotatedClass(Annex4bMaster.class);
        configuration.addAnnotatedClass(Annex4bDetail.class);
        configuration.addAnnotatedClass(LibBookType.class);
        configuration.addAnnotatedClass(LibBookStock.class);
        configuration.addAnnotatedClass(LibBookIssue.class);
        configuration.addAnnotatedClass(BookRemoved.class);
        configuration.addAnnotatedClass(UploadTeachersVideo.class);
        configuration.addAnnotatedClass(BillingDeleteMaster.class);
        configuration.addAnnotatedClass(BillingDeleteDetail.class);
        configuration.addAnnotatedClass(EmployeeInfo.class);
        configuration.addAnnotatedClass(OnlineVacancy.class);
        configuration.addAnnotatedClass(EmpWorkingHour.class);
        configuration.addAnnotatedClass(TaxSlab.class);
        configuration.addAnnotatedClass(EmployeeSalaryInfo.class);
        configuration.addAnnotatedClass(LeaveApplication.class);
        configuration.addAnnotatedClass(EmpLeaveDetail.class);
        configuration.addAnnotatedClass(EmpMonthlySalary.class);
        configuration.addAnnotatedClass(PercentageSystem.class);
        configuration.addAnnotatedClass(AllowanceMaster.class);
        configuration.addAnnotatedClass(MonthlyAllowance.class);
        configuration.addAnnotatedClass(RegularAllowance.class);
        configuration.addAnnotatedClass(CharacterIssue.class);
        configuration.addAnnotatedClass(EmailNotificationService.class);
        configuration.addAnnotatedClass(ShareHolder.class);
        configuration.addAnnotatedClass(SmsCreditAmount.class);
        configuration.addAnnotatedClass(SentSms.class);
        configuration.addAnnotatedClass(SenderEmail.class);
        configuration.addAnnotatedClass(Notes.class);
        configuration.addAnnotatedClass(SmsConfiguration.class);
        configuration.addAnnotatedClass(Section.class);
        configuration.addAnnotatedClass(BiometricDeviceMap.class);
        configuration.addAnnotatedClass(BiometricLog.class);
        configuration.addAnnotatedClass(StudentImport.class);
        configuration.addAnnotatedClass(CertificateData.class);
        configuration.addAnnotatedClass(ExamSchedule.class);

        // ✅ Build session factory
        sessionFactory = configuration.buildSessionFactory();
    }

    @Override
    public Session getSession() {
        if (sessionFactory == null || sessionFactory.isClosed()) {
            init(); // lazy init in case it's not called before
        }
        return sessionFactory.openSession();
    }
}
