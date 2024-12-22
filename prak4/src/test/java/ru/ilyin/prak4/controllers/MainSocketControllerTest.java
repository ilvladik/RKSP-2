package ru.ilyin.prak4.controllers;

import io.rsocket.frame.decoder.PayloadDecoder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import ru.ilyin.prak4.entities.Note;
import ru.ilyin.prak4.repositories.NoteRepository;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MainSocketControllerTest {
    @Autowired
    private NoteRepository noteRepository;

    private RSocketRequester requester;

    @BeforeEach
    public void setup() {
        requester = RSocketRequester.builder()
                .rsocketStrategies(builder -> builder
                        .decoder(new Jackson2JsonDecoder()))
                .rsocketStrategies(builder -> builder
                        .encoder(new Jackson2JsonEncoder()))
                .rsocketConnector(connector -> connector
                        .payloadDecoder(PayloadDecoder.ZERO_COPY)
                        .reconnect(Retry.fixedDelay(2, Duration.ofSeconds(2))))
                .dataMimeType(MimeTypeUtils.APPLICATION_JSON)
                .tcp("localhost", 5200);
    }

    @AfterEach void cleanUp() {
        requester.dispose();
    }

    @Test
    public void testGetNote() {
        Note note = new Note();
        note.setTitle("TestNote");
        note.setDescription("TestNoteDescription");

        Note savedNote = noteRepository.save(note);
        Mono<Note> result = requester.route("getNote")
                .data(savedNote.getId())
                .retrieveMono(Note.class);
        assertNotNull(result.block());
    }
    @Test
    public void testAddNote() {
        Note note = new Note();
        note.setTitle("TestNote");
        note.setDescription("TestNoteDescription");

        Mono<Note> result = requester.route("addNote")
                .data(note)
                .retrieveMono(Note.class);
        Note savedNote = result.block();
        assertNotNull(savedNote);
        assertNotNull(savedNote.getId());
        assertTrue(savedNote.getId() > 0);
    }

    @Test
    public void testDeleteNote() {
        Note note = new Note();
        note.setTitle("TestNote");
        note.setDescription("TestNoteDescription");

        Note savedNote = noteRepository.save(note);
        Mono<Void> result = requester.route("deleteNote")
                .data(savedNote.getId())
                .send();
        result.block();
        Note deletedNote = noteRepository.findNoteById(savedNote.getId());
        assertNotSame(deletedNote, savedNote);
    }
    @Test
    public void testGetNotes() {
        Flux<Note> result = requester.route("getNotes")
                .retrieveFlux(Note.class);

        assertNotNull(result.collectList().block());
    }
}
