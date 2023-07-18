package com.shivak.employee.Service;

import com.shivak.employee.dtos.RoleDto;
import com.shivak.employee.entity.Role;
import com.shivak.employee.repository.RoleRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService{

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public RoleDto findByRoleName(String name) {
        Role byName = roleRepository.findByName(name);
        RoleDto roleDto = copyEntityToRoleDto(byName);
        return roleDto;
    }

    private RoleDto copyEntityToRoleDto(Role role) {
        RoleDto roleDto = new RoleDto();
        BeanUtils.copyProperties(role, roleDto);
        return roleDto;
    }
}
