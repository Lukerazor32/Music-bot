package com.example.telegram_bot.command.admin.addfolder;

import com.example.telegram_bot.Entity.User;
import com.example.telegram_bot.bot.Music_bot;
import com.example.telegram_bot.command.NoCommand;
import com.example.telegram_bot.repository.entity.MoodFolder;
import com.example.telegram_bot.service.SendBotMessageService;
import com.example.telegram_bot.service.MoodFolderService;
import com.example.telegram_bot.state.State;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ConfirmCommand implements State {

    private final SendBotMessageService sendBotMessageService;
    private final MoodFolderService moodFolderService;
    private final Music_bot music_bot;

    private final String folderName;

    public ConfirmCommand(
            SendBotMessageService sendBotMessageService,
            MoodFolderService moodFolderService,
            String folderName,
            Music_bot music_bot) {
        this.sendBotMessageService = sendBotMessageService;
        this.moodFolderService = moodFolderService;
        this.folderName = folderName;
        this.music_bot = music_bot;
    }

    @Override
    public void startState(Update update, User user) {
        execute(update, user);
    }

    @Override
    public void execute(Update update, User user) {
            MoodFolder MoodFolder = new MoodFolder();
            MoodFolder.setFolderName(folderName);

            moodFolderService.save(MoodFolder);

            sendBotMessageService.sendMessage(update.getMessage().getChatId(), "Папка успешно добавлена!");
            user.setState(new NoCommand(sendBotMessageService));
    }

    @Override
    public void undo() {

    }

}
