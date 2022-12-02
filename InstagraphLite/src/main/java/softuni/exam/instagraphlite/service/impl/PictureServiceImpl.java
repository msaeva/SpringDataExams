package softuni.exam.instagraphlite.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.instagraphlite.models.Picture;
import softuni.exam.instagraphlite.models.dtos.PictureImportDto;
import softuni.exam.instagraphlite.repository.PictureRepository;
import softuni.exam.instagraphlite.service.PictureService;
import softuni.exam.instagraphlite.util.ValidationUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static softuni.exam.instagraphlite.constants.Messages.INVALID_PICTURE;
import static softuni.exam.instagraphlite.constants.Messages.VALID_PICTURE_FORMAT;
import static softuni.exam.instagraphlite.constants.Paths.PICTURES_PATH;

@Service
public class PictureServiceImpl implements PictureService {
    private final PictureRepository pictureRepository;
    private final Gson gson;
    private final ModelMapper mapper;
    private final ValidationUtils validationUtils;

    public PictureServiceImpl(PictureRepository pictureRepository, Gson gson, ModelMapper mapper, ValidationUtils validationUtils) {
        this.pictureRepository = pictureRepository;
        this.gson = gson;
        this.mapper = mapper;
        this.validationUtils = validationUtils;
    }

    @Override
    public boolean areImported() {
        return pictureRepository.count() > 0;
    }

    @Override
    public String readFromFileContent() throws IOException {
        return Files.readString(PICTURES_PATH);
    }

    @Override
    public String importPictures() throws IOException {

        List<PictureImportDto> pictureImportDtos =
                Arrays.stream(gson.fromJson(readFromFileContent(), PictureImportDto[].class))
                        .toList();

        StringBuilder sb = new StringBuilder();

        for (PictureImportDto picture : pictureImportDtos) {
            Boolean isValid = validationUtils.isValid(picture);

            if (!isValid || pictureRepository.findByPath(picture.getPath()).isPresent()) {
                sb.append(INVALID_PICTURE).append(System.lineSeparator());
                continue;
            }


            sb.append(String.format(VALID_PICTURE_FORMAT, picture.getSize())).append(System.lineSeparator());
            Picture pictureToSave = mapper.map(picture, Picture.class);

            pictureRepository.saveAndFlush(pictureToSave);
        }
        return sb.toString();
    }

    @Override
    public String exportPictures() {
        return null;
    }

    @Override
    public Optional<Picture> findByPath(String path) {
        return pictureRepository.findByPath(path);
    }
}
