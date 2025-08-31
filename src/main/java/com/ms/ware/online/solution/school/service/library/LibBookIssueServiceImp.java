/*    map = new com.fasterxml.jackson.databind.ObjectMapper().readValue(jsonData, new com.fasterxml.jackson.core.type.TypeReference<>() {});
 */
package com.ms.ware.online.solution.school.service.library;

import com.ms.ware.online.solution.school.config.DateConverted;
import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.library.LibBookIssueDao;
import com.ms.ware.online.solution.school.dto.BookIssueReq;
import com.ms.ware.online.solution.school.entity.library.LibBookIssue;
import com.ms.ware.online.solution.school.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class LibBookIssueServiceImp implements LibBookIssueService {
    @Autowired
    private AuthenticationFacade facade;
    @Autowired
    private LibBookIssueDao da;

    @Override
    public List<Map<String, Object>> getAll(String bookId) {
        return da.getRecord("SELECT B.ID bookId,IFNULL((SELECT P.NAME  FROM program_master P WHERE P.ID=B.PROGRAM),'') AS program,IFNULL((SELECT P.NAME  FROM class_master P WHERE P.ID=B.CLASS_ID),'') className,IFNULL((SELECT P.NAME  FROM subject_master P WHERE P.ID=B.SUBJECT),'') subject,QUANTITY quantity,T.NAME bookType,B.NAME bookName,B.AUTHOR author,PUBLICATION publication,B.LANGUAGE language,EDITION edition,PAGES page,RACK_NO rackNo,PRICE price,GET_BS_DATE(PURCHASE_DATE) purchaseDate  FROM lib_book_stock B,lib_book_type T WHERE B.BOOK_TYPE=T.ID AND (B.ID='" + bookId + "' OR B.ISBN='" + bookId + "') AND B.ID NOT IN(SELECT BOOK_ISSUE_ID FROM lib_book_issue I,lib_book_stock S WHERE I.BOOK_ID=S.ID AND BOOK_ISSUE_ID IS NOT NULL)");

    }

    @Override
    public String save(BookIssueReq req) {
        Message message = new Message();
        String username = facade.getAuthentication().getUserName();
        try {


            String issueFor = req.getIssueFor();
            Long regNo = null, staffId = null;
            if (issueFor.equalsIgnoreCase("1")) {
                regNo = Long.parseLong(req.getRegNo());
            } else if (issueFor.equalsIgnoreCase("2")) {
                staffId = Long.parseLong(req.getStaffId());
            } else {
                throw new CustomException("Please provide Student/Staff");
            }
            Date issueDate = DateConverted.bsToAdDate(req.getIssueDate());
            int issueForDay = Integer.parseInt(req.getIssueForDay());

            String msg = "", sql;


            int row = 0;
            LibBookIssue obj;
            for (String bookId : req.getBookIds()) {
                sql = "SELECT ifnull(MAX(ID),0)+1 AS id FROM lib_book_issue";
                message.map = da.getRecord(sql).get(0);
                obj = LibBookIssue.builder()
                        .bookIssueId(bookId)
                        .bookId(bookId)
                        .id(Long.parseLong(message.map.get("id").toString()))
                        .issueDate(issueDate)
                        .issueBy(username)
                        .issueForDay(issueForDay)
                        .staffId(staffId)
                        .stuId(regNo)
                        .build();

                row += da.save(obj);
                msg = da.getMsg();
            }
            if (row > 0) {
                return message.respondWithMessage("Success");
            } else if (msg.contains("Duplicate entry")) {
                msg = "This record already exist";
            }
            throw new CustomException(msg);

        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
    }

    @Override
    public String update(String date, List<String> bookIds) {
        Message message = new Message();
        int row = 0;
        String msg = "", sql, userName = facade.getAuthentication().getUserName();;
        try {
            String receiveDate = DateConverted.bsToAd(date);
            for (String bookId : bookIds) {
                sql = "UPDATE lib_book_issue SET BOOK_ISSUE_ID=null,`RETURN_BY`='" + userName + "',`RETURN_DATE`='" + receiveDate + "' WHERE `BOOK_ISSUE_ID`='" + bookId + "'";
                row += da.delete(sql);
                msg = da.getMsg();
            }
            if (row > 0) {
                return message.respondWithMessage("Success");
            } else if (msg.contains("Duplicate entry")) {
                msg = "This record already exist";
            } else if (msg.contains("foreign key")) {
                msg = "this record already used in reference tables, Cannot delete of this record";
            }
        } catch (Exception ignored) {
        }
        throw new CustomException(msg);
    }

    @Override
    public String delete(String id) {
        Message message = new Message();
        int row;
        String msg, sql;
        sql = "DELETE FROM lib_book_issue WHERE ID='" + id + "'";
        row = da.delete(sql);
        msg = da.getMsg();
        if (row > 0) {
            return message.respondWithMessage("Success");
        } else if (msg.contains("foreign key")) {
            msg = "this record already used in reference tables, Cannot delete of this record";
        }
        throw new CustomException(msg);
    }

    @Override
    public List<Map<String, Object>> findBookReturn(String bookId) {
        String sql = "SELECT B.ID bookId,ISSUE_DATE issueDate,ISSUE_BY issueBy,IFNULL((SELECT P.NAME  FROM program_master P WHERE P.ID=B.PROGRAM),'') AS program,IFNULL((SELECT P.NAME  FROM class_master P WHERE P.ID=B.CLASS_ID),'') className,IFNULL((SELECT P.NAME  FROM subject_master P WHERE P.ID=B.SUBJECT),'') subject,T.NAME bookType,B.NAME bookName,B.AUTHOR author,PUBLICATION publication,B.LANGUAGE language,EDITION edition,STU_NAME borrow  FROM lib_book_issue I,lib_book_stock B,student_info S,lib_book_type T WHERE B.BOOK_TYPE=T.ID AND I.BOOK_ISSUE_ID=B.ID AND I.STU_ID=S.ID AND B.`ID`='" + bookId + "'"
                + " \nUNION\n"
                + " SELECT B.ID bookId,ISSUE_DATE issueDate,ISSUE_BY issueBy,IFNULL((SELECT P.NAME  FROM program_master P WHERE P.ID=B.PROGRAM),'') AS program,IFNULL((SELECT P.NAME  FROM class_master P WHERE P.ID=B.CLASS_ID),'') className,IFNULL((SELECT P.NAME  FROM subject_master P WHERE P.ID=B.SUBJECT),'') subject,T.NAME bookType,B.NAME bookName,B.AUTHOR author,PUBLICATION publication,B.LANGUAGE language,EDITION edition,CONCAT(first_name,' ',last_name) borrow  FROM lib_book_issue I,lib_book_stock B,employee_info S,lib_book_type T WHERE B.BOOK_TYPE=T.ID AND I.BOOK_ISSUE_ID=B.ID AND I.`STAFF_ID`=S.ID AND B.`ID`='" + bookId + "'";
        return da.getRecord(sql);
    }

    @Override
    public List<Map<String, Object>> bookIssueReport() {
        String sql = "SELECT B.ID bookId,ISSUE_DATE issueDate,ISSUE_BY issueBy,IFNULL((SELECT P.NAME  FROM program_master P WHERE P.ID=B.PROGRAM),'') AS program,IFNULL((SELECT P.NAME  FROM class_master P WHERE P.ID=B.CLASS_ID),'') className,IFNULL((SELECT P.NAME  FROM subject_master P WHERE P.ID=B.SUBJECT),'') subject,T.NAME bookType,B.NAME bookName,B.AUTHOR author,PUBLICATION publication,B.LANGUAGE language,EDITION edition,STU_NAME borrow  FROM lib_book_issue I,lib_book_stock B,student_info S,lib_book_type T WHERE B.BOOK_TYPE=T.ID AND I.BOOK_ISSUE_ID=B.ID AND I.STU_ID=S.ID AND RETURN_DATE IS NULL "
                + "UNION\n"
                + " SELECT B.ID bookId,ISSUE_DATE issueDate,ISSUE_BY issueBy,IFNULL((SELECT P.NAME  FROM program_master P WHERE P.ID=B.PROGRAM),'') AS program,IFNULL((SELECT P.NAME  FROM class_master P WHERE P.ID=B.CLASS_ID),'') className,IFNULL((SELECT P.NAME  FROM subject_master P WHERE P.ID=B.SUBJECT),'') subject,T.NAME bookType,B.NAME bookName,B.AUTHOR author,PUBLICATION publication,B.LANGUAGE language,EDITION edition,CONCAT(first_name,' ',last_name) borrow  FROM lib_book_issue I,lib_book_stock B,employee_info S,lib_book_type T WHERE B.BOOK_TYPE=T.ID AND I.BOOK_ISSUE_ID=B.ID AND I.`STAFF_ID`=S.ID AND RETURN_DATE IS NULL";
        return da.getRecord(sql);
    }
}
