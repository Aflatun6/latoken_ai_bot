package com.latoken.controller;

import com.latoken.service.ContentIngestor;
import com.latoken.service.EmbeddingComponent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LoadController {

    private final EmbeddingComponent embeddingComponent;
    private final ContentIngestor contentIngestor;

    @GetMapping("/loader/single")
    public void loadSingle() {
        log.info("Loading single document start");
        embeddingComponent.loadSingleDocument();
        log.info("Loading single document end");
    }

    @GetMapping("/loader/latoken")
    public void loadLatoken() throws IOException {
        log.info("Loading single document start");
        contentIngestor.ingestContent("https://www.canva.com/design/DAFmGtEKkWs/view?embed#1" , null);
        log.info("Loading single document end");
    }
}