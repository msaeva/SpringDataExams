package com.example.football.models.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "stats")
public class Stat extends BaseEntity {

    @Positive
    @Column(nullable = false)
    private Double shooting;

    @Positive
    @Column(nullable = false)
    private Double passing;

    @Positive
    @Column(nullable = false)
    private Double endurance;
}
