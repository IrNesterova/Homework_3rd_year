package ru.itis.longpolling.controllers;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.itis.longpolling.dto.MessageDto;
import ru.itis.longpolling.models.Token;
import ru.itis.longpolling.repositories.TokensRepository;
import ru.itis.longpolling.services.MessageService;

import java.util.*;

@RestController
public class MessagesController {
    private final Map<String, List<MessageDto>> messages = new HashMap<>();

    @Autowired
    private MessageService messageService;

    @Autowired
    private TokensRepository tokensRepository;

    @CrossOrigin
    @ApiOperation("Get message")
    @GetMapping("/messages")
    @PreAuthorize("isAuthenticated()")
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
                Token tokenCandidate = tokensRepository.findByValue(token).orElseThrow(IllegalArgumentException::new);
                messageDto = MessageDto.builder()
                        .text(messageForm.getText())
                        .author(tokenCandidate.getUser().getLogin())
                        .token(token)
                        .build();
                if (messageForm.getText() != null) {
                    pageMessages.add(messageDto);
                    pageMessages.notifyAll();
                }
            }
        }
        messageService.addMessage(messageDto, token);
        return ResponseEntity.ok().build();
    }

    @CrossOrigin
    @PreAuthorize("permitAll()")
    @GetMapping("/getAll")
    public ResponseEntity<List<MessageDto>> getAllMessages() {
        System.out.println(messageService.getAllMessagesDto());
        return ResponseEntity.ok(messageService.getAllMessagesDto());
    }

}