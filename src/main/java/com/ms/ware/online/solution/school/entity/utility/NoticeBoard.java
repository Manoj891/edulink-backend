package com.ms.ware.online.solution.school.entity.utility;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
//import lombok.Data;
//
//@Data

@Entity
@Table(name = "notice_board")
public class NoticeBoard implements java.io.Serializable {

    @Id
    @Column(name = "ID")
    private Long id;
    @Column(name = "YEAR")
    private Integer year;
    @Column(name = "SN")
    private Integer sn;
    @Column(name = "ENTER_DATE")
    private String enterDate;
    @Column(name = "ENTER_DATEAD", columnDefinition = "DATE")
    private String enterDateAd;
    @Column(name = "ENTER_BY")
    private String enterBy;
    @Column(name = "NOTICE", columnDefinition = "TEXT CHARACTER SET utf8 COLLATE utf8_general_ci")
    private String notice;
    @Column(name = "TITLE", columnDefinition = "TEXT CHARACTER SET utf8 COLLATE utf8_general_ci")
    private String title;

    public NoticeBoard() {
    }

    public NoticeBoard(Long id) {
        this.id = id;
    }

    public NoticeBoard(String id) {
        this.id = Long.parseLong(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getSn() {
        return sn;
    }

    public void setSn(Integer sn) {
        this.sn = sn;
    }

    public String getEnterDate() {
        return enterDate;
    }

    public void setEnterDate(String enterDate) {
        this.enterDate = enterDate;
    }

    public String getEnterDateAd() {
        return enterDateAd;
    }

    public void setEnterDateAd(String enterDateAd) {
        this.enterDateAd = enterDateAd;
    }

    public String getEnterBy() {
        return enterBy;
    }

    public void setEnterBy(String enterBy) {
        this.enterBy = enterBy;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "\n{\"id\": \"" + id + "\",\"year\": \"" + year + "\",\"sn\": \"" + sn + "\",\"enterDateAd\": \"" + enterDateAd + "\",\"enterDate\": \"" + enterDate + "\",\"enterBy\": \"" + enterBy + "\",\"notice\": \"" + notice + "\"}";
    }
}
