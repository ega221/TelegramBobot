package ru.alekseenko.service.impl;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.alekseenko.dao.RawDataDAO;
import ru.alekseenko.service.MainService;
import ru.alekseenko.service.ProducerService;

public class MainServiceImp implements MainService {

    private final RawDataDAO rawDataDAO;
    private final ProducerService producerService;

    public MainServiceImp(RawDataDAO rawDataDAO, ProducerService producerService) {
        this.rawDataDAO = rawDataDAO;
        this.producerService = producerService;
    }

    @Override
    public void processTextMessage(Update update) {

    }
}
