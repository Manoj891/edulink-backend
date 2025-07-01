package com.ms.ware.online.solution.school.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LibBookStockRes {
    private String isbn;
    private Long bookId;
    private Integer bookSn;
    private Long program;
    private Long classId;
    private Long bookType;
    private Long subject;
    private Long quantity;
    private String name;
    private String publication;
    private String language;
    private String author;
    private String edition;
    private Integer pages;
    private Double price;
    private String rackNo;
    private String vendor;
    private String purchaseDate;
}
