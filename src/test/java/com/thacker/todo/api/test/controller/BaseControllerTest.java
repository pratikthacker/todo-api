package com.thacker.todo.api.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
public abstract class BaseControllerTest {

    @Autowired
    protected ObjectMapper objectMapper;

}
