package com.thacker.todo.api.test.controller;

import com.jayway.jsonpath.JsonPath;
import com.thacker.todo.api.model.TodoDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TodoApiApplicationTests extends BaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String TODOS_BASE_URI = "/todos";
    public static final String TODOS_URI_TEMPLATE = TODOS_BASE_URI + "/{id}";

    String title="Test title";
    private TodoDto todoToCreate;

    @BeforeEach
    void setup() {
        todoToCreate = TodoDto.builder().title(title).order(2).build();
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "TODO");
    }


    @Test
    void whenCreateTodo_shouldReturnCreatedTodoWithResourceUrl() throws Exception {

        mockMvc.perform(post(TODOS_BASE_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todoToCreate)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.url").exists())
                .andReturn();
    }

    @Test
    void whenGetTodo_shouldReturnTodo() throws Exception {

        String createResponse = mockMvc.perform(post(TODOS_BASE_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todoToCreate)))
                .andReturn().getResponse().getContentAsString();

        String createdResourceUrl = JsonPath.read(createResponse, "$.url");

        mockMvc.perform(get(createdResourceUrl))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.completed").value(false))
                .andExpect(jsonPath("$.order").value(2))
                .andExpect(jsonPath("$.url").exists());
    }

    @Test
    void whenCreateTodo_withoutMandatoryDetails_shouldReturnBadRequestStatus() throws Exception {

        mockMvc.perform(post(TODOS_BASE_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new TodoDto())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenUpdateTodo_shouldReturnUpdatedTodo_and_WorkingResourceUrl() throws Exception {

        // Send POST request
        String createResponse = mockMvc.perform(post(TODOS_BASE_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todoToCreate)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        TodoDto updatedTodo = todoToCreate;
        updatedTodo.setCompleted(true);

        String createdResourceUrl = JsonPath.read(createResponse, "$.url");
        mockMvc.perform(patch(createdResourceUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTodo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completed").value(true))
                .andReturn().getResponse();

    }

    @Test
    void whenTryToUpdateNonExistingTodo_shouldReturnNotFoundStatus() throws Exception {

        TodoDto updatedTodo = todoToCreate;
        updatedTodo.setCompleted(true);

        mockMvc.perform(patch(TODOS_URI_TEMPLATE,1000)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTodo)))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenDeleteOneTodo_shouldReturnSuccessStatus() throws Exception {

        String createResponse = mockMvc.perform(post(TODOS_BASE_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todoToCreate)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        String createdResourceUrl = JsonPath.read(createResponse, "$.url");

        mockMvc.perform(delete(createdResourceUrl)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get(createdResourceUrl))
                .andExpect(status().isNotFound());

    }

    @Test
    void whenTryToDeleteNonExistingTodo_shouldReturnNotFoundStatus() throws Exception {

        mockMvc.perform(delete(TODOS_URI_TEMPLATE,1000)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    void whenDeleteAllTodo_shouldReturnSuccessStatus() throws Exception {
        mockMvc.perform(post(TODOS_BASE_URI).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(todoToCreate)));
        mockMvc.perform(post(TODOS_BASE_URI).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(todoToCreate)));

        mockMvc.perform(get(TODOS_BASE_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todoToCreate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        mockMvc.perform(delete(TODOS_BASE_URI))
                .andExpect(status().isOk());
    }
}
