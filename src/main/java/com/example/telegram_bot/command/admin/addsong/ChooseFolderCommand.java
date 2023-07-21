package com.example.telegram_bot.command.admin.addsong;

import com.example.telegram_bot.Entity.User;
import com.example.telegram_bot.bot.Music_bot;
import com.example.telegram_bot.repository.entity.MoodFolder;
import com.example.telegram_bot.service.SendBotMessageService;
import com.example.telegram_bot.service.MoodFolderService;
import com.example.telegram_bot.service.TelegramMusicService;
import com.example.telegram_bot.state.State;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

public class ChooseFolderCommand implements State {
    private final SendBotMessageService sendBotMessageService;
    private final MoodFolderService moodFolderService;
    private final Music_bot music_bot;
    private final List<String> similarSongs;
    private final TelegramMusicService telegramMusicService;

    private List<MoodFolder> moodFolders;
    private List<MoodFolder> chooseFolders;

    private final static String REQUEST_MESSAGE = "Выберите папку/папки, в которой должна находиться песня (Пример: 1 2 3)";

    public ChooseFolderCommand(
            SendBotMessageService sendBotMessageService,
            MoodFolderService moodFolderService,
            TelegramMusicService telegramMusicService,
            Music_bot music_bot,
            List<String> similarSongs) {
        this.sendBotMessageService = sendBotMessageService;
        this.moodFolderService = moodFolderService;
        this.telegramMusicService = telegramMusicService;
        this.music_bot = music_bot;
        this.similarSongs = similarSongs;
    }

    @Override
    public void startState(Update update, User user) {
        String folders = "";

        moodFolders = moodFolderService.findAll();
        for (int i = 0; i < moodFolders.size(); i++) {
            folders += i+1 + ". " + moodFolders.get(i).getFolderName() + "\n";
        }

        if (moodFolders.size() > 0) {
            sendBotMessageService.sendMessage(user.getChatId(), "Список существующих папок настроений:\n" + folders);
            sendBotMessageService.sendMessage(user.getChatId(), REQUEST_MESSAGE);
        }
        else {
            sendBotMessageService.sendMessage(user.getChatId(), "Папок не найдено. Пожалуйста, добавьте папки");
        }

    }

    @Override
    public void execute(Update update, User user) {
        if (update.getMessage().hasText()) {
            String[] numbers = update.getMessage().getText().trim().split(" ");
            chooseFolders = new ArrayList<>();
            try {
                for (String number : numbers) {
                    int numberFolder = Integer.parseInt(number)-1;

                    if (numberFolder < moodFolders.size() && numberFolder >= 0) {
                        chooseFolders.add(moodFolders.get(numberFolder));
                    }
                    else {
                        sendBotMessageService.sendMessage(user.getChatId(), "Такой папки нет!");
                        return;
                    }
                }
                user.setState(new ConfirmCommand(sendBotMessageService, telegramMusicService, chooseFolders, similarSongs, music_bot));
            }
            catch (NumberFormatException e) {
                System.out.println(e.getMessage());
                sendBotMessageService.sendMessage(user.getChatId(), REQUEST_MESSAGE);
                return;
            }
        }
        else sendBotMessageService.sendMessage(user.getChatId(), REQUEST_MESSAGE);
    }

    @Override
    public void undo() {

    }

}
