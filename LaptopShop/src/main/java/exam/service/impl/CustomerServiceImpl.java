package exam.service.impl;

import com.google.gson.Gson;
import exam.model.entities.Customer;
import exam.model.entities.Town;
import exam.model.entities.dtos.CustomerImportDto;
import exam.repository.CustomerRepository;
import exam.service.CustomerService;
import exam.service.TownService;
import exam.util.ValidationUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static exam.constants.Messages.INVALID_CUSTOMER;
import static exam.constants.Messages.VALID_CUSTOMER_FORMAT;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtils validationUtils;
    private final TownService townService;

    public CustomerServiceImpl(CustomerRepository customerRepository, ModelMapper modelMapper, Gson gson, ValidationUtils validationUtils, TownService townService) {
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtils = validationUtils;
        this.townService = townService;
    }

    @Override
    public boolean areImported() {
        return customerRepository.count() > 0;
    }

    @Override
    public String readCustomersFileContent() throws IOException {
        return Files.readString(Path.of("src/main/resources/files/json/customers.json"));
    }

    @Override
    public String importCustomers() throws IOException {
        CustomerImportDto[] customerImportDtos = gson.fromJson(readCustomersFileContent(), CustomerImportDto[].class);

        StringBuilder sb = new StringBuilder();

        for (CustomerImportDto customer : customerImportDtos) {
            boolean isValid = validationUtils.isValid(customer);

            if (customerRepository.findByEmail(customer.getEmail()).isPresent() || !isValid) {
                sb.append(INVALID_CUSTOMER).append(System.lineSeparator());
                continue;
            }

            sb.append(String.format(VALID_CUSTOMER_FORMAT, customer.getFirstName(), customer.getLastName(), customer.getEmail()))
                    .append(System.lineSeparator());

            Town town = townService.findByName(customer.getTown().getName());

            Customer customerToSave = modelMapper.map(customer, Customer.class);
            customerToSave.setTown(town);

            customerRepository.saveAndFlush(customerToSave);
        }
        return sb.toString();
    }
}
