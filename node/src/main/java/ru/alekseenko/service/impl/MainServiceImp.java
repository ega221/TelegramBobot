package ru.alekseenko.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.alekseenko.dao.AppUserDAO;
import ru.alekseenko.dao.RawDataDAO;
import ru.alekseenko.entity.AppDocument;
import ru.alekseenko.entity.AppUser;
import ru.alekseenko.entity.RawData;
import ru.alekseenko.entity.enums.UserState;
import ru.alekseenko.exceptions.UploadFileException;
import ru.alekseenko.service.FileService;
import ru.alekseenko.service.MainService;
import ru.alekseenko.service.ProducerService;
import ru.alekseenko.service.enums.ServiceCommand;

import static ru.alekseenko.entity.enums.UserState.BASIC_STATE;
import static ru.alekseenko.entity.enums.UserState.WAIT_FOR_EMAIL_STATE;
import static ru.alekseenko.service.enums.ServiceCommand.*;

@Service
@Log4j
public class MainServiceImp implements MainService {

    private final RawDataDAO rawDataDAO;
    private final ProducerService producerService;
    private final FileService fileService;
    private final AppUserDAO appUserDAO;

    public MainServiceImp(RawDataDAO rawDataDAO, ProducerService producerService, FileService fileService, AppUserDAO appUserDAO) {
        this.rawDataDAO = rawDataDAO;
        this.producerService = producerService;
        this.fileService = fileService;
        this.appUserDAO = appUserDAO;
    }

    @Override
    public void processTextMessage(Update update) {
        saveRawData(update);

        AppUser appUser = findOrSaveAppUser(update);
        UserState userState = appUser.getState();
        String text = update.getMessage().getText();
        String output = "";

        ServiceCommand serviceCommand = ServiceCommand.fromValue(text);

        if (CANCEL.equals(serviceCommand)) {
            output = cancelProcess(appUser);
        } else if (BASIC_STATE.equals(userState)) {
            output = processServiceCommand(appUser, text);
        } else if (WAIT_FOR_EMAIL_STATE.equals(appUser.getState())) {
            // TODO добавить обработку эл адресса
        } else {
            log.error("Unknown user state: " + userState);
            output = "Неизвестная ошибка! Введите /cancel и попробуйте снова";
        }

        String chatId = update.getMessage().getChatId().toString();

        sendAnswer(output, chatId);
    }

    @Override
    public void processPhotoMessage(Update update) {
        saveRawData(update);
        AppUser appUser = findOrSaveAppUser(update);
        String chatId = update.getMessage().getChatId().toString();
        if (isNotAllowedToSendContent(chatId, appUser)) {
            return;
        }
        //ToDo добавить сохранение документов.
        String answer = "Фото успешно загружено!";
        sendAnswer(answer, chatId);
    }

    private boolean isNotAllowedToSendContent(String chatId, AppUser appUser) {
        UserState userState = appUser.getState();
        if (!appUser.getIsActive()) {
            String error = "Зарегистрируйтесь или активируйте свою учетную запись для загрузки контента.";
            sendAnswer(error, chatId);
            return true;
        } else if (!BASIC_STATE.equals(userState)) {
            String error = "Отмените текущую команду с помощью /cancel для отправки файлов";
            sendAnswer(error, chatId);
            return true;
        }
        return false;
    }

    @Override
    public void processDocMessage(Update update) {
        saveRawData(update);
        AppUser appUser = findOrSaveAppUser(update);
        String chatId = update.getMessage().getChatId().toString();
        if (isNotAllowedToSendContent(chatId, appUser)) {
            return;
        }

        try {
            AppDocument document = fileService.processDoc(update.getMessage());
            //ToDo добавить генерацию ссылок.
            String answer = "Документ успешно загружен! Ваша ссылка...";
            sendAnswer(answer, chatId);
        } catch (UploadFileException ex) {
            log.error(ex);
            String error = "Загрузка файла не удалась, пожалуйста, попробуйте позже.";
            sendAnswer(error, chatId);
        }

    }

    private void sendAnswer(String output, String chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(output);

        producerService.produceAnswer(sendMessage);
    }

    private String processServiceCommand(AppUser appUser, String cmd) {
        if (REGISTRATION.equals(cmd)) {
            //ToDo добавить регистрацию
            return "Временно не доступно";
        } else if(HELP.equals(cmd)) {
            return help();
        } else if (START.equals(cmd)) {
            return "Привет! Чтобы посмотреть список доступных команд введи /help";
        } else {
            return "Неизвестная команда! Чтобы посмотреть список доступных команд введи /help";
        }

    }

    private String help() {
        return "Список доступных команд: \n"
                + "/cancel - отмена выполнения текущей команды. \n"
                + "/registration - регистрация пользователя.";
    }

    private String cancelProcess(AppUser appUser) {
        appUser.setState(BASIC_STATE);
        appUserDAO.save(appUser);
        return "Команда отменена!";

    }

    private AppUser findOrSaveAppUser(Update update) {
        User telegramUser = update.getMessage().getFrom();
        AppUser persistentAppUser = appUserDAO.findAppUserByTelegramUserId(telegramUser.getId());
        if (persistentAppUser == null) {
            AppUser transientAppUser = AppUser.builder()
                    .telegramUserId(telegramUser.getId())
                    .firstName(telegramUser.getFirstName())
                    .lastName(telegramUser.getLastName())
                    //TODO изменить значение по умолчанию после добавления регистрации
                    .isActive(true)
                    .state(BASIC_STATE)
                    .build();
            return appUserDAO.save(transientAppUser);
        }
        return persistentAppUser;
    }

    private void saveRawData(Update update) {
        RawData rawData = RawData.builder()
                .event(update)
                .build();

        rawDataDAO.save(rawData);
    }
}
