package ru.alekseenko.service.impl;

import lombok.extern.log4j.Log4j;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import ru.alekseenko.dao.AppDocumentDAO;
import ru.alekseenko.dao.AppPhotoDAO;
import ru.alekseenko.entity.AppDocument;
import ru.alekseenko.entity.AppPhoto;
import ru.alekseenko.entity.BinaryContent;
import ru.alekseenko.service.FileService;

import java.io.File;
import java.io.IOException;

@Service
@Log4j
public class FileServiceImpl implements FileService {
    private final AppDocumentDAO appDocumentDAO;
    private final AppPhotoDAO appPhotoDAO;

    public FileServiceImpl(AppDocumentDAO appDocumentDAO, AppPhotoDAO appPhotoDAO) {
        this.appDocumentDAO = appDocumentDAO;
        this.appPhotoDAO = appPhotoDAO;
    }


    @Override
    public AppDocument getDocument(String docId) {
        // В кач-ве id должна приходить зашифрованная строка, чтобы пользователь не мог
        // просто написать какой ему захочется id и скачать любой файл.
        //ToDo нужно сделать передачу id в закрытом виде, с его последующей расшифровкой.
        Long id = Long.parseLong(docId);
        return appDocumentDAO.findById(id).orElse(null);
    }

    @Override
    public AppPhoto getPhoto(String photoId) {
        // В кач-ве id должна приходить зашифрованная строка, чтобы пользователь не мог
        // просто написать какой ему захочется id и скачать любой файл.
        //ToDo нужно сделать передачу id в закрытом виде, с его последующей расшифровкой.
        Long id = Long.parseLong(photoId);
        return appPhotoDAO.findById(id).orElse(null);
    }

    @Override
    public FileSystemResource getFileSystemResource(BinaryContent binaryContent) {
        try {
            //Возможно, нужна генерация имён временных файлов, ибо может произойти коллизия.
            //ToDo добавить генерацию имён
            File temp = File.createTempFile("tempFile", ".bin");
            temp.deleteOnExit();
            FileUtils.writeByteArrayToFile(temp, binaryContent.getFileAsArrayOfBytes());
            return new FileSystemResource(temp);
        } catch (IOException e) {
            log.error(e);
            return null;
        }
    }
}
