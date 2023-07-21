package com.example.telegram_bot.service;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.toIntExact;

public class InlineButtonAcceptCancel implements InlineButtonService {
    @Override
    public InlineKeyboardMarkup getReplyMarkup() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        List<InlineKeyboardButton> rowInlineAC = new ArrayList<>();

        InlineKeyboardButton inlineButtonAccept = new InlineKeyboardButton();
        inlineButtonAccept.setText("✅");
        inlineButtonAccept.setCallbackData("accept");
        rowInlineAC.add(inlineButtonAccept);

        InlineKeyboardButton inlineButtonCancel = new InlineKeyboardButton();
        inlineButtonCancel.setText("❌");
        inlineButtonCancel.setCallbackData("cancel");
        rowInlineAC.add(inlineButtonCancel);

        rowsInline.add(rowInlineAC);

        // Add it to the message
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    @Override
    public EditMessageText changeMessage(Update update) {
        return null;
    }


    public DeleteMessage deleteMessage(Update update) {
        long messageId = update.getCallbackQuery().getMessage().getMessageId();

        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setMessageId(toIntExact(messageId));
        deleteMessage.setChatId(update.getCallbackQuery().getMessage().getChatId().toString());

        return deleteMessage;
    }
}
