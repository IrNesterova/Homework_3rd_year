package ru.itis.jwtexample.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.jwtexample.models.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
