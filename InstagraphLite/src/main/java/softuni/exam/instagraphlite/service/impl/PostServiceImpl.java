package softuni.exam.instagraphlite.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.instagraphlite.models.Picture;
import softuni.exam.instagraphlite.models.Post;
import softuni.exam.instagraphlite.models.User;
import softuni.exam.instagraphlite.models.dtos.PostImportDto;
import softuni.exam.instagraphlite.models.dtos.PostsImportWrapperDto;
import softuni.exam.instagraphlite.repository.PostRepository;
import softuni.exam.instagraphlite.service.PictureService;
import softuni.exam.instagraphlite.service.PostService;
import softuni.exam.instagraphlite.service.UserService;
import softuni.exam.instagraphlite.util.ValidationUtils;
import softuni.exam.instagraphlite.util.XmlParser;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static softuni.exam.instagraphlite.constants.Messages.INVALID_POST;
import static softuni.exam.instagraphlite.constants.Messages.VALID_POST_FORMAT;
import static softuni.exam.instagraphlite.constants.Paths.POSTS_PATH;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    private final XmlParser xmlParser;
    private final ValidationUtils validationUtils;

    private final UserService userService;

    private final PictureService pictureService;

    public PostServiceImpl(PostRepository postRepository, ModelMapper modelMapper, XmlParser xmlParser, ValidationUtils validationUtils, UserService userService, PictureService pictureService) {
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.validationUtils = validationUtils;
        this.userService = userService;
        this.pictureService = pictureService;
    }

    @Override
    public boolean areImported() {
        return postRepository.count() > 0;
    }

    @Override
    public String readFromFileContent() throws IOException {
        return Files.readString(POSTS_PATH);
    }

    @Override
    public String importPosts() throws IOException, JAXBException {

        final PostsImportWrapperDto postsImportWrapperDto =
                xmlParser.fromFile(POSTS_PATH.toFile(), PostsImportWrapperDto.class);

        final StringBuilder sb = new StringBuilder();

        for (PostImportDto post : postsImportWrapperDto.getPosts()) {
            Boolean isValid = validationUtils.isValid(post);

            if (userService.findByUsername(post.getUser().getUsername()).isEmpty() ||
                    pictureService.findByPath(post.getPicture().getPath()).isEmpty() || !isValid) {

                sb.append(INVALID_POST).append(System.lineSeparator());
                continue;
            }
            sb.append(String.format(VALID_POST_FORMAT, post.getUser().getUsername())).append(System.lineSeparator());

            final User user = userService.findByUsername(post.getUser().getUsername()).get();
            final Picture picture = pictureService.findByPath(post.getPicture().getPath()).get();
            final Post postToSave = modelMapper.map(post, Post.class);

            postToSave.setUser(user);
            postToSave.setPicture(picture);

            postRepository.saveAndFlush(postToSave);
        }

        return sb.toString();
    }
}
