package com.example.telegram_bot.command.admin.updatefolder;

import com.example.telegram_bot.Entity.User;
import com.example.telegram_bot.command.NoCommand;
import com.example.telegram_bot.repository.entity.MoodFolder;
import com.example.telegram_bot.service.MoodFolderService;
import com.example.telegram_bot.service.SendBotMessageService;
import com.example.telegram_bot.state.State;
import org.telegram.telegrambots.meta.api.objects.Update;

public class UpdateFolderNameCommand implements State {
    private final SendBotMessageService sendBotMessageService;
    private final MoodFolderService moodFolderService;
    private final MoodFolder moodFolder;

    public UpdateFolderNameCommand(SendBotMessageService sendBotMessageService, MoodFolderService moodFolderService, MoodFolder moodFolder) {
        this.sendBotMessageService = sendBotMessageService;
        this.moodFolderService = moodFolderService;
        this.moodFolder = moodFolder;
    }

    @Override
    public void startState(Update update, User user) {
        sendBotMessageService.sendMessage(user.getChatId(), "Введите новое название папки");
    }

    @Override
    public void execute(Update update, User user) {
        if (update.hasMessage()) {
            String newName = update.getMessage().getText();

            moodFolder.setFolderName(newName);
            moodFolderService.save(moodFolder);
            sendBotMessageService.sendMessage(user.getChatId(), "Папка успешно изменена!");
            user.setState(new NoCommand(sendBotMessageService));
        }
    }

    @Override
    public void undo() {

    }
}
