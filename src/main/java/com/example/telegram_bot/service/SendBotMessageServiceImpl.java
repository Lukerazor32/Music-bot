package com.example.telegram_bot.service;

import com.example.telegram_bot.bot.Music_bot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Implementation of {@link SendBotMessageService} interface.
 */
@Service
public class SendBotMessageServiceImpl implements SendBotMessageService {

    private final Music_bot music_bot;

    private SendMessage sendMessage = new SendMessage();

    @Autowired
    public SendBotMessageServiceImpl(Music_bot music_bot) {
        this.music_bot = music_bot;
    }

    @Override
    public void sendMessage(Long chatId, String message) {
        sendMessage.setChatId(chatId.toString());
        sendMessage.enableHtml(false);
        sendMessage.setText(message);

        try {
            music_bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        finally {
            sendMessage = new SendMessage();
        }
    }

    @Override
    public void setReplyMarkup(InlineKeyboardMarkup markup) {
        sendMessage.setReplyMarkup(markup);
    }

    @Override
    public void setReplyMarkup(ReplyKeyboardMarkup markup) {
        sendMessage.setReplyMarkup(markup);
    }
}
