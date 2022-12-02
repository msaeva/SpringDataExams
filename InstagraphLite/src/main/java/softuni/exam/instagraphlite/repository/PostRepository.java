package softuni.exam.instagraphlite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.instagraphlite.models.Post;

import java.util.List;
import java.util.Optional;

//ToDo
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

}
