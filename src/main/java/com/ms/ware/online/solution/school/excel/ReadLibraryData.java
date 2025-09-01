/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.excel;

import com.ms.ware.online.solution.school.config.DateConverted;
import com.ms.ware.online.solution.school.dao.library.LibBookStockDao;
import com.ms.ware.online.solution.school.dao.library.LibBookStockDaoImp;
import com.ms.ware.online.solution.school.entity.library.LibBookStock;
import com.ms.ware.online.solution.school.entity.setup.SubjectMaster;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ms.ware.online.solution.school.config.DB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author MS
 */
@Service
public class ReadLibraryData {
    @Autowired
    private DB db;
    public List<LibBookStock> bookStocks = new ArrayList<>();
    @Autowired
    private LibBookStockDao da;
    private Map<String, Object> map;
    String sql;
    List<SubjectMaster> subjectMasters = new ArrayList<>();

    public ReadLibraryData() {

        List<Map<String, Object>> l;

        sql = "SELECT ID id,NAME name FROM subject_master ";
        l = db.getRecord(sql);
        for (Map<String, Object> stringObjectMap : l) {
            map = stringObjectMap;
            subjectMasters.add(new SubjectMaster(Long.parseLong(map.get("id").toString()), map.get("name").toString()));
        }
    }

    long matchSubject(String subjectName) {
        if (subjectName.isEmpty()) {
            return 0L;
        }
        for (SubjectMaster subjectMaster : subjectMasters) {
            if (subjectName.contains(subjectMaster.getName())) {
                return subjectMaster.getId();
            }
        }
        return getId("subject_master", subjectName);
    }

    long getId(String tableName, String value) {
        sql = "SELECT ifnull(MAX(ID),0)+1 AS id FROM " + tableName;
        map = db.getRecord(sql).get(0);
        long id = Long.parseLong(map.get("id").toString());
        sql = "INSERT INTO " + tableName + " (ID, NAME) VALUES (" + id + ", '" + value + "');";
        db.save(sql);
        subjectMasters.add(new SubjectMaster(id, value));
        return id;
    }

    public void run(String fileName) {

        String bId, id, purchaseDate, name, publication, author, edition, rackNo;
        long subject, qty, bookId, program, classId;
        double price;
        int totalColumn = 13, pages;
        LibBookStock bookStock;
        Object[][] data = new Excel().read(fileName, totalColumn, 0);
        String sql = "DELETE FROM lib_book_stock";
        db.delete(sql);
        for (Object[] datum : data) {
            try {
                bookId = Integer.parseInt(datum[0].toString());
                program = Long.parseLong(datum[1].toString());
                classId = Long.parseLong(datum[2].toString());
                subject = matchSubject(datum[3].toString());
                purchaseDate = datum[4].toString();
                name = datum[5].toString();
                publication = datum[7].toString();
                author = datum[8].toString();
                edition = datum[9].toString();
                try {
                    pages = Integer.parseInt(datum[10].toString());
                } catch (Exception e) {
                    pages = 0;
                }
                try {
                    price = Double.parseDouble(datum[11].toString());
                } catch (Exception e) {
                    price = 0;
                }
                rackNo = datum[12].toString();

                try {
                    qty = Integer.parseInt(datum[6].toString());
                    if (qty == 0) {
                        qty = 1;
                    }
                } catch (Exception e) {
                    qty = 1;
                }

                if (bookId < 10) {
                    bId = "B" + bookId + "0000";
                } else if (bookId < 100) {
                    bId = "B" + bookId + "000";
                } else if (bookId < 1000) {
                    bId = "B" + bookId + "00";
                } else if (bookId < 10000) {
                    bId = "B" + bookId + "0";
                } else {
                    bId = "B" + bookId;
                }
                for (int k = 1; k <= qty; k++) {
                    if (k < 10) {
                        id = bId + "000" + k;
                    } else if (k < 100) {
                        id = bId + "00" + k;
                    } else if (k < 1000) {
                        id = bId + "0" + k;
                    } else {
                        id = bId + "" + k;
                    }
                    bookStock = new LibBookStock();
                    bookStock.setId(id);
                    bookStock.setBookId(bookId);
                    bookStock.setBookSn(k);
                    bookStock.setIsbn(bookId + "");
                    bookStock.setAuthor(author);
                    bookStock.setBookType(1L);
                    bookStock.setClassId(classId);
                    bookStock.setProgram(program);
                    bookStock.setLanguage("English");
                    bookStock.setQuantity(qty);
                    bookStock.setEdition(edition);
                    bookStock.setName(name);
                    bookStock.setPages(pages);
                    bookStock.setSubject(subject);
                    bookStock.setRackNo(rackNo);
                    bookStock.setPurchaseDate(DateConverted.bsToAdDate(purchaseDate));
                    bookStock.setPublication(publication);
                    bookStock.setPrice(price);
//                 System.out.println(bookStock);
                    if (da.save(bookStock) == 1) {
                        bookStocks.add(bookStock);
                    }

                }

            } catch (Exception ignored) {
            }
        }
    }
}
