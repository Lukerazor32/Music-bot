package com.example.telegram_bot.service;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;


public interface InlineButtonService {

    InlineKeyboardMarkup getReplyMarkup();

    EditMessageText changeMessage(Update update);
}
