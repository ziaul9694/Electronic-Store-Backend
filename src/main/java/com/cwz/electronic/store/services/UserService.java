package com.cwz.electronic.store.services;

import com.cwz.electronic.store.dtos.PageableResponse;
import com.cwz.electronic.store.dtos.UserDto;
import com.cwz.electronic.store.entities.User;

import java.util.List;

public interface UserService {

    //create user

    UserDto createUser(UserDto userDto);

    //update user
    UserDto updateUser(UserDto userDto, String userId);

    //delete user
    void deleteUser(String userId);

    //get all users
    PageableResponse<UserDto> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir);

    //get single user by ID
    UserDto getSingleUserById(String userId);

    //get single user by email
    UserDto getSingleUserByEmail(String email);

    //search user
    List<UserDto> searchUser(String keyword);
}
