package com.shivak.employee.Service;

import com.shivak.employee.dtos.RoleDto;

public interface RoleService {
    public RoleDto findByRoleName(String name);
}
