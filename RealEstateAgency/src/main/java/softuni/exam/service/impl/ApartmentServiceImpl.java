package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ApartmentImportDto;
import softuni.exam.models.dto.ApartmentsImportWrapperDto;
import softuni.exam.models.entity.Apartment;
import softuni.exam.repository.ApartmentRepository;
import softuni.exam.service.ApartmentService;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static softuni.exam.constants.Messages.INVALID_APARTMENT;
import static softuni.exam.constants.Messages.VALID_APARTMENT_FORMAT;

@Service
public class ApartmentServiceImpl implements ApartmentService {
    private final ApartmentRepository apartmentRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    private final TownService townService;

    public ApartmentServiceImpl(ApartmentRepository apartmentRepository, ValidationUtil validationUtil, ModelMapper modelMapper, TownService townService) {
        this.apartmentRepository = apartmentRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.townService = townService;
    }

    @Override
    public boolean areImported() {
        return apartmentRepository.count() > 0;
    }

    @Override
    public String readApartmentsFromFile() throws IOException {
        return Files.readString(Path.of("src/main/resources/files/xml/apartments.xml"));
    }

    @Override
    public String importApartments() throws IOException, JAXBException {

        JAXBContext context = JAXBContext.newInstance(ApartmentsImportWrapperDto.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        ApartmentsImportWrapperDto wrapperDto = (ApartmentsImportWrapperDto) unmarshaller.unmarshal
                (new File("src/main/resources/files/xml/apartments.xml"));

        StringBuilder sb = new StringBuilder();

        for (ApartmentImportDto apartment : wrapperDto.getApartments()) {
            boolean isValid = validationUtil.isValid(apartment);

            Optional<Apartment> existApartment = apartmentRepository
                    .findByTown_TownNameAndArea(apartment.getTownName(), apartment.getArea());

            if (existApartment.isPresent()) {
                isValid = false;
            }

            if (!isValid) {
                sb.append(INVALID_APARTMENT).append(System.lineSeparator());
                continue;
            }
            sb.append(String.format(VALID_APARTMENT_FORMAT, apartment.getApartmentType(), apartment.getArea()))
                    .append(System.lineSeparator());

            Apartment apartmentToSave = modelMapper.map(apartment, Apartment.class);
            apartmentToSave.setTown(townService.findTownByName(apartment.getTownName()));
            apartmentRepository.saveAndFlush(apartmentToSave);

        }
        return sb.toString();
    }

    @Override
    public Apartment findById(Integer id) {
        return apartmentRepository.findById(id).get();
    }
}
