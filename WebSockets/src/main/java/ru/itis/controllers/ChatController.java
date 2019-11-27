package ru.itis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import ru.itis.dto.MessageDto;
import ru.itis.forms.UserForm;
import ru.itis.models.User;
import ru.itis.services.MessageService;
import ru.itis.services.UserService;

@Controller
public class ChatController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public MessageDto sendMessage(@Payload MessageDto messageDto) {
        return messageService.addMessage(messageDto);
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public MessageDto addUser(@Payload MessageDto messageDto,
                              SimpMessageHeaderAccessor headerAccessor) {
//        add login validation
        if (userService.login(messageDto)) {
            headerAccessor.getSessionAttributes().put("username", messageDto.getSender());

        } else {
            User newUser = userService.signUp(messageDto);
            headerAccessor.getSessionAttributes().put("username", newUser.getLogin());
        }
        return messageDto;
    }

    @GetMapping("/chat")
    public String getChatPage() {
        return "chat";
    }

}

