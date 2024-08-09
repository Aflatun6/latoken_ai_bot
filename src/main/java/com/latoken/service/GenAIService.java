package com.latoken.service;

import com.latoken.controller.dto.ChatRequest;
import com.latoken.model.BookModel;

public interface GenAIService {

    String getResponse(ChatRequest request);

    String getResponseExtended(ChatRequest request);

    BookModel getBookModelFromText(String question);
}