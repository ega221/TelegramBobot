package ru.alekseenko.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.alekseenko.entity.AppDocument;

public interface AppDocumentDAO extends JpaRepository<AppDocument, Long> {
}
