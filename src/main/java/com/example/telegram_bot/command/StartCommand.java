package com.example.telegram_bot.command;

import com.example.telegram_bot.Entity.User;
import com.example.telegram_bot.bot.Music_bot;
import com.example.telegram_bot.repository.entity.TelegramUser;
import com.example.telegram_bot.service.SendBotMessageService;
import com.example.telegram_bot.service.TelegramUserService;
import com.example.telegram_bot.state.State;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

import static com.example.telegram_bot.command.CommandName.*;

public class StartCommand implements State {
    private final SendBotMessageService sendBotMessageService;
    private final TelegramUserService telegramUserService;
    private final Long adminID = Long.valueOf(1395425257);

    public final static String START_MESSAGE = "Приветствую!\nЭто телеграм-бот, где ты можешь получить песню под свое настроение!";

    public StartCommand(SendBotMessageService sendBotMessageService, TelegramUserService telegramUserService) {
        this.sendBotMessageService = sendBotMessageService;
        this.telegramUserService = telegramUserService;
    }

    @Override
    public void startState(Update update, User user) {
        ReplyKeyboardMarkup markupInline = new ReplyKeyboardMarkup();
        markupInline.setResizeKeyboard(true);
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        KeyboardButton getSong = new KeyboardButton();
        getSong.setText(BUTTONGETSONG.getCommandName());
        row1.add(getSong);
        keyboardRows.add(row1);

        if (user.getChatId().equals(adminID)) {
            KeyboardRow folderRow = new KeyboardRow();
            KeyboardRow songRow = new KeyboardRow();

            KeyboardButton addFolder = new KeyboardButton();
            addFolder.setText(ADDFOLDER.getCommandName());

            KeyboardButton updFolder = new KeyboardButton();
            updFolder.setText(UPDATEFOLDER.getCommandName());

            KeyboardButton deleteFolder = new KeyboardButton();
            deleteFolder.setText(DELETEFOLDER.getCommandName());

            folderRow.add(addFolder);
            folderRow.add(updFolder);
            folderRow.add(deleteFolder);

            KeyboardButton addSong = new KeyboardButton();
            addSong.setText(ADDSONG.getCommandName());

            KeyboardButton deleteSong = new KeyboardButton();
            deleteSong.setText(DELETESONG.getCommandName());

            songRow.add(addSong);
            songRow.add(deleteSong);

            keyboardRows.add(folderRow);
            keyboardRows.add(songRow);
        }

        markupInline.setKeyboard(keyboardRows);
        sendBotMessageService.setReplyMarkup(markupInline);

        sendBotMessageService.sendMessage(update.getMessage().getChatId(), START_MESSAGE);
        execute(update, user);
    }

    @Override
    public void execute(Update update, User user) {
        telegramUserService.findByChatId(user.getChatId()).ifPresentOrElse(
                Olduser -> {},
                () -> {
                    TelegramUser newUser = new TelegramUser();
                    newUser.setChatId(user.getChatId());
                    newUser.setUsername(update.getMessage().getFrom().getUserName());
                    newUser.setFirstName(update.getMessage().getFrom().getFirstName());
                    newUser.setLastName(update.getMessage().getFrom().getLastName());
                    telegramUserService.save(newUser);
                }
        );
        user.setState(new NoCommand(sendBotMessageService));
    }

    @Override
    public void undo() {

    }

}
