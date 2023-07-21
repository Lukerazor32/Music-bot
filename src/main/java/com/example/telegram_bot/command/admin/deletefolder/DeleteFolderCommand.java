package com.example.telegram_bot.command.admin.deletefolder;

import com.example.telegram_bot.Entity.User;
import com.example.telegram_bot.bot.Music_bot;
import com.example.telegram_bot.command.NoCommand;
import com.example.telegram_bot.repository.entity.MoodFolder;
import com.example.telegram_bot.repository.entity.TelegramSong;
import com.example.telegram_bot.service.MoodFolderService;
import com.example.telegram_bot.service.SendBotMessageService;
import com.example.telegram_bot.service.TelegramMusicService;
import com.example.telegram_bot.state.State;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.*;

public class DeleteFolderCommand implements State {
    private final SendBotMessageService sendBotMessageService;
    private final MoodFolderService moodFolderService;
    private final TelegramMusicService telegramMusicService;

    private List<MoodFolder> folders;


    public DeleteFolderCommand(
            SendBotMessageService sendBotMessageService,
            MoodFolderService moodFolderService,
            TelegramMusicService telegramMusicService) {
        this.sendBotMessageService = sendBotMessageService;
        this.moodFolderService = moodFolderService;
        this.telegramMusicService = telegramMusicService;
    }

    @Override
    public void startState(Update update, User user) {
        folders = moodFolderService.findAll();

        String result = "Список существующих папок: \n\n";
        for (int i = 0; i < folders.size(); i++) {
            result += i+1 + ". " + folders.get(i).getFolderName() + "\n";
        }

        result += "\nОтправьте номер папки, которую вы хотите удалить";

        sendBotMessageService.sendMessage(user.getChatId(), result);
    }

    @Override
    public void execute(Update update, User user) {
        if (update.hasMessage()) {
            int fdNumb = 0;
            try {
                fdNumb = Integer.parseInt(update.getMessage().getText());
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
                sendBotMessageService.sendMessage(user.getChatId(), "Номер папки введен неверно");
                return;
            }
            if (fdNumb <= folders.size() && fdNumb > 0) {
                MoodFolder folder = folders.get(fdNumb-1);
                List<TelegramSong> songs = folder.getSongs();

                List<Long> nullSongIds = new ArrayList<>();

                for (TelegramSong ts : songs) {
                    ts.removeFolder(folder);
                    telegramMusicService.save(ts);
                    if (ts.getMoodFolders().size() == 0) {
                        nullSongIds.add(ts.getDownloadId());
                    }
                }

                moodFolderService.delete(folder);

                if (nullSongIds.size() != 0) {
                    telegramMusicService.deleteSongs(nullSongIds);
                }

                sendBotMessageService.sendMessage(user.getChatId(), "Папка удалена!");

                user.setState(new NoCommand(sendBotMessageService));
            }
            else sendBotMessageService.sendMessage(user.getChatId(), "Такой папки не существует"); return;
        }
    }

    @Override
    public void undo() {

    }
}
