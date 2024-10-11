package com.nisum.challenge;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nisum.challenge.dto.CreateUserRequest;
import com.nisum.challenge.dto.PhoneDTO;
import com.nisum.challenge.dto.UpdateUserRequest;
import com.nisum.challenge.dto.UserResponse;
import com.nisum.challenge.exception.CustomException;
import com.nisum.challenge.model.User;
import com.nisum.challenge.repository.UserRepository;
import com.nisum.challenge.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class DemoApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

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
    void testList() throws Exception {
        createTesUser();
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").isNotEmpty())
                .andExpect(jsonPath("$[0].name").isNotEmpty());
    }

    @Test
    void testFindById() throws Exception {
        UserResponse response = createTesUser();

        mockMvc.perform(get("/api/users/{id}", response.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(response.getToken()));
    }

    @Test
    void testRegister() throws Exception {
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

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    void testModify() throws Exception {
        UserResponse response = createTesUser();

        UpdateUserRequest updateRequest = new UpdateUserRequest();
        updateRequest.setName("John Smith");

        PhoneDTO phoneDTO = new PhoneDTO();
        phoneDTO.setNumber(RandomStringUtils.randomNumeric(10));
        phoneDTO.setCitycode(RandomStringUtils.randomNumeric(2));
        phoneDTO.setCountrycode(RandomStringUtils.randomNumeric(3));

        updateRequest.setPhones(List.of(phoneDTO));

        mockMvc.perform(put("/api/users/{id}", response.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void testDelete() throws Exception {
        UserResponse response = createTesUser();
        userService.inactivateUser(response.getId());

        mockMvc.perform(delete("/api/users/{id}", response.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void testChangePassword() throws Exception {
        UserResponse response = createTesUser();

        mockMvc.perform(put("/api/users/{id}/change-password", response.getId())
                        .param("newPassword", "A1a" + RandomStringUtils.randomAlphanumeric(10)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(response.getToken()));
    }

    @Test
    void testActivateUser() throws Exception {
        UserResponse response = createTesUser();
        userService.inactivateUser(response.getId());

        mockMvc.perform(put("/api/users/{id}/activate", response.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void testInactivateUser() throws Exception {
        UserResponse response = createTesUser();
        mockMvc.perform(put("/api/users/{id}/inactivate", response.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(false));
    }

    @Test
    void findByIdReturnsNotFoundForNonExistentUser() throws Exception {
        mockMvc.perform(get("/api/users/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void registerFailsWithInvalidEmail() throws Exception {
        CreateUserRequest request = new CreateUserRequest();
        request.setName("John Doe");
        request.setEmail("invalid-email");
        request.setPassword("password");

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void modifyFailsForNonExistentUser() throws Exception {
        UpdateUserRequest updateRequest = new UpdateUserRequest();
        updateRequest.setName("John Smith");

        mockMvc.perform(put("/api/users/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteFailsForNonExistentUser() throws Exception {
        mockMvc.perform(delete("/api/users/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void changePasswordFailsForNonExistentUser() throws Exception {
        mockMvc.perform(put("/api/users/{id}/change-password", UUID.randomUUID())
                        .param("newPassword", "newPassword"))
                .andExpect(status().isNotFound());
    }

    @Test
    void activateUserFailsForNonExistentUser() throws Exception {
        mockMvc.perform(put("/api/users/{id}/activate", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void inactivateUserFailsForNonExistentUser() throws Exception {
        mockMvc.perform(put("/api/users/{id}/inactivate", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }
}