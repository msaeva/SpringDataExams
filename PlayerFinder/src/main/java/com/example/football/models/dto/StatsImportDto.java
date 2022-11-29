package com.example.football.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Positive;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "stat")
@XmlAccessorType(XmlAccessType.FIELD)
public class StatsImportDto {
    @XmlElement
    @Positive
    private Double passing;

    @XmlElement
    @Positive
    private Double shooting;

    @XmlElement
    @Positive
    private Double endurance;
}
