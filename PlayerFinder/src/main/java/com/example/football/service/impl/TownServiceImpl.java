package com.example.football.service.impl;

import com.example.football.models.dto.TownsImportDto;
import com.example.football.models.entity.Town;
import com.example.football.repository.TownRepository;
import com.example.football.service.TownService;
import com.example.football.util.ValidationUtils;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.example.football.constants.Messages.INVALID_TOWN;
import static com.example.football.constants.Messages.VALID_TOWN_FORMAT;


//ToDo - Implement all methods
@Service
public class TownServiceImpl implements TownService {
    private final TownRepository townRepository;
    private final Gson gson;
    private final ModelMapper modelMapper;

    private final ValidationUtils validationUtils;

    public TownServiceImpl(TownRepository townRepository, Gson gson, ModelMapper modelMapper, ValidationUtils validationUtils) {
        this.townRepository = townRepository;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validationUtils = validationUtils;
    }


    @Override
    public boolean areImported() {
        return townRepository.count() > 0;
    }

    @Override
    public String readTownsFileContent() throws IOException {
        return Files.readString(Path.of("src/main/resources/files/json/towns.json"));
    }

    @Override
    public String importTowns() throws IOException {
        List<TownsImportDto> townsImportDtos =
                Arrays.stream(gson.fromJson(readTownsFileContent(), TownsImportDto[].class)).toList();


        StringBuilder sb = new StringBuilder();

        for (TownsImportDto town : townsImportDtos) {
            boolean isValid = validationUtils.isValid(town);

            if (!isValid) {
                sb.append(INVALID_TOWN).append(System.lineSeparator());
                continue;
            }
            sb.append(String.format(VALID_TOWN_FORMAT, town.getName(), town.getPopulation()))
                    .append(System.lineSeparator());

            Town townToSave = modelMapper.map(town, Town.class);
            townRepository.saveAndFlush(townToSave);
        }
        return sb.toString();
    }

    @Override
    public Town findByName(String townName) {
        return townRepository.findByName(townName).get();
    }
}
