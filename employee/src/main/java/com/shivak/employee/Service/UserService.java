package com.shivak.employee.Service;

import com.shivak.employee.common.messages.BaseResponse;
import com.shivak.employee.dtos.UpdatePasswordDto;
import com.shivak.employee.dtos.UserDto;

import java.util.HashMap;
import java.util.List;

public interface UserService {
    public List<UserDto> findUserList();
    public UserDto findByUserId(Integer id);

    public BaseResponse createOrUpdateUser(UserDto userDto);

    public HashMap<String, Object> verifyUser(Integer Id);

    public HashMap<String, Object> forgetPassword(String usernameOrEmail);

    public HashMap<String, Object> updatePassword(UpdatePasswordDto updatePasswordDto);

    public HashMap<String, Object> changePasswordByUsername(String username,UpdatePasswordDto updatePasswordDto);

   public UserDto findByUsername(String username);

   public BaseResponse updateUser(UserDto userDto,String username);
}
