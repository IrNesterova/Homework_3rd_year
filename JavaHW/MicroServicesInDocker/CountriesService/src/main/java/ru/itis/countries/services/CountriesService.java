package ru.itis.countries.services;

import ru.itis.countries.dto.FlagDto;

public interface CountriesService {
    FlagDto getFlagOfCountry(String title);
}