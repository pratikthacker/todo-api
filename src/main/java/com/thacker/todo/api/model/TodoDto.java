package com.thacker.todo.api.model;

import static com.thacker.todo.api.model.validation.ValidationGroups.*;

import com.fasterxml.jackson.annotation.JsonView;
import com.thacker.todo.api.model.view.TodoViews;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TodoDto {

    private Integer id;

    @NotBlank(groups = CreateGroup.class, message = "must be present")
    @Size (min = 1, groups = ModifyGroup.class, message = "cannot be empty, if present")
    @JsonView({TodoViews.Input.class})
    private String title;

    @Builder.Default
    @JsonView({TodoViews.Input.class})
    private Boolean completed = false;

    @JsonView({TodoViews.Input.class})
    private Integer order;
}
