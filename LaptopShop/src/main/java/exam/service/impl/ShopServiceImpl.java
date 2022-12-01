package exam.service.impl;

import exam.model.entities.Shop;
import exam.model.entities.Town;
import exam.model.entities.dtos.ShopsImportDto;
import exam.model.entities.dtos.ShopsImportWrapperDto;
import exam.repository.ShopRepository;
import exam.service.ShopService;
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

import static exam.constants.Messages.INVALID_SHOP;
import static exam.constants.Messages.VALID_SHOP_FORMAT;

@Service
public class ShopServiceImpl implements ShopService {
    private final ShopRepository shopRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtils validationUtils;

    private final TownService townService;

    public ShopServiceImpl(ShopRepository shopRepository, ModelMapper modelMapper, ValidationUtils validationUtils, TownService townService) {
        this.shopRepository = shopRepository;
        this.modelMapper = modelMapper;
        this.validationUtils = validationUtils;
        this.townService = townService;
    }

    @Override
    public boolean areImported() {
        return shopRepository.count() > 0;
    }

    @Override
    public String readShopsFileContent() throws IOException {
        return Files.readString(Path.of("src/main/resources/files/xml/shops.xml"));
    }

    @Override
    public String importShops() throws JAXBException, FileNotFoundException {
        JAXBContext context = JAXBContext.newInstance(ShopsImportWrapperDto.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        ShopsImportWrapperDto shopsDtos =
                (ShopsImportWrapperDto) unmarshaller.unmarshal(new File("src/main/resources/files/xml/shops.xml"));

        StringBuilder sb = new StringBuilder();

        for (ShopsImportDto shop : shopsDtos.getShops()) {
            boolean isValid = validationUtils.isValid(shop);

            if (shopRepository.findByName(shop.getName()).isPresent() || !isValid) {
                sb.append(INVALID_SHOP).append(System.lineSeparator());
                continue;
            }

            sb.append(String.format(VALID_SHOP_FORMAT, shop.getName(), shop.getIncome()))
                    .append(System.lineSeparator());

            Town town = townService.findByName(shop.getTown().getName());

            Shop shopToSave = modelMapper.map(shop, Shop.class);
            shopToSave.setTown(town);

            shopRepository.saveAndFlush(shopToSave);
        }
        return sb.toString();
    }
}
