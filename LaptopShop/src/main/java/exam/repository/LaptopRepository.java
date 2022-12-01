package exam.repository;

import exam.model.entities.Laptop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//ToDo:
@Repository
public interface LaptopRepository extends JpaRepository<Laptop, Long> {
    Optional<Laptop> findByMacAddress(String macAddress);

    @Query(value = "select l from Laptop as l order by l.cpuSpeed DESC, l.ram desc," +
            "l.storage desc, l.macAddress")
    Optional<List<Laptop>> findBestLaptops();
}
