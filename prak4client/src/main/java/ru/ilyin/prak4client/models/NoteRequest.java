package ru.ilyin.prak4client.models;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class NoteRequest {
    private String title;
    private String description;
}
