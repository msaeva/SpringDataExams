package softuni.exam.models.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import softuni.exam.models.enums.ApartmentType;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "apartments")
public class Apartment extends BaseEntity {

    @Column(name = "apartment_type", nullable = false)
    @Enumerated
    private ApartmentType apartmentType;

    @Column(nullable = false)
    @Min(39)
    private Double area;

    @ManyToOne
    private Town town;

    @OneToMany(targetEntity = Offer.class, mappedBy = "apartment")
    private Set<Offer> offers;
}
