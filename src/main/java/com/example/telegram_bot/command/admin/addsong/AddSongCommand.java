package com.example.telegram_bot.command.admin.addsong;

import com.example.telegram_bot.Entity.User;
import com.example.telegram_bot.bot.Music_bot;
import com.example.telegram_bot.dto.CurrentSongList;
import com.example.telegram_bot.service.*;
import com.example.telegram_bot.state.State;
import com.example.telegram_bot.thread.DownloadSongsScraping;
import com.example.telegram_bot.thread.SimilarSongsScraping;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class AddSongCommand implements State {
    private final SendBotMessageService sendBotMessageService;
    private final MoodFolderService moodFolderService;
    private final TelegramMusicService telegramMusicService;
    private final Music_bot music_bot;
    private List<CurrentSongList> currentSongList;

    private InlineButtonSongSelection inlineButtonSongSelection;
    private SimilarSongsRequest similarSongsRequest;

    public AddSongCommand(SendBotMessageService sendBotMessageService, MoodFolderService moodFolderService, TelegramMusicService telegramMusicService, Music_bot music_bot) {
        this.sendBotMessageService = sendBotMessageService;
        this.moodFolderService = moodFolderService;
        this.telegramMusicService = telegramMusicService;
        this.music_bot = music_bot;
    }

    @Override
    public void startState(Update update, User user) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId(), "Введи название песни!");
    }

    @Override
    public void execute(Update update, User user) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            similarSongsRequest = new SimilarSongsRequest();

            currentSongList = similarSongsRequest.getSongs(update.getMessage().getText());

            if (currentSongList != null) {
                inlineButtonSongSelection = new InlineButtonSongSelection(currentSongList);
                sendBotMessageService.setReplyMarkup(inlineButtonSongSelection.getReplyMarkup());

                String result = "";
                for (int i = 0; i < currentSongList.size(); i++) {
                    result += i+1 + ". " + currentSongList.get(i) + "\n";
                }

                sendBotMessageService.sendMessage(user.getChatId(), result);
            }
            else {
                sendBotMessageService.sendMessage(user.getChatId(), "Не найдено");
                return;
            }
        }

        else if (update.hasCallbackQuery()) {
            CurrentSongList currentSong;
            try {
                currentSong = inlineButtonSongSelection.getCallBackResponse(update);
            } catch (NullPointerException e) {
                return;
            }


            ExecutorService executor = Executors.newFixedThreadPool(8);
            executor.submit(new SimilarSongsScraping(similarSongsRequest, currentSong));

            while (similarSongsRequest.getSimilarSongs().size() < 7) {}

            for (int i = 0; i < 7; i++) {
                executor.submit(new SimilarSongsScraping(similarSongsRequest, currentSong));
            }

            executor.shutdown();
            while (!executor.isTerminated()) {}


            List<String> similarSongs = similarSongsRequest.getSimilarSongs();

            user.setState(new ChooseFolderCommand(
                    sendBotMessageService,
                    moodFolderService,
                    telegramMusicService,
                    music_bot,
                    similarSongs));
        }
    }

    @Override
    public void undo() {
        if (currentSongList != null) {
            currentSongList.clear();
        }
    }

}
