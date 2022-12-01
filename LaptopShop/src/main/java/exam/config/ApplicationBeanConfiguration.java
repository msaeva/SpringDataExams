package exam.config;

import com.google.gson.*;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

//ToDo
@Configuration
public class ApplicationBeanConfiguration {

    @Bean
    public static ModelMapper modelMapper() {
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

    @Bean
    public static Gson gson() {
        JsonDeserializer<LocalDate> deserializer = (json, typeOfT, context) ->
                LocalDate.parse(json.getAsString());

        Gson gson = new GsonBuilder().setPrettyPrinting()
                .setDateFormat("dd/mm/yyyy")
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalDate.class, deserializer)
                .create();

        return gson;
    }

    public static class LocalDateAdapter implements JsonSerializer<LocalDate> {
        @Override
        public JsonElement serialize(LocalDate date, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(date.format(DateTimeFormatter.ISO_LOCAL_DATE));
        }
    }
}
