package com.example.football.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TownsImportDto {
    @Size(min = 2)
    private String name;

    @Positive
    private Integer population;

    @Size(min = 10)
    private String travelGuide;
}
