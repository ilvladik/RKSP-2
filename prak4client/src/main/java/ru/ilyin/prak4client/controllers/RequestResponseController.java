package ru.ilyin.prak4client.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.ilyin.prak4client.models.NoteRequest;
import ru.ilyin.prak4client.models.NoteResponse;

@RestController
@RequestMapping("/api/notes")
public class RequestResponseController {

    // ИКБО-01-21 Ильин Владислав Викторович
    private final RSocketRequester rSocketRequester;

    @Autowired
    public RequestResponseController(RSocketRequester rSocketRequester) {
        this.rSocketRequester = rSocketRequester;
    }

    @GetMapping("/{id}")
    public Mono<NoteRequest> getNote(@PathVariable Long id) {
        return rSocketRequester
                .route("getNote")
                .data(id)
                .retrieveMono(NoteRequest.class);
    }

    @PostMapping
    public Mono<NoteResponse> addNote(@RequestBody NoteRequest noteRequest) {
        return  rSocketRequester
                .route("addNote")
                .data(noteRequest)
                .retrieveMono(NoteResponse.class);
    }
}
