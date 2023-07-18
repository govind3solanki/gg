package com.shivak.employee.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Integer id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String dateOfBirth;
    private String address;
    private String city;
    private String state;
    private String country;
    private String zipcode;
    private Date joiningDate;
    private Date leavingDate;
    private Date createdDate;
    private Date modifiedDate;
    private boolean isActive;
    private boolean isVerified;
    private String username;
    private String password;
    private String email;
}
