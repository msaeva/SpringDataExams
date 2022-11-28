package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.OffersImportDto;
import softuni.exam.models.dto.OffersImportWrapperDto;
import softuni.exam.models.entity.Agent;
import softuni.exam.models.entity.Offer;
import softuni.exam.models.enums.ApartmentType;
import softuni.exam.repository.OfferRepository;
import softuni.exam.service.AgentService;
import softuni.exam.service.ApartmentService;
import softuni.exam.service.OfferService;
import softuni.exam.util.ValidationUtil;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static softuni.exam.constants.Messages.INVALID_OFFER;
import static softuni.exam.constants.Messages.VALID_OFFER_FORMAT;

@Service
public class OfferServiceImpl implements OfferService {
    private final OfferRepository offerRepository;
    private final ModelMapper modelMapper;
    private final AgentService agentService;
    private final ValidationUtil validationUtil;
    private final ApartmentService apartmentService;

    public OfferServiceImpl(OfferRepository offerRepository, ModelMapper modelMapper, AgentService agentService, ValidationUtil validationUtil, ApartmentService apartmentService) {
        this.offerRepository = offerRepository;
        this.modelMapper = modelMapper;
        this.agentService = agentService;
        this.validationUtil = validationUtil;
        this.apartmentService = apartmentService;
    }

    @Override
    public boolean areImported() {
        return offerRepository.count() > 0;
    }

    @Override
    public String readOffersFileContent() throws IOException {
        return Files.readString(Path.of("src/main/resources/files/xml/offers.xml"));
    }

    @Override
    public String importOffers() throws IOException, JAXBException {
        JAXBContext context = JAXBContext.newInstance(OffersImportWrapperDto.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        OffersImportWrapperDto offers = (OffersImportWrapperDto) unmarshaller.unmarshal
                (new File("src/main/resources/files/xml/offers.xml"));

        StringBuilder stringBuilder = new StringBuilder();

        for (OffersImportDto offer : offers.getOffers()) {
            boolean isValid = validationUtil.isValid(offer);

            Agent isExist = agentService.findByFirstName(offer.getName().getName());

            if (isExist == null) {
                stringBuilder.append(INVALID_OFFER).append(System.lineSeparator());
                continue;
            }

            if (!isValid) {
                stringBuilder.append(INVALID_OFFER).append(System.lineSeparator());
                continue;
            }

            stringBuilder.append(String.format(VALID_OFFER_FORMAT, offer.getPrice())).append(System.lineSeparator());

            Offer offerToSave = modelMapper.map(offer, Offer.class);
            offerToSave.setAgent(agentService.findByFirstName(offer.getName().getName()));
            offerToSave.setApartment(apartmentService.findById(offer.getApartment().getId()));

            offerRepository.saveAndFlush(offerToSave);

        }

        return stringBuilder.toString();
    }

    @Override
    public String exportOffers() {
        StringBuilder sb = new StringBuilder();

        List<Offer> offerListThreeRooms = offerRepository.findAllByApartment_ApartmentTypeOrderByApartment_AreaDescPriceAsc(ApartmentType.three_rooms);

        offerListThreeRooms.forEach(offer -> sb.append(String.format("Agent %s %s with offer №%d:%n" +
                                "   -Apartment area: %.2f%n" +
                                "   --Town: %s%n" +
                                "   ---Price: %.2f$", offer.getAgent().getFirstName(),
                        offer.getAgent().getLastName(),
                        offer.getId(),
                        offer.getApartment().getArea(),
                        offer.getApartment().getTown().getTownName(),
                        offer.getPrice()))
                .append(System.lineSeparator()));

        return sb.toString();
    }
}
