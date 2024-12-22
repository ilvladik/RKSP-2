package ru.ilyin.prak4client.models;

import java.util.List;

public class NoteListWrapper {
    private List<NoteRequest> noteRequests;

    public List<NoteRequest> getNotes() {
        return noteRequests;
    }

    public void setNotes(List<NoteRequest> noteRequests) {
        this.noteRequests = noteRequests;
    }
}
