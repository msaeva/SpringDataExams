package com.example.football.models.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "towns")
public class Town extends BaseEntity {
    @Column(unique = true, nullable = false)
    @Size(min = 2)
    private String name;

    @Positive
    @Column(nullable = false)
    private Integer population;

    @Column(name = "travel_guide", columnDefinition = "TEXT", nullable = false)
    @Size(min = 10)
    private String travelGuide;

}
