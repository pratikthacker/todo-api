package com.thacker.todo.api.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.thacker.todo.api.model.Resource;
import com.thacker.todo.api.model.TodoDto;
import static com.thacker.todo.api.model.validation.ValidationGroups.*;

import com.thacker.todo.api.model.view.TodoViews;
import com.thacker.todo.api.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.web.bind.annotation.RequestMethod.*;

import java.util.*;

@RestController()
@RequestMapping("/todos")
@RequiredArgsConstructor
@Slf4j
public class TodoController {

    private final TodoService todoService;

    @PostMapping
    public Resource<TodoDto> createTodo(@Validated(CreateGroup.class)
                                        @JsonView(TodoViews.Input.class)
                                        @RequestBody TodoDto todo){
        log.trace("Creating todo");
        TodoDto createdTodo = todoService.createTodo(todo);
        return getResource(createdTodo);
    }

    @GetMapping("/{todoId}")
    public Resource<TodoDto> getById(@PathVariable("todoId") Integer id){
        log.trace("get todo by id");
        TodoDto todo = todoService.getTodo(id);
        return getResource(todo);
    }

    @GetMapping
    public List<Resource<TodoDto>> getAll(){
        log.trace("get all todos");
        List<TodoDto> todos = todoService.getAllTodo();
        return todos.stream().map(this::getResource).toList();
    }

    @PatchMapping("/{todoId}")
    public Resource<TodoDto> update(@PathVariable("todoId") Integer id,
                                                @Validated(ModifyGroup.class)
                                                @JsonView(TodoViews.Input.class)
                                                @RequestBody TodoDto updatedTodo) {
        log.trace("update todo");
        TodoDto result = todoService.update(id, updatedTodo);
        return getResource(result);
    }

    @DeleteMapping("/{todoId}")
    public void deleteById(@PathVariable("todoId") Integer id) {
        log.trace("delete todo by id");
        todoService.delete(id);
    }

    @DeleteMapping
    public void deleteAll(){
        log.trace("delete all todos");
        todoService.deleteAll();
    }

    private Resource<TodoDto> getResource(TodoDto todo) {
        String url = linkTo(methodOn(this.getClass()).getById(todo.getId())).withSelfRel().getHref();
        return new Resource<>(todo, url);
    }
}
