package com.jwt.example.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwt.example.models.JwtRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthIntegTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testFullAuthenticationFlow() throws JsonProcessingException, Exception{
        //login with valid creddentials
        JwtRequest loginRequest = new JwtRequest("shruti" , "shru");

        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(loginRequest)))
                                        .andExpect(status().isOk())
                                        .andExpect(jsonPath("$.jwtToken").exists())
                                        .andExpect(jsonPath("$.username").value("shruti"))
                                        .andReturn();
        // extract token from response
        String responseContent = loginResult.getResponse().getContentAsString();
        String token = objectMapper.readTree(responseContent).get("jwtToken").asText();

        //use token to access the end point
        mockMvc.perform(get("/home/user").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

}
