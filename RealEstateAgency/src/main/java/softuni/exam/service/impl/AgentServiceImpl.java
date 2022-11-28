package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.AgentImportDto;
import softuni.exam.models.entity.Agent;
import softuni.exam.repository.AgentRepository;
import softuni.exam.service.AgentService;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static softuni.exam.constants.Messages.INVALID_AGENT;
import static softuni.exam.constants.Messages.VALID_AGENT_FORMAT;

@Service
public class AgentServiceImpl implements AgentService {
    private final AgentRepository agentRepository;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    private final TownService townService;

    public AgentServiceImpl(AgentRepository agentRepository, Gson gson, ValidationUtil validationUtil, ModelMapper modelMapper, TownService townService) {
        this.agentRepository = agentRepository;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.townService = townService;
    }

    @Override
    public boolean areImported() {
        return agentRepository.count() > 0;
    }

    @Override
    public String readAgentsFromFile() throws IOException {
        return Files.readString(Path.of("src/main/resources/files/json/agents.json"));
    }

    @Override
    public String importAgents() throws IOException {

        List<AgentImportDto> agents = Arrays.stream(gson.fromJson(readAgentsFromFile(), AgentImportDto[].class)).toList();

        StringBuilder sb = new StringBuilder();

        for (AgentImportDto agent : agents) {
            boolean isValid = validationUtil.isValid(agent);

            if (!agent.getEmail().contains("@") && !agent.getEmail().contains(".")) {
                isValid = false;
            }
            if (agentRepository.findByEmail(agent.getEmail()).isPresent()) {
                isValid = false;
            }

            boolean doesntExist = agentRepository.findByFirstName(agent.getFirstName()).isPresent();

            if (doesntExist) {
                isValid = false;
            }

            if (!isValid) {
                sb.append(INVALID_AGENT).append(System.lineSeparator());
                continue;
            }

            sb.append(String.format(VALID_AGENT_FORMAT,
                            agent.getFirstName(), agent.getLastName()))
                    .append(System.lineSeparator());

            Agent agentToSave = modelMapper.map(agent, Agent.class);
            agentToSave.setTown(townService.findTownByName(agent.getTown()));
            agentRepository.saveAndFlush(agentToSave);
        }
        return sb.toString();
    }

    @Override
    public Agent findByFirstName(String firstName) {
        if (agentRepository.findByFirstName(firstName).isEmpty()) {
            return null;
        }
        return agentRepository.findByFirstName(firstName).get();

    }
}
