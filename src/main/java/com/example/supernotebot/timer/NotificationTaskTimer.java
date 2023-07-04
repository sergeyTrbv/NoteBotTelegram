package com.example.supernotebot.timer;

import com.example.supernotebot.repository.NotificationTaskRepository;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

//Аннотация указывает Спрингу, что этот класс является Бином
@Component
public class NotificationTaskTimer {

    private final NotificationTaskRepository notificationTaskRepository;
    private final TelegramBot telegramBot;

    public NotificationTaskTimer(NotificationTaskRepository notificationTaskRepository, TelegramBot telegramBot) {
        this.notificationTaskRepository = notificationTaskRepository;
        this.telegramBot = telegramBot;
    }


    //Аннотация, чтобы использовать функционал по таймеру
    //fixedDelay - частота запроса(задержка между концом последнего и начала следующего), timeUnit - единица измерения
    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void task() {
        //Получаем список задач, где время "сейчас" обрезаем секунды до минут
        notificationTaskRepository.findAllByNotificationDateTime(
                        LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
                //Получаем задачи и через цикл обрабатываем
                .forEach(notificationTask -> {
                    //создаём "отправить сообщение" и отправляем
                    telegramBot.execute(new SendMessage(notificationTask.getChatId(),
                            "Вы просили напомнить: " + notificationTask.getMessage()));
                    //удаляем из кэша задачу
                    notificationTaskRepository.delete(notificationTask);
                });


    }


}
