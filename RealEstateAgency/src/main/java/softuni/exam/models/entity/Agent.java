package softuni.exam.models.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "agents")
public class Agent extends BaseEntity {

    @Size(min = 2)
    @Column(name = "first_name", unique = true, nullable = false)
    private String firstName;

    @Size(min = 2)
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @ManyToOne
    private Town town;

    @OneToMany(targetEntity = Offer.class, mappedBy = "agent")
    private Set<Offer> offers;

}
