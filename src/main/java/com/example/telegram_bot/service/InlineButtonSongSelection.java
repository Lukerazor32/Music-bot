package com.example.telegram_bot.service;

import com.example.telegram_bot.dto.CurrentSongList;
import com.example.telegram_bot.dto.Song;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.toIntExact;

public class InlineButtonSongSelection implements InlineButtonService {

    private int lastIndex = 0;
    private List<CurrentSongList> songs;
    private List<InlineKeyboardButton> rowInline;
    private InlineKeyboardMarkup markupInline;


    public InlineButtonSongSelection(List<CurrentSongList> songs) {
        this.songs = songs;
    }


    @Override
    public InlineKeyboardMarkup getReplyMarkup() {
        markupInline = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        setRowInline();

        rowsInline.add(rowInline);

        // Add it to the message
        markupInline.setKeyboard(rowsInline);

        return markupInline;
    }

    public void setRowInline() {
        rowInline = new ArrayList<>();
        for (int i = 0; i < songs.size(); i++) {
            InlineKeyboardButton inlineButtonNumb = new InlineKeyboardButton();
            inlineButtonNumb.setText(String.valueOf(i+1));
            inlineButtonNumb.setCallbackData(String.valueOf(i));
            rowInline.add(inlineButtonNumb);
        }
    }

//    public List<InlineKeyboardButton> getPrevNextKeyboard() {
//        List<InlineKeyboardButton> rowInlinePN = new ArrayList<>();
//
//        if (lastIndex == 0) {
//            result = "";
//            rowInline = new ArrayList<>();
//
//            for (int i = 0; i < songs.size(); i++) {
//                if (i % 5 == 0 && i != 0) {
//                    lastIndex = i;
//                    break;
//                }
//                String song = songs.get(i).toString();
//                result += i+1 + ". " + song + "\n\n";
//
//                InlineKeyboardButton inlineButtonNumb = new InlineKeyboardButton();
//                inlineButtonNumb.setText(String.valueOf(i+1));
//                inlineButtonNumb.setCallbackData(String.valueOf(i+1));
//                rowInline.add(inlineButtonNumb);
//            }
//        }
//
//        if (lastIndex > 5) {
//            InlineKeyboardButton inlineButtonPrev = new InlineKeyboardButton();
//            inlineButtonPrev.setText("⬅");
//            inlineButtonPrev.setCallbackData("prev");
//            rowInlinePN.add(inlineButtonPrev);
//        }
//
//        if (lastIndex < songs.size()) {
//            InlineKeyboardButton inlineButtonNext = new InlineKeyboardButton();
//            inlineButtonNext.setText("➡");
//            inlineButtonNext.setCallbackData("next");
//            rowInlinePN.add(inlineButtonNext);
//        }
//
//        return rowInlinePN;
//    }

    @Override
    public EditMessageText changeMessage(Update update) {
        EditMessageText newMessage = new EditMessageText();
        String callData = update.getCallbackQuery().getData();
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        long messageId = update.getCallbackQuery().getMessage().getMessageId();
        rowInline = new ArrayList<>();

        try {
            if (callData.equals("next")) {
                for (int i = lastIndex; i < songs.size(); i++) {
                    if (i % 5 == 0 && i != lastIndex) {
                        lastIndex = i;
                        break;
                    }
                    InlineKeyboardButton inlineButtonNumb = new InlineKeyboardButton();
                    inlineButtonNumb.setText(String.valueOf(i+1));
                    inlineButtonNumb.setCallbackData(String.valueOf(i+1));
                    rowInline.add(inlineButtonNumb);
                }
            }
        }
        catch (IndexOutOfBoundsException e) {}

        try {
            if (callData.equals("prev")) {
                int index;

                if (lastIndex-10 == 0) {
                    index = 0;
                }
                else {
                    index = lastIndex-10;
                }

                for (int i = index; i < songs.size(); i++) {
                    if (i % 5 == 0 && i != index) {
                        lastIndex = i;
                        break;
                    }

                    InlineKeyboardButton inlineButtonNumb = new InlineKeyboardButton();
                    inlineButtonNumb.setText(String.valueOf(i+1));
                    inlineButtonNumb.setCallbackData(String.valueOf(i+1));
                    rowInline.add(inlineButtonNumb);
                }
            }
        }
        catch (IndexOutOfBoundsException e) {}

        newMessage.setChatId(chatId.toString());
        newMessage.setMessageId(toIntExact(messageId));
        newMessage.setReplyMarkup(getReplyMarkup());
        return newMessage;
    }

    public CurrentSongList getCallBackResponse(Update update) {
        String callData = update.getCallbackQuery().getData();
        for (int i = 0; i < rowInline.size(); i++) {
            String songNumber = rowInline.get(i).getCallbackData();

            if (songNumber.equals(callData)) {
                int songIndex = Integer.parseInt(songNumber);
                return songs.get(songIndex);
            }
        }
        return null;
    }
}
