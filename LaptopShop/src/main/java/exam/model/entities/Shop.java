package exam.model.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "shops")
public class Shop extends BaseEntity{

    @Size(min = 4)
    @Column(unique = true, nullable = false)
    private String name;

    @Min(20000)
    @Column(nullable = false)
    private BigDecimal income;

    @Size(min = 4)
    @Column(nullable = false)
    private String	address;

    @Min(1)
    @Max(50)
    @Column(name = "employeeCount", nullable = false)
    private Integer employeeCount;

    @Min(150)
    @Column(name = "shop_area", nullable = false)
    private Integer shopArea;

    @ManyToOne(optional = false)
    private Town town;
}
