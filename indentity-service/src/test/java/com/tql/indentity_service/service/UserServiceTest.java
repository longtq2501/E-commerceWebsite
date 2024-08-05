package com.tql.indentity_service.service;

import com.tql.indentity_service.dto.request.UserCreateRequest;
import com.tql.indentity_service.dto.response.UserResponse;
import com.tql.indentity_service.entity.User;
import com.tql.indentity_service.enums.ErrorCode;
import com.tql.indentity_service.exception.AppException;
import com.tql.indentity_service.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(password = "123456", username = "longcuto")
public class UserServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;
    private User user;


    @MockBean
    private UserRepository userRepository;

    private UserCreateRequest request;
    private UserResponse response;


    @BeforeEach
    void initData() {

        request = UserCreateRequest.builder()
                .username("longcuto")
                .name("Long")
                .password("123456")
                .confirmPassword("123456")
                .build();


        response = UserResponse.builder()
                .id("ecf62bfbb6dd1")
                .username("longcuto")
                .name("Long")
                .build();

        user = User.builder()
                .id("ecf62bfbb6dd1")
                .username("longcuto")
                .name("Long")
                .password("123456")
                .confirmPassword("123456")
                .build();
    }

    @Test
    void createService_success() throws Exception {
        //GIVEN
        Mockito.when(userRepository.existsUserByUsername(request.getUsername())).thenReturn(false);
        Mockito.when(userRepository.save(any())).thenReturn(user);

        //WHEN
        var response = userService.create(request);

        //THEN
        Assertions.assertThat(response.getId()).isEqualTo("ecf62bfbb6dd1");
        Assertions.assertThat(response.getUsername()).isEqualTo("longcuto");
        Assertions.assertThat(response.getName()).isEqualTo("Long");
    }

    @Test
    void createService_UsernameExists_fail() throws Exception {
        //GIVEN
        request.setUsername("longcuto");
        Mockito.when(userRepository.existsUserByUsername(request.getUsername())).thenReturn(true);

        //WHEN
        var exception =  assertThrows(AppException.class, () -> userService.create(request));

        //THEN
        Assertions.assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_ALREADY_EXISTS);
    }

    @Test
    @WithMockUser(password = "123456", username = "longcuto")
    void getByIdService_success() throws Exception {
        //GIVEN
        Mockito.when(userRepository.findById(ArgumentMatchers.eq("ecf62bfbb6dd1"))).thenReturn(Optional.of(user));
        //WHEN
        var response = userService.getById("ecf62bfbb6dd1");
        //THEN
        Assertions.assertThat(response.getId()).isEqualTo("ecf62bfbb6dd1");
        Assertions.assertThat(response.getUsername()).isEqualTo("longcuto");
        Assertions.assertThat(response.getName()).isEqualTo("Long");
    }
}















