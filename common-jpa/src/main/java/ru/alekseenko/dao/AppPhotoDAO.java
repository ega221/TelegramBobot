package ru.alekseenko.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.alekseenko.entity.AppPhoto;

public interface AppPhotoDAO extends JpaRepository<AppPhoto, Long> {
}
