package com.example.football.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

//ToDo Create configurations
@Configuration
public class ApplicationBeanConfiguration {

    @Bean
    public Gson createGson() {
        return new GsonBuilder().setPrettyPrinting().create();
    }

    @Bean
    public ModelMapper createModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addConverter(new Converter<String, LocalDate>() {
            @Override
            public LocalDate convert(MappingContext<String, LocalDate> mappingContext) {
                LocalDate parse = LocalDate.parse(mappingContext.getSource(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                return parse;
            }
        });

        return modelMapper;
    }
}
