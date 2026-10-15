package com.appsdeveloperblog.api.users.service;

import com.appsdeveloperblog.api.users.exceptions.UsersServiceException;
import com.appsdeveloperblog.api.users.io.UserEntity;
import com.appsdeveloperblog.api.users.io.UsersRepository;
import com.appsdeveloperblog.api.users.shared.UserDto;
import com.appsdeveloperblog.api.users.ui.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service("usersService")
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserMapper userMapper;

    @Autowired
    public UsersServiceImpl(UsersRepository usersRepository, BCryptPasswordEncoder bCryptPasswordEncoder, UserMapper userMapper) {
        this.usersRepository = usersRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto createUser(UserDto user) {

        if (usersRepository.findByEmail(user.getEmail()) != null)
            throw new UsersServiceException("Record already exists");

        UserEntity userEntity = userMapper.toUserEntity(user);

        String publicUserId = UUID.randomUUID().toString();
        userEntity.setUserId(publicUserId);
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        UserEntity storedUserDetails = usersRepository.save(userEntity);

        return userMapper.toUserDtoFromUserEntity(storedUserDetails);
    }

    @Override
    public List<UserDto> getUsers(int page, int limit) {
        List<UserDto> returnValue;

        if (page > 0) page -=1;

        Pageable pageableRequest = PageRequest.of(page, limit);

        Page<UserEntity> usersPage = usersRepository.findAll(pageableRequest);
        List<UserEntity> users = usersPage.getContent();

        returnValue = userMapper.toUserDtoList(users);
        return returnValue;
    }


    @Override
    public UserDto getUserById(String userId) {
        UserEntity userEntity = usersRepository.findByUserId(userId); // Find user by userId.
        if (userEntity == null) {
            throw new UsernameNotFoundException("User with ID " + userId + " not found");
        }

        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(userEntity, returnValue);

        return returnValue;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = usersRepository.findByEmail(email);

        if (userEntity == null)
            throw new UsernameNotFoundException(email);

        return new User(userEntity.getUserId(), userEntity.getEncryptedPassword(), new ArrayList<>());
    }

}
