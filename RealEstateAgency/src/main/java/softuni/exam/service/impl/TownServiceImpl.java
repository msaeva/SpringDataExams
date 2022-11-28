package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.TownImportDto;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static softuni.exam.constants.Messages.INVALID_TOWN;
import static softuni.exam.constants.Messages.VALID_TOWN_FORMAT;

@Service
public class TownServiceImpl implements TownService {
    private final TownRepository townRepository;
    private final Gson gson;

    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    public TownServiceImpl(TownRepository townRepository, Gson gson, ValidationUtil validationUtil, ModelMapper modelMapper) {
        this.townRepository = townRepository;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
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

        StringBuilder sb = new StringBuilder();

        List<TownImportDto> towns = Arrays.stream(gson.fromJson(readTownsFileContent(), TownImportDto[].class))
                .toList();

        for (TownImportDto town : towns) {
            boolean isValid = validationUtil.isValid(town);

            if (!isValid) {
                sb.append(INVALID_TOWN).append(System.lineSeparator());
                continue;
            }

            sb.append(String.format(VALID_TOWN_FORMAT, town.getTownName(), town.getPopulation()))
                    .append(System.lineSeparator());

            Town townToSave = modelMapper.map(town, Town.class);
            townRepository.saveAndFlush(townToSave);
        }

        return sb.toString();
    }

    @Override
    public Town findTownByName(String townName) {
        return townRepository.findByTownName(townName);
    }
}
