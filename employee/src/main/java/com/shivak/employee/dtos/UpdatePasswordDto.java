package com.shivak.employee.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePasswordDto {
    private Integer userId;
    private String oldPassword;
    private String password;
    private String newPassword;
}
