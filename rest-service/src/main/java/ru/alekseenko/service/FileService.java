package ru.alekseenko.service;

import org.springframework.core.io.FileSystemResource;
import ru.alekseenko.entity.AppDocument;
import ru.alekseenko.entity.AppPhoto;
import ru.alekseenko.entity.BinaryContent;

public interface FileService {
    AppDocument getDocument(String id);
    AppPhoto getPhoto(String id);
    FileSystemResource getFileSystemResource(BinaryContent binaryContent);
}
