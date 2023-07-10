package com.example.supernotebot;

import com.example.supernotebot.listener.TelegramBotUpdatesListener;
import com.example.supernotebot.service.NotificationTaskService;
import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.apache.naming.factory.SendMailFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TelegramBotUpdateListenerTest {

    @Mock
    private TelegramBot telegramBot;

    @Mock
    private NotificationTaskService notificationTaskService;

    @InjectMocks
    private TelegramBotUpdatesListener telegramBotUpdatesListener;


    //Проверяем работу бота
    @Test
    public void handleStartTest() throws URISyntaxException, IOException {

        String json = Files.readString(
                Path.of(TelegramBotUpdateListenerTest.class.getResource("update.json").toURI()));
        //Получем апдейт из json благодаря классу BotUtils; Заменяем значение "текст" на "старт" и говорим что нужно реализовать объект Update;
        Update update = BotUtils.fromJson(json.replace("%text%", "/start"), Update.class);

        SendResponse sendResponse = BotUtils.fromJson("""
                {
                "ok": true
                }
                """,SendResponse.class);
        when(telegramBot.execute(any())).thenReturn(sendResponse);

        //Подставляем update в метод process
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        //Проверяю что вызовется метод execute(куда передается (в ответ) sendMassage с определёнными параметрами)
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(update.message().chat().id());
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("""
                            Привет
                            Сделай запись в формате: 01.01.2020 09:00 Выпить кофе
                            И я тебе напомню о твоих планах.
                            """);


    }


}
