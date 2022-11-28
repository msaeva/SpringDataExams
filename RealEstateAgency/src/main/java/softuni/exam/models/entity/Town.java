package softuni.exam.models.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter@Setter
@NoArgsConstructor
@Entity
@Table(name = "towns")
public class Town extends BaseEntity{

    @Size(min = 2)
    @Column(name = "town_name", unique = true, nullable = false)
    private String townName;

    @Positive
    @Column(nullable = false)
    private Integer population;

    @OneToMany(targetEntity = Agent.class, mappedBy = "town")
    private Set<Agent> agents;

    @OneToMany(targetEntity = Apartment.class, mappedBy = "town")
    private Set<Apartment> apartments;
}
