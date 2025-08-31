/*    map = new com.fasterxml.jackson.databind.ObjectMapper().readValue(jsonData, new com.fasterxml.jackson.core.type.TypeReference<>() {});
 */
package com.ms.ware.online.solution.school.service.library;


import com.ms.ware.online.solution.school.config.DateConverted;
import com.ms.ware.online.solution.school.config.JsonStringConverter;
import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.library.LibBookStockDao;
import com.ms.ware.online.solution.school.dto.LibBookStockRes;
import com.ms.ware.online.solution.school.entity.library.BookRemoved;
import com.ms.ware.online.solution.school.entity.library.LibBookStock;
import com.ms.ware.online.solution.school.exception.CustomException;
import com.ms.ware.online.solution.school.model.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class LibBookStockServiceImp implements LibBookStockService {

    @Autowired
    private LibBookStockDao da;
    @Autowired
    private AuthenticationFacade facade;
    @Autowired
    private JsonStringConverter json;

    @Override
    public List<Map<String, Object>> bookStock() {
        return da.getRecord("select p.name as program, c.name class, s.name as subject, t.name as type, sum(quantity) as quantity from lib_book_stock b join program_master p on b.program = p.id join class_master c on c.id = class_id  join subject_master s on subject = s.id join lib_book_type t on book_type = t.id group by program, class_id, subject, book_type order by program,class,subject");
    }

    @Override
    public List<Map<String, Object>> getAll(String bookId, String dateFrom, String dateTo, Long program, Long classId, Long subject, String bookName) {
        String purchaseDate = "";
        String limit = " order by purchaseId desc limit 50 ";
        if (dateFrom.length() == 10 && dateTo.length() == 10) {
            purchaseDate = " AND PURCHASE_DATE BETWEEN '" + DateConverted.toString(DateConverted.bsToAdDate(dateFrom)) + "' AND '" + DateConverted.toString(DateConverted.bsToAdDate(dateTo)) + "'";
            limit = "";
        } else if (dateFrom.length() == 10) {
            purchaseDate = " AND PURCHASE_DATE = '" + DateConverted.toString(DateConverted.bsToAdDate(dateFrom)) + "'";
            limit = "";
        }else if (dateTo.length() == 10) {
            purchaseDate = " AND PURCHASE_DATE = '" + DateConverted.toString(DateConverted.bsToAdDate(dateTo)) + "'";
            limit = "";
        }
        try {
            if (!bookName.isEmpty()) {
                purchaseDate = " AND B.NAME LIKE '%" + bookName + "%' ";
                limit = "";
            }
        } catch (Exception ignored) {
        }
        try {
            if (!bookId.isEmpty()) {
                purchaseDate = " AND B.ID='" + bookId + "' ";
                limit = "";
            }
        } catch (Exception ignored) {
        }
        if (program != null && classId != null) limit = "";
        return da.getRecord( "SELECT B.BOOK_ID as purchaseId, P.ID as program, C.ID as classId, S.ID as subject, T.ID bookType, P.NAME AS programName, C.NAME as className, S.NAME as subjectName, T.NAME as bookTypeName, B.NAME as bookName, sum(B.QUANTITY) as quantity, MAX(B.AUTHOR) as author, MAX(B.PUBLICATION) as publication, MAX(B.LANGUAGE) as language,max(ISBN) isbn, MAX(B.EDITION) as edition, MAX(B.PAGES) as page, MAX(B.RACK_NO) as rackNo, MAX(B.PRICE) as price, max(B.PURCHASE_DATE) purchaseDate FROM lib_book_stock B join lib_book_type T on B.BOOK_TYPE = T.ID join program_master P on P.ID = B.PROGRAM join class_master C on C.ID = B.CLASS_ID join subject_master S on S.ID = B.SUBJECT WHERE B.PROGRAM = IFNULL(" + program + ", B.PROGRAM) AND B.CLASS_ID = IFNULL(" + classId + ", B.CLASS_ID) AND B.SUBJECT = IFNULL(" + subject + ", B.SUBJECT) " + purchaseDate + " GROUP BY B.BOOK_ID, P.ID,C.ID,S.ID,T.ID,P.NAME,C.NAME,S.NAME,T.NAME,B.NAME " + limit);

    }

    @Override
    public List<Map<String, Object>> bookReport(String dateFrom, String dateTo) {
        String sql = "SELECT B.BOOK_ID purchaseId, IFNULL(max(P.NAME), '') AS program, IFNULL(max(C.NAME), '') className, IFNULL(max(S.NAME), '') subject, count(B.ID) quantity, max(T.NAME) bookType, max(B.NAME) bookName, max(B.AUTHOR) author, max(PUBLICATION) publication, max(B.LANGUAGE) language, max(EDITION) edition, max(PAGES) page, max(RACK_NO) rackNo, max(PRICE) price, GET_BS_DATE(max(PURCHASE_DATE)) purchaseDate FROM lib_book_stock B join lib_book_type T on B.BOOK_TYPE = T.ID left join program_master P on P.ID = B.PROGRAM left join class_master C on C.ID = B.CLASS_ID left join subject_master S on S.ID = B.SUBJECT WHERE PURCHASE_DATE BETWEEN '" + DateConverted.bsToAd(dateFrom) + "' AND '" + DateConverted.bsToAd(dateTo) + "' GROUP BY BOOK_ID order by purchaseId desc";
        return da.getRecord(sql);
    }


    @Override
    public Object indexEdit(String purchaseId) {
        List l = da.getAll("from LibBookStock where bookId='" + purchaseId + "' OR id='" + purchaseId + "'");
        Message message = new Message();
        if (l.isEmpty()) {
            return message.respondWithError("Record not found!");
        }
        return l.get(0);
    }

    @Override
    public ResponseEntity getAll(String bookId) {
        if (bookId.contains("B")) {
            return ResponseEntity.status(200).body(da.getAll("from LibBookStock where  id='" + bookId + "'"));
        }
        return ResponseEntity.status(200).body(da.getAll("from LibBookStock where bookId='" + bookId + "'"));
    }

    private LibBookStock convert(LibBookStockRes res) {
        return LibBookStock.builder().isbn(res.getIsbn()).program(res.getProgram()).classId(res.getClassId()).bookType(res.getBookType()).subject(res.getSubject()).quantity(res.getQuantity()).name(res.getName()).publication(res.getPublication()).language(res.getLanguage()).author(res.getAuthor()).edition(res.getEdition()).pages(res.getPages()).price(res.getPrice()).rackNo(res.getRackNo()).vendor(res.getVendor()).purchaseDate(DateConverted.bsToAdDate(res.getPurchaseDate())).build();
    }

    @Override
    public String save(LibBookStockRes req) {
        Message message = new Message();
        LibBookStock obj = convert(req);
        int row = 0;
        String msg = "", sql, id, bId;
        long bookId;
        try {

            sql = "SELECT ifnull(MAX(BOOK_ID),0)+1 AS id FROM lib_book_stock";
            message.map = da.getRecord(sql).get(0);
            bookId = Long.parseLong(message.map.get("id").toString());
            if (bookId < 10) {
                bId = bookId + "B0000";
            } else if (bookId < 100) {
                bId = bookId + "B000";
            } else if (bookId < 1000) {
                bId = bookId + "B00";
            } else if (bookId < 10000) {
                bId = bookId + "B0";
            } else {
                bId = bookId + "B";
            }

            for (int i = 1; i <= obj.getQuantity(); i++) {
                if (i < 10) {
                    id = bId + "000" + i;
                } else if (i < 100) {
                    id = bId + "00" + i;
                } else if (i < 1000) {
                    id = bId + "0" + i;
                } else {
                    id = bId + i;
                }
                obj.setId(id);
                obj.setBookId(bookId);
                obj.setBookSn(i);
                obj.setIsbn(obj.getId());
                row = row + da.save(obj);
                msg = da.getMsg();
            }

            if (row > 0) {
                return "{\"message\":\"Success\",\"id\":\"" + bookId + "\"}";
            } else if (msg.contains("Duplicate entry")) {
                msg = "This record already exist";
            }
            throw new CustomException(msg);

        } catch (Exception e) {
            throw new CustomException(msg);
        }
    }

    @Override
    public String update(LibBookStockRes req) {
        LibBookStock obj = convert(req);
        int row = 1;
        String msg = "";
        String id, bId;
        long bookId = req.getBookId();
        long quantity = obj.getQuantity();

        if (bookId < 10) {
            bId = bookId + "B0000";
        } else if (bookId < 100) {
            bId = bookId + "B000";
        } else if (bookId < 1000) {
            bId = bookId + "B00";
        } else if (bookId < 10000) {
            bId = bookId + "B0";
        } else {
            bId = bookId + "B";
        }
        for (int i = 1; i <= quantity; i++) {
            if (i < 10) {
                id = bId + "000" + i;
            } else if (i < 100) {
                id = bId + "00" + i;
            } else if (i < 1000) {
                id = bId + "0" + i;
            } else {
                id = bId + i;
            }
            obj.setId(id);
            obj.setBookId(bookId);
            obj.setBookSn(i);
            System.out.println(obj);
            row = row + da.update(obj);
            msg = da.getMsg();
        }

        if (row > 0) {
            if (bookId != 0) {
                String sql = "DELETE FROM lib_book_stock WHERE BOOK_ID=" + bookId + " AND BOOK_SN>" + quantity;
                da.delete(sql);
            }
            return "{\"message\":\"Success\",\"id\":\"" + bookId + "\"}";
        } else if (msg.contains("Duplicate entry")) {
            msg = "This record already exist";
        } else if (msg.contains("foreign key")) {
            msg = "this record already used in reference tables, Cannot delete of this record";
        }
        throw new CustomException(msg);
    }

    @Override
    public void delete(long id) {
        AuthenticatedUser td = facade.getAuthentication();;
        int row;
        String msg, sql;
        Session session = HibernateUtil.getSession();
        Transaction tr = session.beginTransaction();
        session.save(BookRemoved.builder()
                .id(UUID.randomUUID().toString())
                .data(json.jsonString(da.getAll("from LibBookStock where bookId=" + id)))
                .removedBy(td.getUserName())
                .removedDate(DateConverted.now())
                .build());

        sql = "delete from lib_book_stock where book_id=" + id;
        row = session.createSQLQuery(sql).executeUpdate();
        tr.commit();

        msg = da.getMsg();
        if (row > 0) {
            return;
        } else if (msg.contains("foreign key")) {
            msg = "this record already used in reference tables, Cannot delete of this record";
        }
        tr.rollback();
        throw new CustomException(msg);
    }

}
