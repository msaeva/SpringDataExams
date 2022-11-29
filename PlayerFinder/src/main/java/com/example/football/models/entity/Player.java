package com.example.football.models.entity;

import com.example.football.models.enums.Position;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "players")
public class Player extends BaseEntity {

    @Column(name = "first_name")
    @Size(min = 3)
    private String firstName;

    @Column(name = "last_name")
    @Size(min = 3)
    private String lastName;

    @Column(nullable = false)
    private String email;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthdate;

    private Position position;

    @ManyToOne
    private Team team;

    @ManyToOne
    private Town town;

    @ManyToOne
    private Stat stat;

    @Override
    public String toString() {
        return "Player - " + this.getFirstName() + " " + this.getLastName() + System.lineSeparator() +
                "   Position - " + this.getPosition() +  System.lineSeparator()  +
                "   Team - " + this.getTeam().getName() +  System.lineSeparator() +
                "   Stadium - " + this.getTeam().getStadiumName() +  System.lineSeparator();
    }
}
