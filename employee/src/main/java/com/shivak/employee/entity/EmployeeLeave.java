package com.shivak.employee.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "employee_leave")
public class EmployeeLeave {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String status;
    private String reason;
    private String subject;

    //@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_date_time")
    private String startDateTime;

    //@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_date_time")
    private String endDateTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date modifiedDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
