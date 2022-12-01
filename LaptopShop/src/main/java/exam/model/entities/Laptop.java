package exam.model.entities;

import exam.model.entities.enums.WarrantyType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "laptops")
public class Laptop extends BaseEntity {

    @Size(min = 8)
    @Column(name = "mac_address", unique = true, nullable = false)
    private String macAddress;

    @Positive
    @Column(name = "cpu_speed", nullable = false)
    private Double cpuSpeed;

    @Min(8)
    @Max(128)
    @Column(nullable = false)
    private Integer ram;

    @Min(128)
    @Max(1024)
    @Column(nullable = false)
    private Integer storage;


    @Size(min = 10)
    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(nullable = false)
    @Positive
    private BigDecimal price;

    @Column(name = "warranty_type", nullable = false)
    private WarrantyType warrantyType;

    @ManyToOne(optional = false)
    private Shop shop;

    @Override
    public String toString() {
        return String.format("Laptop - %s", this.getMacAddress()) + System.lineSeparator() +
                String.format("*Cpu speed - %.2f", this.getCpuSpeed()) + System.lineSeparator() +
                String.format("**Ram - %d", this.getRam()) + System.lineSeparator() +
                String.format("***Storage - %d", this.getStorage()) + System.lineSeparator() +
                String.format("****Price - %.2f", this.getPrice()) + System.lineSeparator() +
                String.format("Shop name - %s", this.getShop().getName()) + System.lineSeparator() +
                String.format("##Town - %s", this.getShop().getTown().getName()) + System.lineSeparator();
    }
}
