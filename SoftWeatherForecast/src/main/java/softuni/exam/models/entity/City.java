package softuni.exam.models.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLUpdate;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cities")
public class City extends BaseEntity {

    @Column(name = "city_name", nullable = false, unique = true)
    @Size(min = 2, max = 60)
    private String cityName;

    @Size(min = 2)
    @Column(columnDefinition = "TEXT")
    private String description;

    @Min(500)
    @Column(nullable = false)
    private Long population;

    @ManyToOne
    private Country country;

    @OneToMany(targetEntity = Forecast.class, mappedBy = "city")
    private Set<Forecast> forecasts;
}
