package com.tql.indentity_service.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.tql.indentity_service.dto.request.UserUpdateRequest;
import com.tql.indentity_service.dto.response.UserResponse;
import com.tql.indentity_service.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;


@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "longcuto")
//@TestPropertySource("classpath:/test.properties")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private UserUpdateRequest request;
    private UserResponse response;
    private List<UserResponse> listResponse;


    @BeforeEach
    void initData() {
        request = UserUpdateRequest.builder()
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
    }

    @Test
    void create_success() throws Exception {
        //GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(request);
        Mockito.when(userService.create(ArgumentMatchers.any())).thenReturn(response);
        //WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                .post("/user/create")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
                .andExpect(MockMvcResultMatchers.jsonPath("result.id").value("ecf62bfbb6dd1"));
    }

    @Test
    void update_success() throws Exception {
        //GIVEN
        request.setUsername("longchotim");
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(request);
        Mockito.when(userService.update(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(response);
        //WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                .put("/user/update/{id}", "ecf62bfbb6dd1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
                .andExpect(MockMvcResultMatchers.jsonPath("result.id").value("ecf62bfbb6dd1"));
    }

    @Test
    void getAll_success() throws Exception {
        // GIVEN
        List<UserResponse> expectedUsers = userService.getAll();
        Mockito.when(userService.getAll()).thenReturn(expectedUsers);

        // WHEN
        mockMvc.perform(MockMvcRequestBuilders
                .get("/user/get-all")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000));
    }

    @Test
    void getMyInfo_success() throws Exception{
        // WHEN
        mockMvc.perform(MockMvcRequestBuilders
                .get("/user/get-my-info")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000));
    }

    @Test
    void delete_success() throws Exception{
        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/user/delete/{id}", "ecf62bfbb6dd1")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000));
    }

    @Test
    void getById_success() throws Exception {
        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                .get("/user/get-by-id/{id}", "ecf62bfbb6dd1")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000));
    }

}
