package com.shivak.employee.Service;

import com.shivak.employee.common.exceptions.RecordNotFoundException;
import com.shivak.employee.common.messages.BaseResponse;
import com.shivak.employee.common.messages.CustomMessage;
import com.shivak.employee.dtos.EmployeeLeaveDto;
import com.shivak.employee.dtos.RoleDto;
import com.shivak.employee.dtos.UserDto;
import com.shivak.employee.enoms.Status;
import com.shivak.employee.entity.EmployeeLeave;
import com.shivak.employee.entity.User;
import com.shivak.employee.repository.EmployeeLeaveRepository;
import com.shivak.employee.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeLeaveServiceImpl implements EmployeeLeaveService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeLeaveRepository employeeLeaveRepository;

    @Override
    public BaseResponse createEmployeeLeave(EmployeeLeaveDto employeeLeaveDto) {
        EmployeeLeave employeeLeave = copyEmployeeLeaveDtoToEntity(employeeLeaveDto);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        Optional<User> byUsername = userRepository.findByUsername(username);
        if (byUsername.isPresent()) {
            User user = byUsername.get();
            employeeLeave.setUser(user);
        }
        employeeLeave.setStatus("PENDING");
        EmployeeLeave save = employeeLeaveRepository.save(employeeLeave);
        return new BaseResponse(CustomMessage.SAVE_SUCCESS_MESSAGE);
    }

    @Override
    public List<EmployeeLeaveDto> findEmployeeLeaveList() {
        return employeeLeaveRepository.findAll().stream().map(this::copyEmployeeLeaveEntityToDto).collect(Collectors.toList());
    }

    @Override
    public BaseResponse deleteUserById(Integer id) {
        if (employeeLeaveRepository.existsById(id)) {
            employeeLeaveRepository.deleteById(id);
        } else {
            throw new RecordNotFoundException(CustomMessage.NO_RECOURD_FOUND + id);
        }
        return new BaseResponse(CustomMessage.DELETE_SUCCESS_MESSAGE);
    }

    @Override
    public EmployeeLeaveDto findByEmployeeLeaveId(Integer id) {
        Optional<EmployeeLeave> byId = employeeLeaveRepository.findById(id);
        EmployeeLeaveDto employeeLeaveDto=null;
        if(byId.isPresent()){
            EmployeeLeave employeeLeave = byId.get();
            employeeLeaveDto = copyEmployeeLeaveEntityToDto(employeeLeave);
        }
        return employeeLeaveDto;
    }

    @Override
    public EmployeeLeaveDto updateEmployeeLeave(Integer id, EmployeeLeaveDto employeeLeaveDto) {
        EmployeeLeaveDto byEmployeeLeaveId = findByEmployeeLeaveId(id);
        if(byEmployeeLeaveId!=null){
            EmployeeLeave employeeLeave = copyEmployeeLeaveDtoToEntity(byEmployeeLeaveId);
            if(employeeLeaveDto.getSubject()!=null&&employeeLeaveDto.getSubject()!=""){
                employeeLeave.setSubject(employeeLeaveDto.getSubject());
            }
            if(employeeLeaveDto.getReason()!=null&&employeeLeaveDto.getReason()!=""){
                employeeLeave.setReason(employeeLeaveDto.getReason());
            }
            if(employeeLeaveDto.getStartDateTime()!=null){
                employeeLeave.setStartDateTime(employeeLeaveDto.getStartDateTime());
            }
            if(employeeLeaveDto.getEndDateTime()!=null){
                employeeLeave.setEndDateTime(employeeLeaveDto.getEndDateTime());
            }
            EmployeeLeave save = employeeLeaveRepository.save(employeeLeave);
           return copyEmployeeLeaveEntityToDto(save);
        }
        return null;
    }

    private EmployeeLeaveDto copyEmployeeLeaveEntityToDto(EmployeeLeave employeeLeave) {
        EmployeeLeaveDto employeeLeaveDto = new EmployeeLeaveDto();
        BeanUtils.copyProperties(employeeLeave, employeeLeaveDto);
        return employeeLeaveDto;
    }

    private EmployeeLeave copyEmployeeLeaveDtoToEntity(EmployeeLeaveDto employeeLeaveDto) {
        EmployeeLeave employeeLeave = new EmployeeLeave();
        BeanUtils.copyProperties(employeeLeaveDto, employeeLeave);
        return employeeLeave;
    }
}
