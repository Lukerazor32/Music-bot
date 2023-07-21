package com.example.telegram_bot.command.getsong;

import com.example.telegram_bot.Entity.User;
import com.example.telegram_bot.bot.Music_bot;
import com.example.telegram_bot.repository.entity.MoodFolder;
import com.example.telegram_bot.repository.entity.TelegramSong;
import com.example.telegram_bot.service.*;
import com.example.telegram_bot.state.State;
import com.example.telegram_bot.thread.FindLinkScraping;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Audio;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InputMediaAudio;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.SendAudio;
import com.pengrad.telegrambot.response.MessagesResponse;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.stereotype.Component;
import com.pengrad.telegrambot.request.SendMediaGroup;
import org.telegram.telegrambots.meta.api.objects.Update;
import com.pengrad.telegrambot.model.request.InputMedia;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class GetMoodCommand implements State {
    private final SendBotMessageService sendBotMessageService;
    private final MoodFolderService moodFolderService;
    private final TelegramMusicService telegramMusicService;
    private final Music_bot music_bot;

    private SendMediaGroup sendMediaGroup;

    private final static String TECHWORKSMESSAGE = "В данный момент ведутся технические работы. Приходите позже";

    public GetMoodCommand(SendBotMessageService sendBotMessageService, MoodFolderService moodFolderService, TelegramMusicService telegramMusicService, Music_bot music_bot) {
        this.sendBotMessageService = sendBotMessageService;
        this.moodFolderService = moodFolderService;
        this.telegramMusicService = telegramMusicService;

        this.music_bot = music_bot;
    }

    @Override
    public void startState(Update update, User user) {
        List<MoodFolder> moodFolders = moodFolderService.findAll();
        if (moodFolders.size() >= 1) {
            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

            List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

            List<InlineKeyboardButton> rowInline = new ArrayList<>();

            for (MoodFolder moodFolder : moodFolders) {
                if (moodFolder.getSongs().size() >= 100) {
                    InlineKeyboardButton inlineButtonNumb = new InlineKeyboardButton();
                    inlineButtonNumb.setText(moodFolder.getFolderName());
                    inlineButtonNumb.setCallbackData(String.valueOf(moodFolder.getId()));
                    rowInline.add(inlineButtonNumb);
                }
            }
            if (rowInline.size() >= 1) {
                rowsInline.add(rowInline);

                markupInline.setKeyboard(rowsInline);
                sendBotMessageService.setReplyMarkup(markupInline);
                sendBotMessageService.sendMessage(user.getChatId(), "Какое у вас сейчас настроение?");
            } else {
                sendBotMessageService.sendMessage(user.getChatId(), TECHWORKSMESSAGE);
            }
        }
        else {
            sendBotMessageService.sendMessage(user.getChatId(), TECHWORKSMESSAGE);
        }
    }

    @Override
    public void execute(Update update, User user) {
        if (update.hasCallbackQuery()) {
            String callBackData = update.getCallbackQuery().getData();
            for(MoodFolder moodFolder : moodFolderService.findAll()) {
                if (Integer.parseInt(callBackData) == moodFolder.getId()) {
                    List<TelegramSong> songListSynchronized = new CopyOnWriteArrayList<>(moodFolder.getSongs());

                    Random random = new Random();
                    for (int i = songListSynchronized.size()-1; i > 0; i--) {
                        TelegramSong swap = songListSynchronized.get(i);
                        int randomSong = random.nextInt(i+1);

                        songListSynchronized.set(i, songListSynchronized.get(randomSong));
                        songListSynchronized.set(randomSong, swap);
                    }

                    InputMediaAudio[] sendSongs = new InputMediaAudio[5];
                    DownloadSongRequest downloadRequest = new DownloadSongRequest();

                    ExecutorService executor = Executors.newFixedThreadPool(5);
                    for (int i = 0; i < 5; i++) {
                        Runnable scrapingLink = new FindLinkScraping(songListSynchronized, downloadRequest);

                        executor.execute(scrapingLink);
                    }
                    executor.shutdown();
                    while (!executor.isTerminated()) {}


                    Map<Long, String> findSongs = downloadRequest.getInputMedia();

                    List<Long> downloadId = new ArrayList<>(findSongs.keySet());
                    List<String> links = new ArrayList<>(findSongs.values());

                    for (int i = 0; i < links.size(); i++) {
                        sendSongs[i] = new InputMediaAudio(links.get(i));
                    }

                    telegramMusicService.deleteSongs(downloadRequest.getNullSongs());

                    try {
                        sendMediaGroup =new SendMediaGroup(user.getChatId(), sendSongs);

                        TelegramBot bot = new TelegramBot(music_bot.getBotToken());

                        MessagesResponse response = bot.execute(sendMediaGroup);
                        System.out.println(response);
                        for (Message m:
                             response.messages()) {
                            bot.execute(new DeleteMessage(user.getChatId(), m.messageId()));
                        }


//                        for (int i = 0; i < response.messages().length; i++) {
//                            int messageIndex = i;
//                            telegramMusicService.findByDownloadId(downloadId.get(i)).ifPresent(
//                                    telegramSong -> {
//                                        if (telegramSong.getFileId() == null) {
//
//                                            Message message = response.messages()[messageIndex];
//                                            telegramSong.setFileId(message.audio().fileId());
//
//                                            telegramMusicService.save(telegramSong);
//
//                                        }
//                                    }
//                            );
//                        }
                        return;
                    }
                    catch (IllegalArgumentException e) {
                        System.out.println(e);
                        sendBotMessageService.sendMessage(user.getChatId(), "Песни под такое настроение нет");
                        return;
                    }
                }
            }
        }
        else {
            sendBotMessageService.sendMessage(user.getChatId(), "Выберите настроение");
        }
    }

    @Override
    public void undo() {

    }
}
