package exam.service.impl;

import exam.model.entities.Shop;
import exam.model.entities.Town;
import exam.model.entities.dtos.TownsImportDto;
import exam.model.entities.dtos.TownsImportWrapperDto;
import exam.repository.TownRepository;
import exam.service.TownService;
import exam.util.ValidationUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static exam.constants.Messages.INVALID_TOWN;
import static exam.constants.Messages.VALID_TOWN_FORMAT;

@Service
public class TownServiceImpl implements TownService {
    private final TownRepository townRepository;
    private final ModelMapper modelMapper;

    private final ValidationUtils validationUtils;


    public TownServiceImpl(TownRepository townRepository, ModelMapper modelMapper, ValidationUtils validationUtils) {
        this.townRepository = townRepository;
        this.modelMapper = modelMapper;
        this.validationUtils = validationUtils;
    }

    @Override
    public boolean areImported() {
        return townRepository.count() > 0;
    }

    @Override
    public String readTownsFileContent() throws IOException {
        return Files.readString(Path.of("src/main/resources/files/xml/towns.xml"));
    }

    @Override
    public String importTowns() throws JAXBException, FileNotFoundException {
        JAXBContext context = JAXBContext.newInstance(TownsImportWrapperDto.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        TownsImportWrapperDto townsDtos =
                (TownsImportWrapperDto) unmarshaller.unmarshal(new File("src/main/resources/files/xml/towns.xml"));

        StringBuilder sb = new StringBuilder();

        for (TownsImportDto town : townsDtos.getTowns()) {
            boolean isValid = validationUtils.isValid(town);

            if (!isValid) {
                sb.append(INVALID_TOWN).append(System.lineSeparator());
                continue;
            }
            sb.append(String.format(VALID_TOWN_FORMAT, town.getName())).append(System.lineSeparator());

            Town townToSave = modelMapper.map(town, Town.class);

            townRepository.saveAndFlush(townToSave);
        }

        return sb.toString();
    }

    @Override
    public Town findByName(String name) {
        //•	The provided town names will always be valid.
        return townRepository.findByName(name).get();
    }
}
