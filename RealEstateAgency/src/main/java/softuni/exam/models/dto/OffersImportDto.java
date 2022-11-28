package softuni.exam.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Positive;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "offer")
@XmlAccessorType(XmlAccessType.FIELD)
public class OffersImportDto {
    @Positive
    @XmlElement(name = "price")
    private Double price;

    @XmlElement(name = "agent")
    private AgentNameDto name;

    @XmlElement(name = "apartment")
    private ApartmentIdDto apartment;

    @XmlElement(name = "publishedOn")
    private String publishedOn;

}
