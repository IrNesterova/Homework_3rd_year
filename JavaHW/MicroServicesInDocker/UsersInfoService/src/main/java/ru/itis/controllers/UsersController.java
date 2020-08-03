package ru.itis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import ru.itis.dto.CatDto;
import ru.itis.dto.FlagDto;
import ru.itis.dto.InfoUserDto;

@Controller
public class UsersController {

    @Autowired
    private RestTemplate template;

    @GetMapping("/users/info")
    public ResponseEntity<InfoUserDto> getInfoAbout(@RequestParam("name") String name, @RequestParam("country") String country) {
        CatDto catDto = template.getForEntity("http://CAT/cats/search", CatDto.class).getBody();
        FlagDto flagDto = template.getForEntity("http://COUNTRY/countries/" + country, FlagDto.class).getBody();
        return ResponseEntity.ok(InfoUserDto.builder()
                .flagUrl(flagDto.getFlag())
                .catUrl(catDto.getUrl())
                .build());
    }
}
