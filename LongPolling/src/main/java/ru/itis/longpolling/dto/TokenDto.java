package ru.itis.longpolling.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.longpolling.models.Token;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto {
    private String value;

    public static TokenDto from(Token token) {
        return TokenDto.builder()
                .value(token.getValue())
                .build();
    }
}
