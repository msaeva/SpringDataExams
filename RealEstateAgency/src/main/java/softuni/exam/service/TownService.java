package softuni.exam.service;

import org.springframework.stereotype.Service;
import softuni.exam.models.entity.Town;

import java.io.IOException;

// TODO: Implement all methods
@Service
public interface TownService {

    boolean areImported();

    String readTownsFileContent() throws IOException;
	
	String importTowns() throws IOException;

    Town findTownByName(String townName);
}
