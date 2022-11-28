package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.CountryImportDto;
import softuni.exam.models.entity.Country;
import softuni.exam.repository.CountryRepository;
import softuni.exam.service.CountryService;
import softuni.exam.util.ValidationUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import static softuni.exam.constants.Messages.INVALID_COUNTRY;
import static softuni.exam.constants.Messages.VALID_COUNTRY_FORMAT;
import static softuni.exam.constants.Paths.COUNTRIES_PATH;

@Service
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;
    private final Gson gson;
    private final ModelMapper mapper;

    private final ValidationUtils validationUtils;

    @Autowired
    public CountryServiceImpl(CountryRepository countryRepository, Gson gson, ModelMapper mapper, ValidationUtils validationUtils) {
        this.countryRepository = countryRepository;
        this.gson = gson;
        this.mapper = mapper;
        this.validationUtils = validationUtils;
    }

    @Override
    public boolean areImported() {
        return this.countryRepository.count() > 0;
    }

    @Override
    public String readCountriesFromFile() throws IOException {
        return Files.readString(COUNTRIES_PATH);
    }

    @Override
    public String importCountries() throws IOException {

        final StringBuilder sb = new StringBuilder();

        List<CountryImportDto> countryDtos = Arrays.stream(gson.fromJson(readCountriesFromFile(), CountryImportDto[].class)).toList();

        for (CountryImportDto countryDto : countryDtos) {
            boolean isValid = this.validationUtils.isValid(countryDto);

            if (this.countryRepository.findByCountryName(countryDto.getCountryName()).isPresent()) {
                isValid = false;
            }

            if (isValid) {
                sb.append(String.format(VALID_COUNTRY_FORMAT,
                        countryDto.getCountryName(),
                        countryDto.getCurrency()));

                Country countryToSave = this.mapper.map(countryDto, Country.class);

                this.countryRepository.saveAndFlush(countryToSave);

            } else {
                sb.append(INVALID_COUNTRY).append(System.lineSeparator());
            }
        }

        return sb.toString();
    }
}
