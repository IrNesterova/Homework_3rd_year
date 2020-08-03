package ru.itis.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.itis.dto.CatDto;

@Service
public class CatsServiceImpl implements CatsService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${cats.get.request.url}")
    private String catsGetRequestUrl;

    @Override
    public CatDto getCat() {
        return restTemplate.getForEntity(catsGetRequestUrl, CatDto[].class).getBody()[0];
    }
}