package com.example.telegram_bot.command.getsong;

import com.example.telegram_bot.Entity.User;
import com.example.telegram_bot.bot.Music_bot;
import com.example.telegram_bot.repository.entity.TelegramSong;
import com.example.telegram_bot.service.RequestService;
import com.example.telegram_bot.service.SendBotMessageService;
import com.example.telegram_bot.state.State;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GetQuantityCommand implements State {
    private final SendBotMessageService sendBotMessageService;
    private final RequestService requestService;
    private final Music_bot music_bot;
    private final List<TelegramSong> songsList;



    public GetQuantityCommand(SendBotMessageService sendBotMessageService,
                              RequestService requestService,
                              Music_bot music_bot,
                              List<TelegramSong> songsList) {
        this.sendBotMessageService = sendBotMessageService;
        this.requestService = requestService;
        this.music_bot = music_bot;
        this.songsList = songsList;
    }

    @Override
    public void startState(Update update, User user) {
        sendBotMessageService.sendMessage(user.getChatId(), "Сколько песен вы бы хотели, чтобы я вам отправил? (Максимум 10)");
    }

    @Override
    public void execute(Update update, User user) {
        if(update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            int number;

            try {
                number = Integer.parseInt(message);
            }
            catch (NumberFormatException e) {
                sendBotMessageService.sendMessage(user.getChatId(), "Введите количество песен!");
                return;
            }

            if(number < songsList.size() && number > 0 && number <= 10) {
                Random random = new Random();
                for (int i = songsList.size()-1; i > 0; i--) {
                    TelegramSong swap = songsList.get(i);
                    int randomSong = random.nextInt(i+1);

                    songsList.set(i, songsList.get(randomSong));
                    songsList.set(randomSong, swap);
                }

                List<InputMedia> inputMedia = new ArrayList<>();

                for (int i = 0; i < number; i++) {
//                    inputMedia.add(new InputMediaAudio(requestService.getSong(songsList.get(i).getDownloadId())));
                }
                try{
                    SendMediaGroup sendMediaGroup = new SendMediaGroup(user.getChatId().toString(), inputMedia);
                    music_bot.execute(sendMediaGroup);
                }
                catch (IllegalArgumentException e) {
                    System.out.println(e);
                    sendBotMessageService.sendMessage(user.getChatId(), "Песни под такое настроение нет");
                    return;
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                    return;
                }
            }
            else {
                sendBotMessageService.sendMessage(user.getChatId(), "В данный момент такого количества песен нет");
            }

        }
    }

    @Override
    public void undo() {

    }
}
