package ru.itis.longpolling.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.longpolling.models.Token;

import java.util.Optional;

public interface TokensRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByValue(String value);
}
