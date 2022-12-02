package softuni.exam.instagraphlite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.instagraphlite.models.User;

import java.util.List;
import java.util.Optional;

//ToDo
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Query("select u from User as u " +
            "order by u.posts.size DESC, u.id")
    Optional<List<User>> getUsersWithTheirPosts();
}
