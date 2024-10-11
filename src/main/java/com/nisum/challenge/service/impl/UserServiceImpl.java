package com.nisum.challenge.service.impl;

import com.nisum.challenge.dto.CreateUserRequest;
import com.nisum.challenge.dto.UpdateUserRequest;
import com.nisum.challenge.dto.UserDTO;
import com.nisum.challenge.dto.UserResponse;
import com.nisum.challenge.exception.CustomException;
import com.nisum.challenge.exception.NotFoundException;
import com.nisum.challenge.exception.ValidationException;
import com.nisum.challenge.mapper.GeneralMapper;
import com.nisum.challenge.model.Phone;
import com.nisum.challenge.model.User;
import com.nisum.challenge.model.common.Identity;
import com.nisum.challenge.repository.PhoneRepository;
import com.nisum.challenge.repository.UserRepository;
import com.nisum.challenge.service.JwtTokenProvider;
import com.nisum.challenge.service.UserService;
import com.nisum.challenge.util.EntityUtil;
import com.nisum.challenge.util.PasswordUtil;
import com.nisum.challenge.util.ValidationUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PhoneRepository phoneRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public List<UserDTO> listAll() {
        List<User> users = userRepository.findAll();
        return GeneralMapper.INSTANCE.toUserDTOList(users);
    }

    @Override
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public UserDTO findById(UUID id) throws CustomException {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return GeneralMapper.INSTANCE.toUserDTO(user.get());
        } else {
            throw new NotFoundException("User not found");
        }
    }

    @Override
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public Page<UserDTO> listWithPagination(PageRequest pageRequest) {
        Page<User> userPage = userRepository.findAll(pageRequest);
        return userPage.map(GeneralMapper.INSTANCE::toUserDTO);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public UserResponse register(CreateUserRequest createUserRequest) throws CustomException {
        if (userRepository.existsByEmail(createUserRequest.getEmail())) {
            throw new ValidationException("The email address already exists");
        }

        ValidationUtil.validateEmail(createUserRequest.getEmail());
        ValidationUtil.validatePassword(createUserRequest.getPassword());

        User user = new User();
        user.setName(createUserRequest.getName());
        user.setEmail(createUserRequest.getEmail());
        user.setPassword(PasswordUtil.encryptPassword(createUserRequest.getPassword()));
        EntityUtil.setCreated(user);
        user.setLastLogin(user.getCreatedOn());
        user.setToken(jwtTokenProvider.createToken(user.getEmail()));
        user.setActive(true);
        List<Phone> phones = GeneralMapper.INSTANCE.toPhoneList(createUserRequest.getPhones());
        phones.forEach(phone -> {
            phone.setUser(user);
            EntityUtil.setCreated(phone);
        });
        user.setPhones(phones);
        userRepository.save(user);
        return getUserResponse(user);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public UserResponse modify(UUID id, UpdateUserRequest updateUserRequest) throws CustomException {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            if (!user.isActive()) {
                throw new ValidationException("Only active users can be modified");
            }
            user.setName(updateUserRequest.getName());
            EntityUtil.setUpdated(user);
            phoneRepository.deleteAllById(user.getPhones().stream().map(Identity::getId).toList());
            user.getPhones().clear();
            User savedUser = userRepository.save(user);
            List<Phone> phones = GeneralMapper.INSTANCE.toPhoneList(updateUserRequest.getPhones());
            phones.forEach(phone -> {
                phone.setUser(savedUser);
                EntityUtil.setCreated(phone);
            });
            phoneRepository.saveAll(phones);
            return getUserResponse(savedUser);
        } else {
            throw new NotFoundException("User not found");
        }
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void delete(UUID id) throws CustomException {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            if (user.isActive()) {
                throw new ValidationException("Only inactive users can be deleted");
            }
            userRepository.deleteById(user.getId());
        } else {
            throw new NotFoundException("User not found");
        }
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public UserResponse changePassword(UUID id, String newPassword) throws CustomException {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            if (!user.isActive()) {
                throw new ValidationException("Only active users can change their password");
            }
            ValidationUtil.validatePassword(newPassword);
            user.setPassword(PasswordUtil.encryptPassword(newPassword));
            userRepository.save(user);

            return getUserResponse(user);
        } else {
            throw new NotFoundException("User not found");
        }
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public UserResponse activateUser(UUID id) throws CustomException {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            if (user.isActive()) {
                throw new ValidationException("User is already active");
            }
            user.setActive(true);
            EntityUtil.setUpdated(user);
            userRepository.save(user);
            return getUserResponse(user);
        } else {
            throw new NotFoundException("User not found");
        }
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public UserResponse inactivateUser(UUID id) throws CustomException {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            if (!user.isActive()) {
                throw new ValidationException("User is already inactive");
            }
            user.setActive(false);
            EntityUtil.setUpdated(user);
            userRepository.save(user);
            return getUserResponse(user);
        } else {
            throw new NotFoundException("User not found");
        }
    }

    private UserResponse getUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .created(user.getCreatedOn())
                .modified(user.getUpdatedOn())
                .lastLogin(user.getLastLogin())
                .token(user.getToken())
                .isActive(user.isActive())
                .build();
    }
}