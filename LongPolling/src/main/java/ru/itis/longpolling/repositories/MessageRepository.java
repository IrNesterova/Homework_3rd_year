package ru.itis.longpolling.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.longpolling.models.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
