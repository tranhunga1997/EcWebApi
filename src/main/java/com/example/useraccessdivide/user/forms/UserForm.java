package com.example.useraccessdivide.user.forms;

import com.example.useraccessdivide.user.dtos.UserDto;
import lombok.Data;

@Data
public class UserForm {
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;


    public UserDto convertToUserDto(){
        UserDto userDto = new UserDto();
        userDto.setUsername(username);
        userDto.setFirstName(firstName);
        userDto.setLastName(lastName);
        userDto.setEmail(email);
        return userDto;
    }
}
