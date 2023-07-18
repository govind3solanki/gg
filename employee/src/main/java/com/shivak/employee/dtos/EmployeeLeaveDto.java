package com.shivak.employee.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeLeaveDto {
    private int id;
    private int userId;
    private String status;
    private String reason;
    private String subject;
    private String startDateTime;
    private String endDateTime;
    private Date createdDate;
    private Date modifiedDate;
}
