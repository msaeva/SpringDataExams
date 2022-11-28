package softuni.exam.service;

import org.springframework.stereotype.Service;
import softuni.exam.models.entity.Apartment;

import javax.xml.bind.JAXBException;
import java.io.IOException;

@Service
// TODO: Implement all methods
public interface ApartmentService {
    
    boolean areImported();

    String readApartmentsFromFile() throws IOException;

    String importApartments() throws IOException, JAXBException;

    Apartment findById(Integer id);
}
