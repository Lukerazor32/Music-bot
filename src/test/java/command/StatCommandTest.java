package command;

import com.example.telegram_bot.command.StatCommand;
import com.example.telegram_bot.state.State;

import static com.example.telegram_bot.command.CommandName.STAT;
import static com.example.telegram_bot.command.StatCommand.STAT_MESSAGE;

public class StatCommandTest extends AbstractCommandTest {
    @Override
    String getCommandName() {
        return STAT.getCommandName();
    }

    @Override
    String getCommandMessage() {
        return String.format(STAT_MESSAGE, 0);
    }

    @Override
    State getCommand() {
        return new StatCommand(sendBotMessageService, telegramUserService, music_bot);
    }
}
