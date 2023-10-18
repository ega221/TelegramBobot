package ru.alekseenko.service;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.alekseenko.entity.AppDocument;
import ru.alekseenko.entity.AppPhoto;

public interface FileService {
    AppDocument processDoc(Message telegramMessage);
    AppPhoto processPhoto(Message telegramMessage);
}
