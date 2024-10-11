package com.nisum.challenge;

import com.nisum.challenge.config.TestConfig;
import com.nisum.challenge.dto.*;
import com.nisum.challenge.exception.CustomException;
import com.nisum.challenge.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Import(TestConfig.class)
class UserServiceTest {

    @Autowired
    private UserService userService;

    private UserResponse createTesUser() throws CustomException {
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setName(RandomStringUtils.randomAlphabetic(10));
        userRequest.setEmail(RandomStringUtils.randomAlphanumeric(25) + "@nisum.com");
        userRequest.setPassword("A1a" + RandomStringUtils.randomAlphanumeric(10));

        PhoneDTO phoneDTO1 = new PhoneDTO();
        phoneDTO1.setNumber(RandomStringUtils.randomNumeric(10));
        phoneDTO1.setCitycode(RandomStringUtils.randomNumeric(2));
        phoneDTO1.setCountrycode(RandomStringUtils.randomNumeric(3));

        PhoneDTO phoneDTO2 = new PhoneDTO();
        phoneDTO2.setNumber(RandomStringUtils.randomNumeric(10));
        phoneDTO2.setCitycode(RandomStringUtils.randomNumeric(2));
        phoneDTO2.setCountrycode(RandomStringUtils.randomNumeric(3));

        userRequest.setPhones(List.of(phoneDTO1, phoneDTO2));

        return userService.register(userRequest);
    }

    @Test
    void testRegister() throws CustomException {
        UserResponse response = createTesUser();

        assertNotNull(response.getId());
        assertNotNull(response.getCreated());
        assertNotNull(response.getLastLogin());
        assertNotNull(response.getToken());
        assertTrue(response.isActive());
    }

    @Test
    void testFindUserById() throws CustomException {
        UserResponse response = createTesUser();
        UserDTO foundUser = userService.findById(response.getId());

        assertEquals(response.getId(), foundUser.getId());
        assertNotNull(foundUser.getCreatedOn());
        assertNotNull(foundUser.getLastLogin());
        assertEquals(response.getToken(), foundUser.getToken());
        assertEquals(response.isActive(), foundUser.isActive());
        assertNull(foundUser.getModifiedOn());
    }

    @Test
    void testUpdateUser() throws CustomException {
        UserResponse response = createTesUser();

        UpdateUserRequest updateRequest = new UpdateUserRequest();
        updateRequest.setName("John Smith");

        PhoneDTO phoneDTO1 = new PhoneDTO();
        phoneDTO1.setNumber(RandomStringUtils.randomNumeric(10));
        phoneDTO1.setCitycode(RandomStringUtils.randomNumeric(2));
        phoneDTO1.setCountrycode(RandomStringUtils.randomNumeric(3));

        updateRequest.setPhones(List.of(phoneDTO1));

        userService.modify(response.getId(), updateRequest);
        UserDTO updatedUser = userService.findById(response.getId());

        assertEquals(updateRequest.getName(), updatedUser.getName());
        assertEquals(updateRequest.getPhones().size(), 1);
        assertNotNull(updatedUser.getModifiedOn());
    }

    @Test
    void testInactivateUser() throws CustomException {
        UserResponse response = createTesUser();
        UserResponse newResponse = userService.inactivateUser(response.getId());

        assertEquals(response.getId(), newResponse.getId());
        assertEquals(response.getToken(), newResponse.getToken());
        assertFalse(newResponse.isActive());
        assertNotNull(newResponse.getModified());
    }

    @Test
    void testActivateUser() throws CustomException {
        UserResponse response = createTesUser();
        userService.inactivateUser(response.getId());
        UserResponse newResponse = userService.activateUser(response.getId());

        assertEquals(response.getId(), newResponse.getId());
        assertEquals(response.getToken(), newResponse.getToken());
        assertTrue(newResponse.isActive());
        assertNotNull(newResponse.getModified());
    }

    @Test
    void testChangePassword() throws CustomException {
        UserResponse response = createTesUser();
        UserResponse newResponse = userService.changePassword(response.getId(), "A1a" + RandomStringUtils.randomAlphanumeric(10));

        assertEquals(response.getId(), newResponse.getId());
        assertEquals(response.getToken(), newResponse.getToken());
        assertNull(newResponse.getModified());
    }

    @Test
    void testDeleteUser() throws CustomException {
        UserResponse createdUser = createTesUser();
        userService.inactivateUser(createdUser.getId());
        userService.delete(createdUser.getId());
    }

    @Test
    void testFindAllUsers() throws CustomException {
        createTesUser();
        createTesUser();

        List<UserDTO> foundUsers = userService.listAll();

        assertTrue(foundUsers.size() >= 2);
    }
}