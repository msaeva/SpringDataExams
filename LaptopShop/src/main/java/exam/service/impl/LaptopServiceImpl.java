package exam.service.impl;

import com.google.gson.Gson;
import exam.model.entities.Laptop;
import exam.model.entities.Shop;
import exam.model.entities.dtos.LaptopsImportDto;
import exam.repository.LaptopRepository;
import exam.repository.ShopRepository;
import exam.service.LaptopService;
import exam.util.ValidationUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static exam.constants.Messages.INVALID_LAPTOP;
import static exam.constants.Messages.VALID_LAPTOP_FORMAT;

@Service
public class LaptopServiceImpl implements LaptopService {
    private final LaptopRepository laptopRepository;
    private final ValidationUtils validationUtils;
    private final ModelMapper modelMapper;
    private final Gson gson;

    private final ShopRepository shopRepository;

    public LaptopServiceImpl(LaptopRepository laptopRepository, ValidationUtils validationUtils, ModelMapper modelMapper, Gson gson, ShopRepository shopRepository) {
        this.laptopRepository = laptopRepository;
        this.validationUtils = validationUtils;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.shopRepository = shopRepository;
    }

    @Override
    public boolean areImported() {
        return laptopRepository.count() > 0;
    }

    @Override
    public String readLaptopsFileContent() throws IOException {
        return Files.readString(Path.of("src/main/resources/files/json/laptops.json"));
    }

    @Override
    public String importLaptops() throws IOException {
        LaptopsImportDto[] laptopsImportDtos = gson.fromJson(readLaptopsFileContent(), LaptopsImportDto[].class);

        StringBuilder sb = new StringBuilder();

        for (LaptopsImportDto laptop : laptopsImportDtos) {
            boolean isValid = validationUtils.isValid(laptop);

            if (laptopRepository.findByMacAddress(laptop.getMacAddress()).isPresent() || !isValid) {
                sb.append(INVALID_LAPTOP).append(System.lineSeparator());
                continue;
            }
            sb.append(String.format(VALID_LAPTOP_FORMAT, laptop.getMacAddress(), laptop.getCpuSpeed(), laptop.getRam(), laptop.getStorage()))
                    .append(System.lineSeparator());

            Shop shop = shopRepository.findByName(laptop.getShop().getName()).get();

            Laptop laptopToSave = modelMapper.map(laptop, Laptop.class);
            laptopToSave.setShop(shop);

            laptopRepository.saveAndFlush(laptopToSave);
        }
        return sb.toString();
    }

    @Override
    public String exportBestLaptops() {
        Optional<List<Laptop>> bestLaptops = laptopRepository.findBestLaptops();
        if (bestLaptops.isEmpty()) return "no laptops";

        String info = bestLaptops.get().stream().map(Laptop::toString).collect(Collectors.joining());
        return info;
    }
}
