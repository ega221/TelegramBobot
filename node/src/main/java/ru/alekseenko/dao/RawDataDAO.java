package ru.alekseenko.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.alekseenko.entity.RawData;

public interface RawDataDAO extends JpaRepository<RawData, Long> {

}
