package org.example.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TaskDTO {
    @NotBlank(message = "Title is required")
    private String title;

    private String description;
}