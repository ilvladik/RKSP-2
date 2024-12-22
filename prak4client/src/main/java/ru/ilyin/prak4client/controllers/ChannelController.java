package ru.ilyin.prak4client.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.ilyin.prak4client.models.NoteRequest;
import ru.ilyin.prak4client.models.NoteListWrapper;
import ru.ilyin.prak4client.models.NoteResponse;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class ChannelController {

    // ИКБО-01-21 Ильин Владислав Викторович
    private final RSocketRequester rSocketRequester;

    @Autowired
    public ChannelController(RSocketRequester rSocketRequester) {
        this.rSocketRequester = rSocketRequester;
    }

    @PostMapping("/exp")
    public Flux<NoteResponse> addNotesMultiple(@RequestBody NoteListWrapper noteListWrapper){
        List<NoteRequest> noteRequestList = noteListWrapper.getNotes();
        Flux<NoteRequest> notes = Flux.fromIterable(noteRequestList);
        return rSocketRequester
                .route("noteChannel")
                .data(notes)
                .retrieveFlux(NoteResponse.class);
    }
}
