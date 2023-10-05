package ru.alekseenko.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.alekseenko.entity.AppUser;

public interface AppUserDAO extends JpaRepository<AppUser, Long> {
    AppUser findAppUserByTelegramUserId(Long id);

}
