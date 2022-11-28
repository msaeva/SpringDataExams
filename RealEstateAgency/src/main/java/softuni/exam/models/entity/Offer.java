package softuni.exam.models.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "offers")
public class Offer extends BaseEntity {

    @Column
    @Positive
    private Double price;

    @Column(name = "published_on")
    private LocalDate publishedOn;

    @ManyToOne
    private Apartment apartment;

    @ManyToOne
    private Agent agent;
}
