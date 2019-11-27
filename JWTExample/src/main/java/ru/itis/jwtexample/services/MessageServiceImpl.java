package ru.itis.jwtexample.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.jwtexample.dto.MessageDto;
import ru.itis.jwtexample.models.Message;
import ru.itis.jwtexample.models.User;
import ru.itis.jwtexample.repositories.MessageRepository;
import ru.itis.jwtexample.repositories.UsersRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public void addMessage(MessageDto message) {
        User userAuthor = usersRepository.findByLogin(message.getAuthor()).orElseThrow(IllegalArgumentException::new);
        Message newMessage = Message.builder()
                .text(message.getText())
                .author(userAuthor)
                .build();
        messageRepository.save(newMessage);
    }

    @Override
    public List<MessageDto> getAllMessagesDto() {
        return getAllMessages().stream().map(temp -> MessageDto.builder()
                .text(temp.getText())
                .author(temp.getAuthor().getLogin())
                .build()).collect(Collectors.toList());
    }

    @Override
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }
}
