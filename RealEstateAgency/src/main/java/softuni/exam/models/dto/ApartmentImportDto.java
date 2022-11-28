package softuni.exam.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import softuni.exam.models.enums.ApartmentType;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "apartment")
@XmlAccessorType(XmlAccessType.FIELD)
public class ApartmentImportDto {
    @NotNull
    @XmlElement(name = "apartmentType")
    private ApartmentType apartmentType;

    @Min(40)
    @XmlElement(name = "area")
    private Double area;

    @XmlElement(name = "town")
    private String townName;
}
