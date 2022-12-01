package exam.model.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Getter@NoArgsConstructor
@Setter
@AllArgsConstructor
@Entity
@Table(name = "towns")
public class Town extends BaseEntity{

    @Size(min = 2)
    @Column(unique = true, nullable = false)
    private String name;

    @Positive
    @Column(nullable = false)
    private Integer population;


    @Size(min = 10)
    @Column(columnDefinition = "TEXT", name = "travel_guide", nullable = false)
    private String travelGuide;
}
