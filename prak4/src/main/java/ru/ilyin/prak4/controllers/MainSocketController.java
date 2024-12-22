package ru.ilyin.prak4.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.ilyin.prak4.dtos.NoteDto;
import ru.ilyin.prak4.entities.Note;
import ru.ilyin.prak4.repositories.NoteRepository;

@Controller
public class MainSocketController {
    private  final NoteRepository noteRepository;

    // Ильин Владислав Викторович ИКБО-01-21
    @Autowired
    public MainSocketController(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @MessageMapping("getNote")
    public Mono<Note> getNote(Long id) {
        return Mono.justOrEmpty(noteRepository.findNoteById(id));
    }

    @MessageMapping("addNote")
    public Mono<Note> addNote(NoteDto noteDto) {
        Note note = new Note();
        note.setTitle(noteDto.getTitle());
        note.setDescription(noteDto.getDescription());
        return Mono.justOrEmpty(noteRepository.save(note));
    }

    @MessageMapping("getNotes")
    public Flux<Note> getNotes() {
        return Flux.fromIterable(noteRepository.findAll());
    }

    @MessageMapping("deleteNote")
    public Mono<Void> deleteNote(Long id){
        Note note = noteRepository.findNoteById(id);
        noteRepository.delete(note);
        return Mono.empty();
    }

    @MessageMapping("noteChannel")
    public Flux<Note> noteChannel(Flux<NoteDto> notes){
        return notes
                .flatMap(noteDto -> Mono
                        .fromCallable(() -> {
                            Note note = new Note();
                            note.setTitle(noteDto.getTitle());
                            note.setDescription(noteDto.getDescription());
                            return noteRepository.save(note);
                        }))
                .collectList()
                .flatMapMany(Flux::fromIterable);
    }
}
