package softuni.exam.instagraphlite.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.instagraphlite.models.Picture;
import softuni.exam.instagraphlite.models.Post;
import softuni.exam.instagraphlite.models.User;
import softuni.exam.instagraphlite.models.dtos.UserImportDto;
import softuni.exam.instagraphlite.repository.UserRepository;
import softuni.exam.instagraphlite.service.PictureService;
import softuni.exam.instagraphlite.service.UserService;
import softuni.exam.instagraphlite.util.ValidationUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static softuni.exam.instagraphlite.constants.Messages.INVALID_USER;
import static softuni.exam.instagraphlite.constants.Messages.VALID_USER_FORMAT;
import static softuni.exam.instagraphlite.constants.Paths.USERS_PATH;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtils validationUtils;
    private final PictureService pictureService;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, Gson gson, ValidationUtils validationUtils, PictureService pictureService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtils = validationUtils;
        this.pictureService = pictureService;
    }

    @Override
    public boolean areImported() {
        return userRepository.count() > 0;
    }

    @Override
    public String readFromFileContent() throws IOException {
        return Files.readString(USERS_PATH);
    }

    @Override
    public String importUsers() throws IOException {
        UserImportDto[] users = gson.fromJson(readFromFileContent(), UserImportDto[].class);

        StringBuilder sb = new StringBuilder();

        for (UserImportDto user : users) {
            Boolean isValid = validationUtils.isValid(user);


            if (pictureService.findByPath(user.getProfilePicture()).isEmpty() || !isValid) {
                sb.append(INVALID_USER).append(System.lineSeparator());
                continue;
            }
            sb.append(String.format(VALID_USER_FORMAT, user.getUsername()))
                    .append(System.lineSeparator());

            Picture picture = pictureService.findByPath(user.getProfilePicture()).get();
            User userToSave = modelMapper.map(user, User.class);
            userToSave.setProfilePicture(picture);

            userRepository.saveAndFlush(userToSave);
        }

        return sb.toString();
    }

    @Override
    public String exportUsersWithTheirPosts() {
        List<User> users = userRepository.getUsersWithTheirPosts().get();

        final StringBuilder sb = new StringBuilder();

        for (User user : users) {
            sb.append(user);
            sb.append(user
                    .getPosts()
                    .stream()
                    .sorted(Comparator.comparing(post -> post.getPicture().getSize()))
                    .map(Post::toString)
                    .collect(Collectors.joining()));
        }
        return sb.toString();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
