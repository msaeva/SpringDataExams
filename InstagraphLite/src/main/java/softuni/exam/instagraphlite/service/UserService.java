package softuni.exam.instagraphlite.service;

import softuni.exam.instagraphlite.models.User;

import java.io.IOException;
import java.util.Optional;

public interface UserService {
    boolean areImported();
    String readFromFileContent() throws IOException;
    String importUsers() throws IOException;
    String exportUsersWithTheirPosts();

    Optional<User> findByUsername(String username);
}
