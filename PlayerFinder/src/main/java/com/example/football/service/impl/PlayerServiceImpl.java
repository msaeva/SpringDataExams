package com.example.football.service.impl;

import com.example.football.models.dto.PlayersImportDto;
import com.example.football.models.dto.PlayersImportWrapperDto;
import com.example.football.models.dto.StatsImportWrapperDto;
import com.example.football.models.entity.Player;
import com.example.football.models.entity.Stat;
import com.example.football.models.entity.Team;
import com.example.football.models.entity.Town;
import com.example.football.repository.PlayerRepository;
import com.example.football.service.PlayerService;
import com.example.football.service.StatService;
import com.example.football.service.TeamService;
import com.example.football.service.TownService;
import com.example.football.util.ValidationUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.example.football.constants.Messages.INVALID_PLAYER;
import static com.example.football.constants.Messages.VALID_PLAYER_FORMAT;

//ToDo - Implement all methods
@Service
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtils validationUtils;
    private final TownService townService;
    private final TeamService teamService;
    private final StatService statService;

    public PlayerServiceImpl(PlayerRepository playerRepository, ModelMapper modelMapper, ValidationUtils validationUtils, TownService townService, TeamService teamService, StatService statService) {
        this.playerRepository = playerRepository;
        this.modelMapper = modelMapper;
        this.validationUtils = validationUtils;
        this.townService = townService;
        this.teamService = teamService;
        this.statService = statService;
    }


    @Override
    public boolean areImported() {
        return playerRepository.count() > 0;
    }

    @Override
    public String readPlayersFileContent() throws IOException {
        return Files.readString(Path.of("src/main/resources/files/xml/players.xml"));
    }

    @Override
    public String importPlayers() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(PlayersImportWrapperDto.class);

        Unmarshaller unmarshaller = context.createUnmarshaller();
        PlayersImportWrapperDto playersImportWrapperDto =
                (PlayersImportWrapperDto) unmarshaller.unmarshal(new File("src/main/resources/files/xml/players.xml"));

        StringBuilder sb = new StringBuilder();

        for (PlayersImportDto player : playersImportWrapperDto.getPlayers()) {
            boolean isValid = validationUtils.isValid(player);

            Optional<Player> playerExistByEmail = playerRepository.findByEmail(player.getEmail());

            if (playerExistByEmail.isPresent() || !isValid) {
                sb.append(INVALID_PLAYER).append(System.lineSeparator());
                continue;
            }

//            if (!isValid) {
//                sb.append(INVALID_PLAYER).append(System.lineSeparator());
//                continue;
//            }
            sb.append(String.format(VALID_PLAYER_FORMAT,
                            player.getFirstName(), player.getLastName(), player.getPosition()))
                    .append(System.lineSeparator());

            // The provided town and team names will always be valid.
            Town town = townService.findByName(player.getTown().getName());
            Team team = teamService.findByName(player.getTeam().getName());

            //•	The Stat id referenced to the valid Stat id.
            Stat stat = statService.findById(player.getStat().getId());

            Player playerToSave = modelMapper.map(player, Player.class);
            playerToSave.setTown(town);
            playerToSave.setTeam(team);
            playerToSave.setStat(stat);

            playerRepository.saveAndFlush(playerToSave);
        }
        return sb.toString();
    }

    @Override
    public String exportBestPlayers() {

        Optional<List<Player>> bestPlayers = playerRepository
                .findAllByBirthdateAfterAndBirthdateBeforeOrderByStat_ShootingDescStat_PassingDescStat_EnduranceDescLastName
                        (LocalDate.of(1995, 1, 2), LocalDate.of(2003, 1, 1));

        StringBuilder stringBuilder = new StringBuilder();

        for (Player player : bestPlayers.get()) {
            String info = player.toString();
            stringBuilder.append(info);
        }
        return stringBuilder.toString();
    }
}
