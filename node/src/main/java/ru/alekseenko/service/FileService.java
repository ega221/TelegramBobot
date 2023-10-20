package ru.alekseenko.service;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.alekseenko.entity.AppDocument;
import ru.alekseenko.entity.AppPhoto;
import ru.alekseenko.service.enums.LinkType;

public interface FileService {
    AppDocument processDoc(Message telegramMessage);
    AppPhoto processPhoto(Message telegramMessage);

    String generateLink(Long docId, LinkType linkType);
}
