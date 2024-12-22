package ru.ilyin.prak4.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data

@AllArgsConstructor
public class NoteDto {
    private String title;
    private String description;
}
