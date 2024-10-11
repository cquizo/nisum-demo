package com.nisum.challenge.config;

import com.nisum.challenge.dto.UserDTO;
import com.nisum.challenge.exception.CustomException;
import com.nisum.challenge.mapper.GeneralMapper;
import com.nisum.challenge.model.User;
import com.nisum.challenge.repository.UserRepository;
import com.nisum.challenge.service.UserService;
import com.nisum.challenge.service.impl.UserServiceImpl;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public UserService userService() {
        return new UserServiceImpl() {
            @Autowired
            private UserRepository userRepository;

            @Override
            @Transactional
            public List<UserDTO> listAll() {
                List<User> users = userRepository.findAll();
                return GeneralMapper.INSTANCE.toUserDTOList(users);
            }

            @Override
            @Transactional
            public Page<UserDTO> listWithPagination(PageRequest pageRequest) {
                Page<User> userPage = userRepository.findAll(pageRequest);
                return userPage.map(GeneralMapper.INSTANCE::toUserDTO);
            }

            @Override
            @Transactional
            public UserDTO findById(UUID id) throws CustomException {
                Optional<User> user = userRepository.findById(id);
                if (user.isPresent()) {
                    return GeneralMapper.INSTANCE.toUserDTO(user.get());
                } else {
                    throw new CustomException("User not found");
                }
            }
        };
    }
}