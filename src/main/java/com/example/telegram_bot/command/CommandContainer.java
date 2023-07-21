package com.example.telegram_bot.command;

import com.example.telegram_bot.Entity.User;
import com.example.telegram_bot.bot.Music_bot;
import com.example.telegram_bot.command.admin.addfolder.AddFolderNameCommand;
import com.example.telegram_bot.command.admin.addsong.AddSongCommand;
import com.example.telegram_bot.command.admin.deletefolder.DeleteFolderCommand;
import com.example.telegram_bot.command.admin.deletesong.DeleteSongCommand;
import com.example.telegram_bot.command.admin.help.AdminHelpCommand;
import com.example.telegram_bot.command.admin.updatefolder.ChooseFolderCommand;
import com.example.telegram_bot.command.admin.updatefolder.UpdateFolderNameCommand;
import com.example.telegram_bot.command.getsong.GetMoodCommand;
import com.example.telegram_bot.service.*;
import com.example.telegram_bot.state.State;
import com.google.common.collect.ImmutableMap;

import static com.example.telegram_bot.command.CommandName.*;

public class CommandContainer {

    private final ImmutableMap<String, State> commandMap;
    private final ImmutableMap<String, State> commandAdminMap;
    private final State unknownCommand;
    private final Long adminID = Long.valueOf(1395425257);

    public CommandContainer(SendBotMessageService sendBotMessageService,
                            TelegramUserService telegramUserService,
                            Music_bot music_bot,
                            MoodFolderService moodFolderService,
                            TelegramMusicService telegramMusicService) {
        commandMap = ImmutableMap.<String, State>builder()
                .put(START.getCommandName(), new StartCommand(sendBotMessageService, telegramUserService))
                .put(GETSONG.getCommandName(), new GetMoodCommand(sendBotMessageService, moodFolderService, telegramMusicService, music_bot))
                .put(HELP.getCommandName(), new HelpCommand(sendBotMessageService, music_bot))
                .put(NO.getCommandName(), new NoCommand(sendBotMessageService))
                .put(STAT.getCommandName(), new StatCommand(sendBotMessageService, telegramUserService, music_bot))
                .build();

        unknownCommand = new UnknownCommand(sendBotMessageService, music_bot);

        commandAdminMap = ImmutableMap.<String, State>builder()
                .put(ADMINHELP.getCommandName(), new AdminHelpCommand(sendBotMessageService))
                .put(ADDSONG.getCommandName(), new AddSongCommand(sendBotMessageService, moodFolderService, telegramMusicService, music_bot))
                .put(DELETESONG.getCommandName(), new DeleteSongCommand(sendBotMessageService, telegramMusicService, music_bot))
                .put(ADDFOLDER.getCommandName(), new AddFolderNameCommand(sendBotMessageService, moodFolderService, music_bot))
                .put(DELETEFOLDER.getCommandName(), new DeleteFolderCommand(sendBotMessageService, moodFolderService, telegramMusicService))
                .put(UPDATEFOLDER.getCommandName(), new ChooseFolderCommand(sendBotMessageService, moodFolderService))
                .build();
    }

    public State retrieveCommand(String commandIdentifier, User user) {
        if (commandAdminMap.containsKey(commandIdentifier) && (user.getChatId().equals(adminID))) return commandAdminMap.get(commandIdentifier);
        else return commandMap.getOrDefault(commandIdentifier, unknownCommand);
    }
}
