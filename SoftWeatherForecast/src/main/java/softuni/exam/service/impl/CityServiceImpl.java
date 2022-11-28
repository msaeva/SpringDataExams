package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.CityImportDto;
import softuni.exam.models.entity.City;
import softuni.exam.repository.CityRepository;
import softuni.exam.repository.CountryRepository;
import softuni.exam.service.CityService;
import softuni.exam.util.ValidationUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import static softuni.exam.constants.Messages.INVALID_CITY;
import static softuni.exam.constants.Messages.VALID_CITY_FORMAT;
import static softuni.exam.constants.Paths.CITY_PATH;

@Service
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;

    private final CountryRepository countryRepository;
    private final Gson gson;
    private final ValidationUtils validationUtils;

    private final ModelMapper modelMapper;

    public CityServiceImpl(CityRepository cityRepository, CountryRepository countryRepository, Gson gson, ValidationUtils validationUtils, ModelMapper modelMapper) {
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
        this.gson = gson;
        this.validationUtils = validationUtils;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean areImported() {
        return this.cityRepository.count() > 0;
    }

    @Override
    public String readCitiesFileContent() throws IOException {
        return Files.readString(CITY_PATH);
    }

    @Override
    public String importCities() throws IOException {
        final StringBuilder sb = new StringBuilder();

        List<CityImportDto> cities =
                Arrays.stream(gson.fromJson(readCitiesFileContent(), CityImportDto[].class)).toList();

        for (CityImportDto city : cities) {
            boolean isValid = this.validationUtils.isValid(city);

            if (this.cityRepository.findFirstByCityName(city.getCityName()).isPresent()) {
                continue;
            }

            if (isValid) {
                sb.append(String.format(VALID_CITY_FORMAT, city.getCityName(), city.getPopulation()));

                if (countryRepository.findById(city.getCountry()).isPresent()) {
                    City cityToSave = this.modelMapper.map(city, City.class);

                    cityToSave.setCountry(this.countryRepository.findById(city.getCountry()).get());

                    this.cityRepository.save(cityToSave);
                } else {
                    sb.append("Error").append(System.lineSeparator());
                }
            } else {
                sb.append(INVALID_CITY).append(System.lineSeparator());
            }
        }

        return sb.toString();
    }
}
