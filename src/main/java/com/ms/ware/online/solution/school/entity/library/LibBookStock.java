package com.ms.ware.online.solution.school.entity.library;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "lib_book_stock", uniqueConstraints = @UniqueConstraint(columnNames = {"BOOK_ID", "BOOK_SN"}, name = "BOOK_ID"))
public class LibBookStock implements java.io.Serializable {


    @Id
    @Column(name = "ID", columnDefinition = "VARCHAR(13)")
    private String id;
    @Column(name = "ISBN", columnDefinition = "VARCHAR(100)")
    private String isbn;
    @Column(name = "BOOK_ID")
    private Long bookId;
    @Column(name = "BOOK_SN")
    private Integer bookSn;
    @Column(name = "PROGRAM")
    private Long program;
    @Column(name = "CLASS_ID")
    private Long classId;
    @Column(name = "BOOK_TYPE")
    private Long bookType;
    @Column(name = "SUBJECT")
    private Long subject;
    @Column(name = "QUANTITY")
    private Long quantity;
    @Column(name = "NAME", columnDefinition = "TEXT CHARACTER SET utf8 COLLATE utf8_general_ci")
    private String name;
    @Column(name = "PUBLICATION")
    private String publication;
    @Column(name = "LANGUAGE")
    private String language;
    @Column(name = "AUTHOR")
    private String author;
    @Column(name = "EDITION")
    private String edition;
    @Column(name = "PAGES")
    private Integer pages;
    @Column(name = "PRICE")
    private Double price;
    @Column(name = "rack_no")
    private String rackNo;
    @Column(name = "vendor")
    private String vendor;
    @Column(name = "purchase_date")
    @Temporal(TemporalType.DATE)
    private Date purchaseDate;


    @Override
    public String toString() {
        return "\n{\"id\": \"" + id + "\",\"bookId\": \"" + bookId + "\",\"language\": \"" + language + "\",\"isbn\": \"" + isbn + "\",\"quantity\": \"" + quantity + "\",\"bookSn\": \"" + bookSn + "\",\"program\": \"" + program + "\",\"classId\": \"" + classId + "\",\"bookType\": \"" + bookType + "\",\"subject\": \"" + subject + "\",\"name\": \"" + name + "\",\"publication\": \"" + publication + "\",\"language\": \"" + language + "\",\"author\": \"" + author + "\",\"edition\": \"" + edition + "\",\"pages\": \"" + pages + "\",\"price\": \"" + price + "\",\"rackNo\": \"" + rackNo + "\",\"language\": \"" + language + "\"}";
    }
}
