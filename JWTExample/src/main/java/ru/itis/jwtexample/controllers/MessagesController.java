package ru.itis.jwtexample.controllers;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import ru.itis.jwtexample.dto.MessageDto;
import ru.itis.jwtexample.models.User;
import ru.itis.jwtexample.repositories.UsersRepository;
import ru.itis.jwtexample.services.MessageService;

import java.util.*;

@RestController
public class MessagesController {
    private final Map<String, List<MessageDto>> messages = new HashMap<>();

    @Autowired
    private MessageService messageService;

    @Autowired
    private UsersRepository usersRepository;

    @CrossOrigin
    @ApiOperation("Get message")
    @GetMapping("/messages")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<MessageDto>> getMessagesForPage(@RequestHeader("AUTH") String token) throws InterruptedException {
        synchronized (messages.get(token)) {
            if (messages.get(token).isEmpty()) {
                messages.get(token).wait();
            }
            List<MessageDto> response = new ArrayList<>(messages.get(token));
            messages.get(token).clear();
            return ResponseEntity.ok(response);
        }
    }

    @CrossOrigin
    @ApiOperation("Add new message")
    @PostMapping("/messages")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Object> receiveMessage(@RequestBody MessageDto messageForm, @RequestHeader("AUTH") String token) {
        MessageDto messageDto = null;
        if (!messages.containsKey(token)) {
            messages.put(token, new ArrayList<>());
        }
        for (List<MessageDto> pageMessages : messages.values()) {
            synchronized (pageMessages) {
                if (usersRepository.findByLogin(messageForm.getAuthor()).isPresent()) {
                    User userAuthor = usersRepository.findByLogin(messageForm.getAuthor()).get();
                    messageDto = MessageDto.builder()
                            .text(messageForm.getText())
                            .author(userAuthor.getLogin())
                            .token(token)
                            .build();
                    if (messageForm.getText() != null) {
                        pageMessages.add(messageDto);
                        pageMessages.notifyAll();
                    }
                } else throw new UsernameNotFoundException("no user with such login");
            }
        }
        messageService.addMessage(messageDto);
        return ResponseEntity.ok().build();
    }

    @CrossOrigin
    @PreAuthorize("permitAll()")
    @GetMapping("/getAll")
    public ResponseEntity<List<MessageDto>> getAllMessages() {
        return ResponseEntity.ok(messageService.getAllMessagesDto());
    }

}