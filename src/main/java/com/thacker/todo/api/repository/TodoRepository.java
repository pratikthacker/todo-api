package com.thacker.todo.api.repository;

import com.thacker.todo.api.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Integer> {

}
