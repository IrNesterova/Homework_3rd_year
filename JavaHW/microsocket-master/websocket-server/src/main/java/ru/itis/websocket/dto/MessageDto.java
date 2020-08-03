package ru.itis.websocket.dto;

import lombok.*;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageDto {
    private String author;
    private String message;
    private String timestamp;
    private Timestamp createdAt;
    private String room;
}
