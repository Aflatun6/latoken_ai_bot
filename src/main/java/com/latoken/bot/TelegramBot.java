package com.latoken.bot;

import com.latoken.controller.LoadController;
import com.latoken.service.RAGAssistant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private final String botName;

    @Autowired
    private RAGAssistant ragAssistant;

    @Autowired
    private LoadController loadController;

    public TelegramBot(String botName, String botToken) {
        super(botToken);
        this.botName = botName;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            var chatId = message.getChatId();
            log.info("Message received: {}", message.getChatId());
            var messageText = message.getText();
            boolean isRag = messageText.equals(Commands.RAG.getMessage());
            var response = isRag ? "rag was loaded" :
                    ragAssistant.chat(Math.toIntExact(chatId), messageText);
            if (isRag) {
                loadController.loadSingle();
//                try {
//                    loadController.loadLatoken();
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
            }
            try {
                execute(new SendMessage(chatId.toString(), response));
            } catch (TelegramApiException e) {
                log.error("Exception during processing telegram api: {}", e.getMessage());
            }
        }
    }

    @Override
    public String getBotUsername() {
        return this.botName;
    }
}