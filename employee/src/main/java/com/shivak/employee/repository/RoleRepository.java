package com.shivak.employee.repository;

import com.shivak.employee.dtos.RoleDto;
import com.shivak.employee.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {

    public Role findByName(String name);
}
