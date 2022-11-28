package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.Town;

// TODO:
@Repository
public interface TownRepository extends JpaRepository<Town, Integer> {

    Town findByTownName(String townName);
}
