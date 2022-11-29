package com.example.football.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Primary;

import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "player")
@XmlAccessorType(XmlAccessType.FIELD)
public class PlayersImportDto {
    @Size(min = 3)
    @XmlElement(name = "first-name")
    private String firstName;

    @Size(min = 3)
    @XmlElement(name = "last-name")
    private String lastName;

    @XmlElement(name = "email")
    private String email;

    @XmlElement(name = "birth-date")
    private String birthdate;

    @XmlElement(name = "position")
    private String position;

    @XmlElement(name = "town")
    private TownNameDto town;

    @XmlElement(name = "team")
    private TeamNameDto team;

    @XmlElement(name = "stat")
    private StatIdDto stat;

}
