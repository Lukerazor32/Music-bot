package com.example.telegram_bot.command.admin.deletesong;

import com.example.telegram_bot.Entity.User;
import com.example.telegram_bot.bot.Music_bot;
import com.example.telegram_bot.command.NoCommand;
import com.example.telegram_bot.repository.entity.TelegramSong;
import com.example.telegram_bot.service.SendBotMessageService;
import com.example.telegram_bot.service.TelegramMusicService;
import com.example.telegram_bot.state.State;
import org.telegram.telegrambots.meta.api.objects.Update;

public class DeleteSongCommand implements State {
    private final SendBotMessageService sendBotMessageService;
    private final TelegramMusicService telegramMusicService;
    private final Music_bot music_bot;



    public DeleteSongCommand(SendBotMessageService sendBotMessageService, TelegramMusicService telegramMusicService, Music_bot music_bot) {
        this.sendBotMessageService = sendBotMessageService;
        this.telegramMusicService = telegramMusicService;
        this.music_bot = music_bot;
    }

    @Override
    public void startState(Update update, User user) {
          sendBotMessageService.sendMessage(user.getChatId(), "Введите название песни");
    }

    @Override
    public void execute(Update update, User user) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            final TelegramSong song = telegramMusicService.findByTitle(update.getMessage().getText());
            if (song != null) {
                telegramMusicService.delete(song);
                sendBotMessageService.sendMessage(user.getChatId(), "Песня удалена");
                user.setState(new NoCommand(sendBotMessageService));
                return;
            }
            sendBotMessageService.sendMessage(user.getChatId(), "Такой песни нет");
            user.setState(new NoCommand(sendBotMessageService));
        }
    }

    @Override
    public void undo() {

    }
}
