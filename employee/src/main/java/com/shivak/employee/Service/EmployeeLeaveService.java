package com.shivak.employee.Service;

import com.shivak.employee.common.messages.BaseResponse;
import com.shivak.employee.dtos.EmployeeLeaveDto;

import java.util.List;

public interface EmployeeLeaveService {
    BaseResponse createEmployeeLeave(EmployeeLeaveDto employeeLeaveDto);

    List<EmployeeLeaveDto> findEmployeeLeaveList();

    BaseResponse deleteUserById(Integer id);

    EmployeeLeaveDto findByEmployeeLeaveId(Integer id);

    EmployeeLeaveDto updateEmployeeLeave(Integer id, EmployeeLeaveDto employeeLeaveDto);
}
