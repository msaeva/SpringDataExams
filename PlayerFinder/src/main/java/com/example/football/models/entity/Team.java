package com.example.football.models.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "teams")
public class Team extends BaseEntity{
    @Column(unique = true, nullable = false)
    @Size(min = 3)
    private String name;

    @Column(name = "stadium_name", nullable = false)
    @Size(min = 3)
    private String stadiumName;

    @Min(1000)
    @Column(name = "fan_base")
    private Long fanBase;

    @Size(min = 10)
    @Column(columnDefinition = "TEXT", nullable = false)
    private String history;

    @ManyToOne
    private Town town;

}
