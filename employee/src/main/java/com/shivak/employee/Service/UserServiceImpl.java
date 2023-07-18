package com.shivak.employee.Service;

import com.shivak.employee.common.exceptions.CustomDataIntegrityViolationException;
import com.shivak.employee.common.exceptions.RecordNotFoundException;
import com.shivak.employee.common.messages.BaseResponse;
import com.shivak.employee.common.messages.CustomMessage;
import com.shivak.employee.dtos.RoleDto;
import com.shivak.employee.dtos.UpdatePasswordDto;
import com.shivak.employee.dtos.UserDto;
import com.shivak.employee.entity.Role;
import com.shivak.employee.entity.User;
import com.shivak.employee.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.internet.MimeMessage;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    public List<UserDto> findUserList() {
        return userRepository.findAll().stream().map(this::copyUserEntityToDto).collect(Collectors.toList());
    }

    public UserDto findByUserId(Integer id) {
        if (userRepository.existsById(id)) {
            Optional<User> userOptional = userRepository.findById(id);
            User user = userOptional.get();
            return copyUserEntityToDto(user);
        } else {
            throw new RecordNotFoundException(CustomMessage.DOESNOT_EXIT + id);
        }
    }

    public HashMap<String, Object> verifyUser(Integer userId) {
        Optional<User> byUserId = userRepository.findById(userId);
        HashMap<String, Object> map = new HashMap<>();
        if (byUserId.isPresent()) {
            User user = byUserId.get();
            if (!user.isVerified()) {
                user.setVerified(true);
                userRepository.save(user);
                map.put("success", true);
                map.put("message", "Successfully user verified");
            } else {
                map.put("success", false);
                map.put("message", "User already verified");
            }
        } else {
            map.put("success", false);
            map.put("message", "User not exists");
        }
        return map;
    }

    private static final String EMAIL_REGEX =
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    private static final Pattern pattern =
            Pattern.compile(EMAIL_REGEX);

    public static boolean isValidEmail(String email) {
        return pattern.matcher(email).matches();
    }

    @Override
    public HashMap<String, Object> forgetPassword(String usernameOrEmail) {
        boolean isValid = this.isValidEmail(usernameOrEmail);
        HashMap<String, Object> map = new HashMap<>();
        if (isValid) {
            User byEmail = userRepository.findByEmail(usernameOrEmail);
            if (byEmail != null) {
                try {
                    Integer userId = byEmail.getId();
                    String userIdString = String.valueOf(userId);
                    System.out.println(userIdString);
                    MimeMessage message = javaMailSender.createMimeMessage();
                    MimeMessageHelper helper = new MimeMessageHelper(message, true);

                    helper.setFrom(sender);
                    helper.setTo("tejasaaryan77@gmail.com");
                    helper.setSubject("For set new password");

                    String htmlContent = "<html><body>";
                    htmlContent += "<h1>HTML Mail</h1>";
                    htmlContent += "<span>If you want to set new password please click on :</span>";
                    htmlContent += "<a href=\"http://localhost:4200/set/newPassword/" + userIdString + "\">Click here</a>";
                    htmlContent += "</body></html>";
                    helper.setText(htmlContent, true);
                    javaMailSender.send(message);
                } catch (Exception e) {
                    Map<String, String> response = new HashMap<>();
                    response.put("message", "Error while Sending Mail");
                }
                map.put("success", true);
                map.put("message", "Please check your email");
                return map;
            } else {
                map.put("success", false);
                map.put("message", "Username or email not exist");
                return map;
            }
        } else {
            Optional<User> byUsername = userRepository.findByUsername(usernameOrEmail);
            if (byUsername.isPresent()) {
                User user = byUsername.get();
                try {
                    Integer userId = user.getId();
                    String userIdString = String.valueOf(userId);
                    System.out.println(userIdString);
                    MimeMessage message = javaMailSender.createMimeMessage();
                    MimeMessageHelper helper = new MimeMessageHelper(message, true);

                    helper.setFrom(sender);
                    helper.setTo("tejasaaryan77@gmail.com");
                    helper.setSubject("For set new password");

                    String htmlContent = "<html><body>";
                    htmlContent += "<h1>HTML Mail</h1>";
                    htmlContent += "<span>If you want to set new password please click on :</span>";
                    htmlContent += "<a href=\"http://localhost:4200/newPassword/" + userIdString + "\">Click here</a>";
                    htmlContent += "</body></html>";
                    helper.setText(htmlContent, true);
                    javaMailSender.send(message);
                } catch (Exception e) {
                    Map<String, String> response = new HashMap<>();
                    response.put("message", "Error while Sending Mail");
                }
                map.put("success", true);
                map.put("message", "Please check your email");
                return map;
            } else {
                map.put("success", false);
                map.put("message", "Username or email not exist");
                return map;
            }
        }
    }

    @Override
    public HashMap<String, Object> updatePassword(UpdatePasswordDto updatePasswordDto) {
        Optional<User> byId = userRepository.findById(updatePasswordDto.getUserId());
        HashMap<String, Object> map = new HashMap<>();
        if (byId.isPresent()) {
            User user=byId.get();
            user.setPassword(passwordEncoder.encode(updatePasswordDto.getPassword()));
            userRepository.save(user);
            map.put("success", true);
            map.put("message", "Successfully Password updated");
        } else {
            map.put("success", false);
            map.put("message", "User not exists");
        }
        return map;
    }

    @Override
    public HashMap<String, Object> changePasswordByUsername(String username, UpdatePasswordDto updatePasswordDto) {
        Optional<User> byUsername = userRepository.findByUsername(username);
        HashMap<String, Object> map = new HashMap<>();
        if (byUsername.isPresent()) {
            User user = byUsername.get();
            boolean matches = passwordEncoder.matches(updatePasswordDto.getOldPassword(), user.getPassword());
            if(matches) {
                user.setPassword(passwordEncoder.encode(updatePasswordDto.getPassword()));
                userRepository.save(user);
                map.put("success", true);
                map.put("message", "Successfully Password updated");
            }else {map.put("success", false);
                map.put("message", "User not exists");}
        } else {
            map.put("success", false);
            map.put("message", "User not exists");
        }
        return map;
    }

    @Override
    public UserDto findByUsername(String username) {
        User user = userRepository.findByUsername(username).get();
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        return userDto;
    }

    @Override
    public BaseResponse updateUser(UserDto userDto,String username) {
        Optional<User> byUsername = userRepository.findByUsername(username);
        if(byUsername.isPresent()){
            User user=byUsername.get();
            if(userDto.getFirstName()!=null&&userDto.getFirstName()!=""){
                user.setFirstName(userDto.getFirstName());
            }
            if(userDto.getMiddleName()!=null&&userDto.getMiddleName()!=""){
                user.setMiddleName(userDto.getMiddleName());
            }
            if(userDto.getLastName()!=null&&userDto.getLastName()!=""){
                user.setLastName(userDto.getLastName());
            }
            if(userDto.getAddress()!=null&&userDto.getAddress()!=""){
                user.setAddress(userDto.getAddress());
            }
            if(userDto.getState()!=null&&userDto.getState()!=""){
                user.setState(userDto.getState());
            }
            if(userDto.getCity()!=null&&userDto.getCity()!=""){
                user.setCity(userDto.getCity());
            }
            if(userDto.getCountry()!=null&&userDto.getCountry()!=""){
                user.setCountry(userDto.getCountry());
            }
            if(userDto.getZipcode()!=null&&userDto.getZipcode()!=""){
                user.setZipcode(userDto.getZipcode());
            }
            if(userDto.getEmail()!=null&&userDto.getEmail()!=""){
                user.setEmail(userDto.getEmail());
            }
            if(userDto.getUsername()!=null&&userDto.getUsername()!=""){
                user.setUsername(userDto.getUsername());
            }
            userRepository.save(user);
        }
        return new BaseResponse(CustomMessage.SAVE_SUCCESS_MESSAGE);
    }

    public BaseResponse createOrUpdateUser(UserDto userDto) {
        try {
            User user = copyUserDtoToEntity(userDto);
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));

            RoleDto role_employee = roleService.findByRoleName("ROLE_EMPLOYEE");
            Role role = copyRoleDtoToEntity(role_employee);
            List<Role> roles = new ArrayList<>();
            roles.add(role);
            user.setRoles(roles);

            user = userRepository.save(user);
            Integer userId = user.getId();
            String userIdString = String.valueOf(userId);
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(sender);
            helper.setTo("tejasaaryan77@gmail.com");
            helper.setSubject("For check smtp mail");

            String htmlContent = "<html><body>";
            htmlContent += "<h1>HTML Mail</h1>";
            htmlContent += "<span>You have successfully registered your account. please verify your email by click on below link</span>";
            htmlContent += "<a href=\"http://localhost:4200/verify/" + userIdString + "\">Click here</a>";
            htmlContent += "</body></html>";
            helper.setText(htmlContent, true);
            javaMailSender.send(message);

        } catch (DataIntegrityViolationException ex) {
            throw new CustomDataIntegrityViolationException(ex.getCause().getCause().getMessage());
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error while Sending Mail");
        }
        return new BaseResponse(CustomMessage.SAVE_SUCCESS_MESSAGE);
    }

    private UserDto copyUserEntityToDto(User user) {
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        return userDto;
    }

    private User copyUserDtoToEntity(UserDto userDto) {
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        return user;
    }

    private Role copyRoleDtoToEntity(RoleDto roleDto) {
        Role role = new Role();
        BeanUtils.copyProperties(roleDto, role);
        return role;
    }

}
