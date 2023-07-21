package service;

import com.example.telegram_bot.bot.Music_bot;
import com.example.telegram_bot.service.SendBotMessageService;
import com.example.telegram_bot.service.SendBotMessageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class SendBotMessageServiceTest {
//    private SendBotMessageService sendBotMessageService;
//    private Music_bot music_bot;
//
//    @BeforeEach
//    public void init() {
//        music_bot = Mockito.mock(Music_bot.class);
//        sendBotMessageService = new SendBotMessageServiceImpl(music_bot);
//    }
//
//    @Test
//    public void shouldProperlySendMessage() throws TelegramApiException {
//        //given
//        Long chatId = "test_chat_id";
//        String message = "test_message";
//
//        SendMessage sendMessage = new SendMessage();
//        sendMessage.setText(message);
//        sendMessage.setChatId(chatId);
//        sendMessage.enableHtml(true);
//
//        //when
//        sendBotMessageService.sendMessage(user.getChatId(), message);
//
//        //then
//        Mockito.verify(music_bot).execute(sendMessage);
//    }
}
