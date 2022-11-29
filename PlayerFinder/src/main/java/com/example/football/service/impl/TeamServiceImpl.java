package com.example.football.service.impl;

import com.example.football.models.dto.TeamsImportDto;
import com.example.football.models.entity.Team;
import com.example.football.models.entity.Town;
import com.example.football.repository.TeamRepository;
import com.example.football.service.TeamService;
import com.example.football.service.TownService;
import com.example.football.util.ValidationUtils;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.example.football.constants.Messages.INVALID_TEAM;
import static com.example.football.constants.Messages.VALID_TEAM_FORMAT;

//ToDo - Implement all methods
@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidationUtils validationUtils;

    private final TownService townService;

    public TeamServiceImpl(TeamRepository teamRepository, Gson gson, ModelMapper modelMapper, ValidationUtils validationUtils, TownService townService) {
        this.teamRepository = teamRepository;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validationUtils = validationUtils;
        this.townService = townService;
    }

    @Override
    public boolean areImported() {
        return teamRepository.count() > 0;
    }

    @Override
    public String readTeamsFileContent() throws IOException {
        return Files.readString(Path.of("src/main/resources/files/json/teams.json"));
    }

    @Override
    public String importTeams() throws IOException {

        List<TeamsImportDto> teamsImportDtos =
                Arrays.stream(gson.fromJson(readTeamsFileContent(), TeamsImportDto[].class)).toList();

        StringBuilder sb = new StringBuilder();

        for (TeamsImportDto team : teamsImportDtos) {
            boolean isValid = validationUtils.isValid(team);

            if (!isValid){
                sb.append(INVALID_TEAM).append(System.lineSeparator());
                continue;
            }

            //•	The provided town names will always be valid.
            Town town = townService.findByName(team.getTownName());

            Team isExistTeam = teamRepository.findByName(team.getName());

            if (isExistTeam != null) {
                sb.append(INVALID_TEAM).append(System.lineSeparator());
                continue;
            }
            sb.append(String.format(VALID_TEAM_FORMAT, team.getName(), team.getFanBase())).append(System.lineSeparator());

            Team teamToSave = modelMapper.map(team, Team.class);
            teamToSave.setTown(town);

            teamRepository.saveAndFlush(teamToSave);
        }
        return sb.toString();
    }

    @Override
    public Team findByName(String name) {
        return teamRepository.findByName(name);
    }
}
