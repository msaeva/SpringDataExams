package exam.model.entities.dtos;

import exam.model.entities.enums.WarrantyType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LaptopsImportDto {
    @Size(min = 8)
    private String macAddress;

    @Positive
    private Double cpuSpeed;

    @Min(8)
    @Max(128)
    private Integer ram;

    @Min(128)
    @Max(1024)
    private Integer storage;

    @Size(min = 10)
    private String description;

    @Positive
    private BigDecimal price;

    @NotNull
    private WarrantyType warrantyType;

    private ShopNameDto shop;
}
