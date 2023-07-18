package com.shivak.employee.controller;

import com.shivak.employee.Service.EmployeeLeaveService;
import com.shivak.employee.Service.UserService;
import com.shivak.employee.common.messages.BaseResponse;
import com.shivak.employee.common.messages.CustomMessage;
import com.shivak.employee.dtos.EmployeeLeaveDto;
import com.shivak.employee.dtos.UpdatePasswordDto;
import com.shivak.employee.dtos.UserDto;
import com.shivak.employee.dtos.UserNameOrEmailDto;
import com.shivak.employee.entity.OAuthAccessToken;
import com.shivak.employee.repository.OAuthAccessTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@Validated
@RestController
@RequestMapping("/user")
//we can use OncePerRequestFilter class for allow cross origin
/*@CrossOrigin(origins = "*")*/
public class UserController {

    @Autowired
    private EmployeeLeaveService employeeLeaveService;

    @Autowired
    private UserService userService;

    @Autowired
    private OAuthAccessTokenRepository oAuthAccessTokenRepository;



    @GetMapping(value = "/find")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> list = userService.findUserList();
        return new ResponseEntity<List<UserDto>>(list, HttpStatus.OK);
    }

    @GetMapping(value = "/find/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Integer id) {
        UserDto list = userService.findByUserId(id);
        return new ResponseEntity<UserDto>(list, HttpStatus.OK);
    }

    @PostMapping(value = {"/add"})
    public ResponseEntity<BaseResponse> createOrUpdateUser(@Valid @RequestBody UserDto userDto) {
        BaseResponse response = userService.createOrUpdateUser(userDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping(value = "/logout")
    public  ResponseEntity<BaseResponse> logout(@RequestBody String accessToken) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal). getUsername();
        } else {
            username = principal. toString();
        }
        OAuthAccessToken byUserName = oAuthAccessTokenRepository.findByUserName(username);
        if(byUserName!= null){
            oAuthAccessTokenRepository.delete(byUserName);
        }
        BaseResponse baseResponse = new BaseResponse(CustomMessage.DELETE_SUCCESS_MESSAGE);
        return new ResponseEntity<>(baseResponse,HttpStatus.OK);
    }

    @PutMapping(value = {"/update"})
    public  ResponseEntity<BaseResponse> updateUser(@RequestBody UserDto userDto){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal). getUsername();
        } else {
            username = principal. toString();
        }
        BaseResponse response=userService.updateUser(userDto,username);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping(value = "/verify/{id}")
    public ResponseEntity<HashMap<String, Object>> verifyUser(@PathVariable Integer id){
        HashMap<String, Object> s = userService.verifyUser(id);
        return new ResponseEntity<>(s, HttpStatus.OK);
    }

    @PostMapping(value ={ "/forgetPassword"})
    public ResponseEntity<HashMap<String, Object>> forgetPassword(@RequestBody UserNameOrEmailDto userNameOrEmailDto){
        HashMap<String, Object> s = userService.forgetPassword(userNameOrEmailDto.getUsernameOrEmail());
        return new ResponseEntity<>(s, HttpStatus.OK);
    }

    @PostMapping(value = "/updatePassword")
    public ResponseEntity<HashMap<String, Object>> updatePassword(@RequestBody UpdatePasswordDto updatePasswordDto){
        HashMap<String, Object> s = userService.updatePassword(updatePasswordDto);
        return new ResponseEntity<>(s, HttpStatus.OK);
    }

    @GetMapping(value = "/loggedinUser")
    public ResponseEntity<UserDto> loggedInUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(principal);
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal). getUsername();
        } else {
            username = principal. toString();
        }
        return new ResponseEntity<UserDto>(userService.findByUsername(username), HttpStatus.OK);
    }

    @PostMapping(value = "/changePasswordByToken")
    public ResponseEntity<HashMap<String, Object>> changePasswordByUsername(@RequestBody UpdatePasswordDto updatePasswordDto){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal). getUsername();
        } else {
            username = principal. toString();
        }
        HashMap<String, Object> s = userService.changePasswordByUsername(username,updatePasswordDto);
        return new ResponseEntity<>(s, HttpStatus.OK);
    }

    @PostMapping(value = {"/addEmployeeLeave"})
    public ResponseEntity<BaseResponse> createEmployeeLeave(@Valid @RequestBody EmployeeLeaveDto employeeLeaveDto) {
        BaseResponse response = employeeLeaveService.createEmployeeLeave(employeeLeaveDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping(value = "/findLeaves")
    public ResponseEntity<List<EmployeeLeaveDto>> getAllLeaves() {
        List<EmployeeLeaveDto> list = employeeLeaveService.findEmployeeLeaveList();
        return new ResponseEntity<List<EmployeeLeaveDto>>(list, HttpStatus.OK);
    }

    @DeleteMapping(value = "/deleteLeave/{id}")
    public ResponseEntity<BaseResponse> deleteUserById(@PathVariable("id") Integer id) {
        BaseResponse response = employeeLeaveService.deleteUserById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping(value = "/leave/{id}")
    public ResponseEntity<EmployeeLeaveDto> getLeaveById(@PathVariable("id") Integer id) {
        EmployeeLeaveDto emp = employeeLeaveService.findByEmployeeLeaveId(id);
        return new ResponseEntity<EmployeeLeaveDto>(emp, HttpStatus.OK);
    }

    @PutMapping(value = "/leave/update/{id}")
    public ResponseEntity<EmployeeLeaveDto> updateEmployeeLeave(@PathVariable("id") Integer id,@RequestBody EmployeeLeaveDto employeeLeaveDto){
        EmployeeLeaveDto emp =employeeLeaveService.updateEmployeeLeave(id,employeeLeaveDto);
        return new ResponseEntity<EmployeeLeaveDto>(emp,HttpStatus.OK);
    }
}


