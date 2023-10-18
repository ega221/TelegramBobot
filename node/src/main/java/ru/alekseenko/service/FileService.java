package ru.alekseenko.service;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.alekseenko.entity.AppDocument;

public interface FileService {
    AppDocument processDoc(Message externalMessage);
}
