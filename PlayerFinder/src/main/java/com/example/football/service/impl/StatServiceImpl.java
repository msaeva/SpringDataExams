package com.example.football.service.impl;

import com.example.football.models.dto.StatsImportDto;
import com.example.football.models.dto.StatsImportWrapperDto;
import com.example.football.models.entity.Stat;
import com.example.football.repository.StatRepository;
import com.example.football.service.StatService;
import com.example.football.util.ValidationUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static com.example.football.constants.Messages.INVALID_STAT;
import static com.example.football.constants.Messages.VALID_STAT_FORMAT;

//ToDo - Implement all methods
@Service
public class StatServiceImpl implements StatService {
    private final StatRepository statRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtils validationUtils;

    public StatServiceImpl(StatRepository statRepository, ModelMapper modelMapper, ValidationUtils validationUtils) {
        this.statRepository = statRepository;
        this.modelMapper = modelMapper;
        this.validationUtils = validationUtils;
    }


    @Override
    public boolean areImported() {
        return statRepository.count() > 0;
    }

    @Override
    public String readStatsFileContent() throws IOException {
        return Files.readString(Path.of("src/main/resources/files/xml/stats.xml"));
    }

    @Override
    public String importStats() throws JAXBException, IOException {

        JAXBContext context = JAXBContext.newInstance(StatsImportWrapperDto.class);

        Unmarshaller unmarshaller = context.createUnmarshaller();
        StatsImportWrapperDto statsImportWrapperDto =
                (StatsImportWrapperDto) unmarshaller.unmarshal(new File("src/main/resources/files/xml/stats.xml"));

        StringBuilder sb = new StringBuilder();

        for (StatsImportDto stat : statsImportWrapperDto.getStats()) {
            boolean isValid = validationUtils.isValid(stat);

            Optional<Stat> IsExist = statRepository
                    .findByPassingAndShootingAndEndurance(stat.getPassing(), stat.getShooting(), stat.getEndurance());

            if (IsExist.isPresent()) {
                sb.append(INVALID_STAT).append(System.lineSeparator());
                continue;
            }

            if (!isValid) {
                sb.append(INVALID_STAT).append(System.lineSeparator());
                continue;
            }

            sb.append(String.format(VALID_STAT_FORMAT, stat.getShooting(), stat.getPassing(), stat.getEndurance()))
                    .append(System.lineSeparator());

            Stat statToSave = modelMapper.map(stat, Stat.class);
            statRepository.saveAndFlush(statToSave);

        }

        return sb.toString();
    }

    @Override
    public Stat findById(Long id) {
        return statRepository.findById(id).get();
    }
}
