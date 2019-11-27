package ru.itis.dto;

import lombok.Builder;
import lombok.Data;
import ru.itis.models.Message;
import ru.itis.models.MessageType;

@Builder
@Data
public class MessageDto {
    private String sender;
    private String content;
    private String type;
    private String password;

    public static MessageDto from(Message message) {
        return MessageDto.builder()
                .content(message.getContent())
                .sender(message.getSender().getLogin())
                .build();
    }
}