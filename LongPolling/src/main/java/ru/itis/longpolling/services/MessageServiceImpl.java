package ru.itis.longpolling.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.longpolling.dto.MessageDto;
import ru.itis.longpolling.models.Message;
import ru.itis.longpolling.models.Token;
import ru.itis.longpolling.models.User;
import ru.itis.longpolling.repositories.MessageRepository;
import ru.itis.longpolling.repositories.TokensRepository;
import ru.itis.longpolling.repositories.UsersRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private TokensRepository tokensRepository;

    @Override
    public void addMessage(MessageDto message, String token) {
        Optional<Token> tokenCandidate = tokensRepository.findByValue(token);
        User userAuthor = tokenCandidate.orElseThrow(IllegalAccessError::new).getUser();
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
