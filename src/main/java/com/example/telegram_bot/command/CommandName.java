package com.example.telegram_bot.command;

public enum CommandName {

    START("/start"),
    BUTTONGETSONG("Песни под настроение"),
    GETSONG("/getsong"),
    HELP("/help"),
    NO(""),

    ADMINHELP("/adminhelp"),
    STAT("/stat"),
    ADDSONG("/addsong"),
    DELETESONG("/dltsong"),
    ADDFOLDER("/addfolder"),
    DELETEFOLDER("/dltfolder"),
    UPDATEFOLDER("/updfolder"),
    EXIT("/exit");

    private final String commandName;

    CommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }

}
