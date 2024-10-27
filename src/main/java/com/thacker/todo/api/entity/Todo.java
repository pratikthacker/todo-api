package com.thacker.todo.api.entity;

import com.thacker.todo.api.base.entity.AbstractTimestampEntity;
import com.thacker.todo.api.model.TodoDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.io.Serializable;
import java.util.Optional;

import static jakarta.persistence.GenerationType.IDENTITY;


@Entity
@Table(name="TODO")
@Getter
@Setter
@NoArgsConstructor
public class Todo extends AbstractTimestampEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name="ID", unique = true, nullable = false)
	private Integer id;

	@Column(name="TITLE", nullable = false)
	private String title;

	@Column(name="COMPLETED", columnDefinition = "boolean default false", nullable = false)
	private Boolean completed;

	@Column(name="ORDER_VALUE")
	private Integer order;

	public void update(TodoDto updatedTodo){
		this.title = Optional.ofNullable(updatedTodo.getTitle()).orElse(title);
		this.completed=Optional.ofNullable(updatedTodo.getCompleted()).orElse(completed);
		this.order=	Optional.ofNullable(updatedTodo.getOrder()).orElse(order);
	}
}