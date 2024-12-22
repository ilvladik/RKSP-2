package ru.ilyin.prak4client.models;

import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@RequiredArgsConstructor
public class NoteResponse {
    private Long id;
    private String title;
    private String description;
}

