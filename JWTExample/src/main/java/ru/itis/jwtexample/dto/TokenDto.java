package ru.itis.jwtexample.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto {
    private String value;

    public static TokenDto from(String token) {
        return TokenDto.builder()
                .value(token)
                .build();
    }
}
