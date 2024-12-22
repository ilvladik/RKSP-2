package ru.ilyin.prak4client.controllers;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ilyin.prak4client.models.NoteRequest;
import ru.ilyin.prak4client.models.NoteResponse;

@RestController
@RequestMapping("/api/notes")
public class RequestStreamController {

    // ИКБО-01-21 Ильин Владислав Викторович
    private final RSocketRequester rSocketRequester;

    @Autowired
    public RequestStreamController(RSocketRequester rSocketRequester) {
        this.rSocketRequester = rSocketRequester;
    }

    @GetMapping
    public Publisher<NoteResponse> getNotes() {
        return rSocketRequester
                .route("getNotes")
                .retrieveFlux(NoteResponse.class);
    }



}
