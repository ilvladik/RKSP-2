package flux.prak7;

import flux.prak7.controllers.NoteController;
import flux.prak7.models.Note;
import flux.prak7.repositories.NoteRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class NoteControllerTest {

    // Ильин Владислав Викторович ИКБО-01-21
    @Test
    public void testGetNoteById() {
        Note note = new Note();
        note.setId(1L);
        note.setTitle("Test");
        note.setDescription("Test");

        NoteRepository repo = Mockito.mock(NoteRepository.class);
        when(repo.findById(1L)).thenReturn(Mono.just(note));

        NoteController controller = new NoteController(repo);

        ResponseEntity<Note> response = controller.findById(1L).block();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(note, response.getBody());
    }

    // Ильин Владислав Викторович ИКБО-01-21
    @Test
    public void testGetAllNotes() {
        Note note = new Note();
        note.setId(1L);
        note.setTitle("Test");
        note.setDescription("Test");
        Note note1 = new Note();
        note1.setId(2L);
        note1.setTitle("Test1");
        note1.setDescription("Test1");
        NoteRepository catRepository = Mockito.mock(NoteRepository.class);
        when(catRepository.findAll()).thenReturn(Flux.just(note, note1));
        NoteController controller = new NoteController(catRepository);
        Flux<Note> response = controller.getAll();
        assertEquals(2, response.collectList().block().size());
    }

    // Ильин Владислав Викторович ИКБО-01-21
    @Test
    public void testCreateNote() {
        Note note = new Note();
        note.setId(1L);
        note.setTitle("Test");
        note.setDescription("Test");

        NoteRepository repo = Mockito.mock(NoteRepository.class);
        when(repo.save(note)).thenReturn(Mono.just(note));

        NoteController controller = new NoteController(repo);

        ResponseEntity<Note> response = controller.create(note).block();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(note, response.getBody());;
    }

    @Test
    public void testUpdateCat() {
        Note note = new Note();
        note.setId(1L);
        note.setTitle("Test");
        Note note1 = new Note();
        note1.setId(1L);
        note1.setTitle("TestUpdated");
        NoteRepository noteRepository = Mockito.mock(NoteRepository.class);
        when(noteRepository.findById(1L)).thenReturn(Mono.just(note));

        when(noteRepository.save(note)).thenReturn(Mono.just(note1));
        NoteController controller = new NoteController(noteRepository);
        ResponseEntity<Note> response = controller.update(1L,
                note1).block();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(note1, response.getBody());
    }

    @Test
    public void testDeleteCat() {
        Note note = new Note();
        note.setId(1L);
        note.setTitle("Test");

        NoteRepository noteRepository = Mockito.mock(NoteRepository.class);
        when(noteRepository.findById(1L)).thenReturn(Mono.just(note));
        when(noteRepository.delete(note)).thenReturn(Mono.empty());
        NoteController controller = new NoteController(noteRepository);
        ResponseEntity<Void> response = controller.delete(1L).block();
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
