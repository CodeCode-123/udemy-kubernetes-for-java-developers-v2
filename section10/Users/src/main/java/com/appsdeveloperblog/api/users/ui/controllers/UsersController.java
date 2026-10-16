package com.appsdeveloperblog.api.users.ui.controllers;

import com.appsdeveloperblog.api.users.service.UsersService;
import com.appsdeveloperblog.api.users.shared.UserDto;
import com.appsdeveloperblog.api.users.ui.mapper.UserMapper;
import com.appsdeveloperblog.api.users.ui.request.UserDetailsRequestModel;
import com.appsdeveloperblog.api.users.ui.response.UserRest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UsersService usersService;
    private final UserMapper userMapper;

    @Autowired
    public UsersController(UsersService usersService, UserMapper userMapper) {
        this.usersService = usersService;
        this.userMapper = userMapper;
    }

    @PostMapping
    public UserRest createUser(@RequestBody @Valid UserDetailsRequestModel userDetails) throws Exception {
        UserDto userDto = userMapper.toUserDto(userDetails);
        UserDto createdUser = usersService.createUser(userDto);

        return userMapper.toUserRest(createdUser);
    }

    @GetMapping("/{userId}")
    public UserRest getUser(@PathVariable String userId) {
        UserDto userDto = usersService.getUserById(userId); // Call the service layer method
        return userMapper.toUserRest(userDto);
    }


    @GetMapping
    public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "limit", defaultValue = "2") int limit) {
        List<UserDto> users = usersService.getUsers(page, limit);

        return userMapper.toUserRestList(users);
    }
}
