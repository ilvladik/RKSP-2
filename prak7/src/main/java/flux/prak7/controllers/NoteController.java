package flux.prak7.controllers;

import flux.prak7.models.Note;
import flux.prak7.repositories.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/notes")
public class NoteController {
    // Ильин Владислав Викторович ИКБО-01-21
    private final NoteRepository noteRepository;

    @Autowired
    public NoteController(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Note>> findById(@PathVariable Long id) {
        return noteRepository.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Flux<Note> getAll() {
        return noteRepository.findAll()
                .onErrorResume(Flux::error)
                .onBackpressureBuffer();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<Note>> create(@RequestBody Note note) {
        return noteRepository.save(note)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Note>> update(@PathVariable Long id,
                                               @RequestBody Note updatedNote) {
        return noteRepository.findById(id)
                .flatMap(existingNote -> {
                    existingNote.setTitle(updatedNote.getTitle());
                    existingNote.setDescription(updatedNote.getDescription());
                    return noteRepository.save(existingNote);
                })
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable Long id) {
        return noteRepository.findById(id)
                .flatMap(existingNote ->
                        noteRepository.delete(existingNote)
                                .then(Mono.just(new
                                        ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
