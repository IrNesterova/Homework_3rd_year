package ru.itis.jwtexample.services;

import ru.itis.jwtexample.dto.MessageDto;
import ru.itis.jwtexample.models.Message;

import java.util.List;

public interface MessageService {
    void addMessage(MessageDto message);
    List<MessageDto> getAllMessagesDto();
    List<Message> getAllMessages();
}
