package com.example.telegram_bot.bot;

import com.example.telegram_bot.Entity.User;
import com.example.telegram_bot.command.CommandContainer;
import com.example.telegram_bot.command.NoCommand;
import com.example.telegram_bot.service.*;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.ws.rs.GET;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.telegram_bot.command.CommandName.BUTTONGETSONG;
import static com.example.telegram_bot.command.CommandName.GETSONG;

@Component
public class Music_bot extends TelegramLongPollingBot {

    public static String COMMAND_PREFIX = "/";
    private final CommandContainer commandContainer;
    private final SendBotMessageService sendBotMessageService;
    private ExecutorService executor;
    private List<User> activeUsers;

    @Value("${bot.username}")
    private String username;

    @Value("${bot.token}")
    private String token;

    private Long chatId;

    public Music_bot(TelegramUserService telegramUserService, MoodFolderService moodFolderService, TelegramMusicService telegramMusicService) {
        sendBotMessageService = new SendBotMessageServiceImpl(this);
        this.commandContainer = new CommandContainer(sendBotMessageService,
                telegramUserService,
                this,
                moodFolderService,
                telegramMusicService);
        executor = Executors.newFixedThreadPool(100);
        activeUsers = new ArrayList<>();
        Unirest.config()
                .socketTimeout(5000)
                .connectTimeout(1000)
                .setDefaultHeader("Accept", "application/json");
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Runnable newThread = new Runnable() {
            @Override
            public void run() {
                User user = null;
                if (update.hasMessage()) {
                    chatId = update.getMessage().getChatId();
                }
                if (update.hasCallbackQuery()) {
                    chatId = update.getCallbackQuery().getMessage().getChatId();
                }

                for (User activeUser : activeUsers) {
                    if (activeUser.getChatId().equals(chatId)) {
                        user = activeUser;
                        break;
                    }
                    else {
                        user = null;
                    }
                }

                if (user == null) {
                    user = new User();
                    user.setState(new NoCommand(sendBotMessageService));
                    user.setOldState(user.getState());
                    user.setChatId(chatId);
                    activeUsers.add(user);
                }

                if (update.hasMessage() && update.getMessage().hasText()) {
                    String message = update.getMessage().getText().trim();

                    if (message.startsWith(COMMAND_PREFIX) || message.equals(BUTTONGETSONG.getCommandName())) {
                        String commandIdentifier;

                        if (message.equals(BUTTONGETSONG.getCommandName())) {
                            commandIdentifier = GETSONG.getCommandName();
                        }
                        else {
                            commandIdentifier = message.split(" ")[0].toLowerCase();
                        }

                        user.getState().undo();
                        user.setState(commandContainer.retrieveCommand(commandIdentifier, user));
                        user.getState().startState(update, user);
                        return;
                    }
                }

                user.setOldState(user.getState());
                user.getState().execute(update, user);
                if (user.getState() != user.getOldState()) {
                    user.getState().startState(update, user);
                }
            }
        };

        executor.execute(newThread);
    }
}
