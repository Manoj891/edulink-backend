package com.ms.ware.online.solution.school.controller.swagger.student;



import com.ms.ware.online.solution.school.config.DateConverted;
import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.student.StudentInfoDao;
import com.ms.ware.online.solution.school.dto.RollNumberUpdate;
import com.ms.ware.online.solution.school.model.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ms.ware.online.solution.school.config.Message;

@RestController
@RequestMapping("api/Student/Update")
public class ClassUpdateController {
    @Autowired
    private HibernateUtil util;
    @Autowired
    private StudentInfoDao dao;
    @Autowired
    private AuthenticationFacade facade;
    @Autowired
    private Message message;
    @GetMapping
    public Object index(@RequestParam(required = false) Long subjectGroup, @RequestParam(required = false) Long program, @RequestParam Long classId, @RequestParam Long academicYear, @RequestParam(defaultValue = "") String section) {
        
        AuthenticatedUser td = facade.getAuthentication();;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        if (!section.isEmpty()) {
            section = " and section='" + section + "'";
        }
        String hql = "from StudentInfo where academicYear=" + academicYear + " and program=ifnull(" + program + ",program) and classId=" + classId + " and subjectGroup=ifnull(" + subjectGroup + ",subjectGroup)" + section + " order by stuName,fathersName";
        return ResponseEntity.status(HttpStatus.OK).body(dao.getAll(hql));
    }

    @PostMapping
    public Object update(@RequestBody RollNumberUpdate jsonData) {
        
        AuthenticatedUser td = facade.getAuthentication();;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        String username = td.getUserName();
        String updateDate = DateConverted.now();
        String section = jsonData.getSection();
        Session session = util.getSession();
        Transaction tr = session.beginTransaction();
        try {

            long program = jsonData.getProgram();
            long classId = jsonData.getClassId();
            Long subjectGroup = jsonData.getSubjectGroup();
            long academicYear = jsonData.getAcademicYear();

            jsonData.getObj().forEach(map -> {
                String transferSql;
                long regNo = map.getRegNo();
                int rollNo = map.getRollNo();
                String sql = "SELECT STUDENT_ID FROM class_transfer where STUDENT_ID=" + regNo + " and ACADEMIC_YEAR=" + academicYear + " and CLASS_ID=" + classId;
                if (session.createSQLQuery(sql).setResultTransformer(org.hibernate.Criteria.ALIAS_TO_ENTITY_MAP).list().isEmpty()) {
                    if (subjectGroup == null) {
                        transferSql = "insert into class_transfer(ACADEMIC_YEAR, STUDENT_ID, CLASS_ID, PROGRAM, ROLL_NO, SECTION, SUBJECT_GROUP,update_by,update_date) values (" + academicYear + "," + regNo + "," + classId + "," + program + "," + rollNo + ",'" + section + "',(select SUBJECT_GROUP from student_info SI where id="+regNo+"),'" + username + "','" + updateDate + "')";
                    } else {
                        transferSql = "insert into class_transfer(ACADEMIC_YEAR, STUDENT_ID, CLASS_ID, PROGRAM, ROLL_NO, SECTION, SUBJECT_GROUP,update_by,update_date) values (" + academicYear + "," + regNo + "," + classId + "," + program + "," + rollNo + ",'" + section + "'," + subjectGroup + ",'" + username + "','" + updateDate + "')";
                    }
                } else {
                    transferSql = "update class_transfer set PROGRAM=" + program + ", SECTION='" + section + "', SUBJECT_GROUP=ifnull(" + subjectGroup + ",SUBJECT_GROUP),ROLL_NO=" + rollNo + ",update_by='" + username + "',update_date='" + updateDate + "' where ACADEMIC_YEAR=" + academicYear + " and CLASS_ID=" + classId + " and STUDENT_ID=" + regNo;
                }
                sql = "update student_info set ACADEMIC_YEAR=" + academicYear + ",PROGRAM=" + program + ",CLASS_ID=" + classId + ",SUBJECT_GROUP=ifnull(" + subjectGroup + ",SUBJECT_GROUP),ROLL_NO=" + rollNo + ",SECTION='" + section + "' where id=" + regNo;
                session.createSQLQuery(sql).executeUpdate();
                session.createSQLQuery(transferSql).executeUpdate();
            });

            tr.commit();
        } catch (Exception ex) {
            tr.rollback();
            return message.respondWithError(ex.getMessage());
        }
        return message.respondWithMessage("Success");
    }

    @PutMapping
    public Object sortAndUpdate(@RequestBody RollNumberUpdate jsonData) {
        
        AuthenticatedUser td = facade.getAuthentication();;
        String username = td.getUserName();
        String updateDate = DateConverted.now();
        String section = jsonData.getSection();
        Session session = util.getSession();
        Transaction tr = session.beginTransaction();
        try {

            long program = jsonData.getProgram();
            long classId = jsonData.getClassId();
            long subjectGroup = jsonData.getSubjectGroup();
            long academicYear = jsonData.getAcademicYear();

            jsonData.getObj().forEach(map -> session.createSQLQuery("update student_info set ROLL_NO=NULL where id=" + map.getRegNo()).executeUpdate());

            jsonData.getObj().forEach(map -> {
                String transferSql;
                long regNo = map.getRegNo();
                int rollNo = map.getRollNo();

                String sql = "SELECT STUDENT_ID FROM class_transfer where STUDENT_ID=" + regNo + " and ACADEMIC_YEAR=" + academicYear + " and CLASS_ID=" + classId;
                if (session.createSQLQuery(sql).setResultTransformer(org.hibernate.Criteria.ALIAS_TO_ENTITY_MAP).list().isEmpty()) {
                    transferSql = "insert into class_transfer(ACADEMIC_YEAR, STUDENT_ID, CLASS_ID, PROGRAM, ROLL_NO, SECTION, SUBJECT_GROUP,update_by,update_date) values (" + academicYear + "," + regNo + "," + classId + "," + program + "," + rollNo + ",'" + section + "'," + subjectGroup + ",'" + username + "','" + updateDate + "')";
                } else {
                    transferSql = "update class_transfer set PROGRAM=" + program + ", SECTION='" + section + "', SUBJECT_GROUP=" + subjectGroup + ",ROLL_NO=" + rollNo + ",update_by='" + username + "',update_date='" + updateDate + "' where ACADEMIC_YEAR=" + academicYear + " and CLASS_ID=" + classId + " and STUDENT_ID=" + regNo;
                }
                sql = "update student_info set ACADEMIC_YEAR=" + academicYear + ",PROGRAM=" + program + ",CLASS_ID=" + classId + ",SUBJECT_GROUP=" + subjectGroup + ",ROLL_NO=" + rollNo + ",SECTION='" + section + "' where id=" + regNo;
                session.createSQLQuery(sql).executeUpdate();
                session.createSQLQuery(transferSql).executeUpdate();
            });

            tr.commit();
        } catch (Exception ex) {
            tr.rollback();
            return message.respondWithError(ex.getMessage());
        }
        return message.respondWithMessage("Success");
    }


}
