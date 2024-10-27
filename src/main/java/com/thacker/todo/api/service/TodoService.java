package com.thacker.todo.api.service;


import com.thacker.todo.api.entity.Todo;
import com.thacker.todo.api.exception.NotFoundException;
import com.thacker.todo.api.model.TodoDto;
import com.thacker.todo.api.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TodoService {

	private final TodoRepository todoRepository;
	private final ModelMapper modelMapper;

	public TodoDto createTodo(TodoDto todoDto){
		Todo todo = modelMapper.map(todoDto, Todo.class);
		Todo created = todoRepository.save(todo);
		log.debug("Created todo. Id: {}", created.getId());
		return modelMapper.map(created, TodoDto.class);
	}

	public TodoDto getTodo(Integer id){
		Optional<Todo> todo = todoRepository.findById(id);
		return todo.map(entity->modelMapper.map(entity, TodoDto.class))
				.orElseThrow(()->new NotFoundException("Todo not found"));
	}

	public List<TodoDto> getAllTodo(){
		Iterable<Todo> todos = todoRepository.findAll();

		return StreamSupport.stream(todos.spliterator(), false)
				.map(t -> modelMapper.map(t, TodoDto.class)).toList();
	}

	public TodoDto update(Integer id, TodoDto todoDto) {
		Optional<Todo> todo = todoRepository.findById(id);
		return todo.map(entity ->{
			entity.update(todoDto);
			Todo updated = todoRepository.save(entity);
			log.debug("Updated todo. Id: {}", updated.getId());
			return modelMapper.map(updated, TodoDto.class);
		}).orElseThrow(()->new NotFoundException(String.format("Todo not found for id %s", id)));
	}

	public void delete(Integer id){
        if (todoRepository.existsById(id)) {
            todoRepository.deleteById(id);
			log.debug("Deleted todo. Id: {}", id);
        }else {
			throw new NotFoundException(String.format("Todo not found for id %s", id));
		}
	}

	public void deleteAll(){
		todoRepository.deleteAll();
	}
}
