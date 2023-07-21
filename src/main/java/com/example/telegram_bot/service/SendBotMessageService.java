package com.example.telegram_bot.service;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public interface SendBotMessageService {

    /**
     * Send message via telegram bot.
     *
     * @param chatId provided chatId in which messages would be sent.
     * @param message provided message to be sent.
     */
    void sendMessage(Long chatId, String message);

    /**
     * Set reply markup via telegram bot.
     */
    void setReplyMarkup(InlineKeyboardMarkup markup);

    void setReplyMarkup(ReplyKeyboardMarkup markup);
}
