package ru.alekseenko.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.alekseenko.entity.BinaryContent;

public interface BinaryContentDAO extends JpaRepository<BinaryContent, Long> {
}
