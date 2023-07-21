package com.example.telegram_bot.command.admin.addfolder;

import com.example.telegram_bot.Entity.User;
import com.example.telegram_bot.bot.Music_bot;
import com.example.telegram_bot.repository.entity.MoodFolder;
import com.example.telegram_bot.service.SendBotMessageService;
import com.example.telegram_bot.service.MoodFolderService;
import com.example.telegram_bot.state.State;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class AddFolderNameCommand implements State {
    private final SendBotMessageService sendBotMessageService;
    private final MoodFolderService moodFolderService;
    private final Music_bot music_bot;



    public AddFolderNameCommand(SendBotMessageService sendBotMessageService, MoodFolderService moodFolderService, Music_bot music_bot) {
        this.sendBotMessageService = sendBotMessageService;
        this.moodFolderService = moodFolderService;
        this.music_bot = music_bot;
    }


    @Override
    public void startState(Update update, User user) {
        StringBuilder textFolders = new StringBuilder();

        List<MoodFolder> folders = moodFolderService.findAll();
        for (int i = 0; i < folders.size(); i++) {
            MoodFolder folder = folders.get(i);
            textFolders.append(i+1 + ". " + folder.getFolderName() + "\nКоличество песен - " + folder.getSongs().size() + "\n\n");
        }

        sendBotMessageService.sendMessage(user.getChatId(), "Список существующих папок настроений:\n\n" + textFolders.toString());

        sendBotMessageService.sendMessage(user.getChatId(), "Введите название папки настроения");
    }

    @Override
    public void execute(Update update, User user) {
        if(update.hasMessage() && update.getMessage().hasText()) {
            String folderName = update.getMessage().getText();

            user.setState(new ConfirmCommand(sendBotMessageService, moodFolderService, folderName, music_bot));
        }
        else {
            sendBotMessageService.sendMessage(user.getChatId(), "Название папки должно содержать текст");
        }
    }

    @Override
    public void undo() {

    }

}
