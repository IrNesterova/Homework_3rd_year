package ru.itis.jwtexample.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.jwtexample.models.User;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<User,Long> {
    Optional<User> findByLogin(String login);
}
