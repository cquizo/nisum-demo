package com.nisum.challenge.service;

import com.nisum.challenge.dto.CreateUserRequest;
import com.nisum.challenge.dto.UpdateUserRequest;
import com.nisum.challenge.dto.UserDTO;
import com.nisum.challenge.dto.UserResponse;
import com.nisum.challenge.exception.CustomException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.UUID;

public interface UserService {

    List<UserDTO> listAll();

    UserDTO findById(UUID id) throws CustomException;

    Page<UserDTO> listWithPagination(PageRequest pageRequest);

    UserResponse register(CreateUserRequest createUserRequest) throws CustomException;

    UserResponse modify(UUID id, UpdateUserRequest updateUserRequest) throws CustomException;

    void delete(UUID id) throws CustomException;

    UserResponse changePassword(UUID id, String newPassword) throws CustomException;

    UserResponse activateUser(UUID id) throws CustomException;

    UserResponse inactivateUser(UUID id) throws CustomException;
}