package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.Forecast;
import softuni.exam.models.entity.enums.DayOfWeek;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ForecastRepository extends JpaRepository<Forecast, Long> {

    Optional<Forecast> findByCityIdAndDayOfWeek(Long city, DayOfWeek dayOfWeek);

    Optional<Set<Forecast>> findAllByDayOfWeekAndCityPopulationLessThanOrderByMaxTemperatureDescIdAsc(DayOfWeek dayOfWeek, Long population);
}
