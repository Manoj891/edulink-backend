package com.ms.ware.online.solution.school.entity.library;

import lombok.*;

import java.util.Date;
import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "lib_book_issue")
public class LibBookIssue implements java.io.Serializable {

    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "book_id")
    private String bookId;
    @Column(name = "stu_id", nullable = true)
    private Long stuId;
    @Column(name = "staff_id", nullable = true)
    private Long staffId;
    @Column(name = "ISSUE_DATE", nullable = false)
    @javax.persistence.Temporal(javax.persistence.TemporalType.DATE)
    private Date issueDate;
    @Column(name = "ISSUE_FOR_DAY")
    private Integer issueForDay;
    @Column(name = "BOOK_ISSUE_ID", unique = true, nullable = true)
    private String bookIssueId;
    @Column(name = "RETURN_DATE", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date returnDate;
    @Column(name = "ISSUE_BY")
    private String issueBy;
    @Column(name = "RETURN_BY")
    private String returnBy;

    @Override
    public String toString() {
        return "\n{\"id\": \"" + id + "\",\"bookId\": \"" + bookId + "\",\"stuId\": \"" + stuId + "\",\"staffId\": \"" + staffId + "\",\"issueDate\": \"" + issueDate + "\",\"issueForDay\": \"" + issueForDay + "\",\"bookIssueId\": \"" + bookIssueId + "\",\"returnDate\": \"" + returnDate + "\"}";
    }
}
