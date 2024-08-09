package com.latoken.bot;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Commands {
    RAG("/rag"),
    START("/start");

    private String message;
}
