package ru.itis.countries.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.itis.countries.dto.FlagDto;

@Service
public class CountriesServiceImpl implements CountriesService {

    @Autowired
    private RestTemplate template;

    @Value("${restcountries.api.url}")
    private String restCountriesApiUrl;

    @Value("${restcountries.api.fields}")
    private String restCountriesFields;

    @Override
    public FlagDto getFlagOfCountry(String title) {
        String request = restCountriesApiUrl + title + "?fields=" + restCountriesFields;
        System.out.println(request);
        return template.getForEntity(request, FlagDto[].class).getBody()[0];
    }
}