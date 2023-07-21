package com.example.telegram_bot.command.admin.help;

import com.example.telegram_bot.Entity.User;
import com.example.telegram_bot.bot.Music_bot;
import com.example.telegram_bot.command.NoCommand;
import com.example.telegram_bot.service.SendBotMessageService;
import com.example.telegram_bot.state.State;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.example.telegram_bot.command.CommandName.*;

public class AdminHelpCommand implements State {
    private final SendBotMessageService sendBotMessageService;

    public final static String HELP_MESSAGE = String.format("Доступные админ-команды:" +
                    "\n\n" +
                    "%s - статистика бота" +
                    "\n" +
                    "%s - добавить папку" +
                    "\n" +
                    "%s - изменить папку" +
                    "\n" +
                    "%s - удалить папку" +
                    "\n" +
                    "%s - добавить песню" +
                    "\n" +
                    "%s - удалить песню",
            STAT.getCommandName(), ADDFOLDER.getCommandName(), UPDATEFOLDER.getCommandName(),
            DELETEFOLDER.getCommandName(), ADDSONG.getCommandName(), DELETESONG.getCommandName());

    public AdminHelpCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void startState(Update update, User user) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId(), HELP_MESSAGE);
        user.setState(new NoCommand(sendBotMessageService));
    }

    @Override
    public void execute(Update update, User user) {

    }

    @Override
    public void undo() {

    }
}
