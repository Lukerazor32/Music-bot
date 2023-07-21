package com.example.telegram_bot.command.admin.updatefolder;

import com.example.telegram_bot.Entity.User;
import com.example.telegram_bot.repository.entity.MoodFolder;
import com.example.telegram_bot.service.MoodFolderService;
import com.example.telegram_bot.service.SendBotMessageService;
import com.example.telegram_bot.state.State;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class ChooseFolderCommand implements State {
    private final SendBotMessageService sendBotMessageService;
    private final MoodFolderService moodFolderService;

    private List<MoodFolder> folders;

    public ChooseFolderCommand(SendBotMessageService sendBotMessageService, MoodFolderService moodFolderService) {
        this.sendBotMessageService = sendBotMessageService;
        this.moodFolderService = moodFolderService;
    }

    @Override
    public void startState(Update update, User user) {
        folders = moodFolderService.findAll();

        StringBuilder result = new StringBuilder("Список существующих папок: \n\n");
        for (int i = 0; i < folders.size(); i++) {
            result.append(
                    i+1 + ". "
                    + folders.get(i).getFolderName()
                    + "\n"
                    + "Количество песен - "
                    + folders.get(i).getSongs().size()
                    + "\n\n");
        }

        result.append("Отправьте номер папки, которую вы хотите изменить");

        sendBotMessageService.sendMessage(user.getChatId(), result.toString());
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

                user.setState(new UpdateFolderNameCommand(
                        sendBotMessageService,
                        moodFolderService,
                        folder));
            }
            else sendBotMessageService.sendMessage(user.getChatId(), "Такой папки не существует"); return;
        }
    }

    @Override
    public void undo() {

    }
}
