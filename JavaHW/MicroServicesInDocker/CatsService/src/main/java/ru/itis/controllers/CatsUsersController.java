package ru.itis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.dto.CatDto;
import ru.itis.services.CatsService;

@RestController
public class CatsUsersController {

    @Autowired
    private CatsService service;

    @GetMapping("cats/search")
    public ResponseEntity<CatDto> getCat() {
        return ResponseEntity.ok(service.getCat());
    }
}