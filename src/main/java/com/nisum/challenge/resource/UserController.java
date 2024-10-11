package com.nisum.challenge.resource;

import com.nisum.challenge.dto.CreateUserRequest;
import com.nisum.challenge.dto.UpdateUserRequest;
import com.nisum.challenge.dto.UserDTO;
import com.nisum.challenge.dto.UserResponse;
import com.nisum.challenge.exception.CustomException;
import com.nisum.challenge.exception.NotFoundException;
import com.nisum.challenge.exception.ValidationException;
import com.nisum.challenge.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "User Management", description = "Operations related to user management")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @Operation(summary = "List all users", description = "This operation lists all users including their phones")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users listed successfully"),
            @ApiResponse(responseCode = "204", description = "There are no users to display")
    })
    public ResponseEntity<?> list() {
        List<UserDTO> list = userService.listAll();
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/page")
    @Operation(summary = "List users with pagination", description = "This operation lists users with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users listed successfully"),
            @ApiResponse(responseCode = "204", description = "There are no users to display")
    })
    public ResponseEntity<?> listWithPagination(@RequestParam int page, @RequestParam int size) {
        Page<UserDTO> userPage = userService.listWithPagination(PageRequest.of(page, size));
        if (userPage.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(userPage, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find user by ID", description = "This operation retrieves a user by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<?> findById(@PathVariable UUID id) {
        try {
            UserDTO user = userService.findById(id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(Collections.singletonMap("message", e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (CustomException e) {
            return new ResponseEntity<>(Collections.singletonMap("message", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Register a new user", description = "This operation creates a new user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid values or email already exists")
    })
    public ResponseEntity<?> register(@Valid @RequestBody CreateUserRequest createUserRequest) {
        try {
            UserResponse response = userService.register(createUserRequest);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (CustomException e) {
            return new ResponseEntity<>(Collections.singletonMap("message", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modify user", description = "This operation modifies an existing user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User modified successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid values"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<?> modify(@PathVariable UUID id, @Valid @RequestBody UpdateUserRequest updateUserRequest) {
        try {
            UserResponse response = userService.modify(id, updateUserRequest);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(Collections.singletonMap("message", e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (CustomException e) {
            return new ResponseEntity<>(Collections.singletonMap("message", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "This operation deletes a user by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        try {
            userService.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(Collections.singletonMap("message", e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (CustomException e) {
            return new ResponseEntity<>(Collections.singletonMap("message", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}/change-password")
    @Operation(summary = "Change user password", description = "This operation changes the password of an existing user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid password"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<?> changePassword(@PathVariable UUID id, @RequestParam String newPassword) {
        try {
            UserResponse response = userService.changePassword(id, newPassword);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(Collections.singletonMap("message", e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (CustomException e) {
            return new ResponseEntity<>(Collections.singletonMap("message", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}/activate")
    @Operation(summary = "Activate user", description = "This operation activates an existing user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User inactivated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<?> activateUser(@PathVariable UUID id) {
        try {
            UserResponse response = userService.activateUser(id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(Collections.singletonMap("message", e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (CustomException e) {
            return new ResponseEntity<>(Collections.singletonMap("message", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}/inactivate")
    @Operation(summary = "Inactivate user", description = "This operation inactivates an existing user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User inactivated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<?> inactivateUser(@PathVariable UUID id) {
        try {
            UserResponse response = userService.inactivateUser(id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(Collections.singletonMap("message", e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (CustomException e) {
            return new ResponseEntity<>(Collections.singletonMap("message", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}