package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ForecastImportDto;
import softuni.exam.models.dto.ForecastImportWrapperDto;
import softuni.exam.models.entity.City;
import softuni.exam.models.entity.Forecast;
import softuni.exam.models.entity.enums.DayOfWeek;
import softuni.exam.repository.CityRepository;
import softuni.exam.repository.ForecastRepository;
import softuni.exam.service.ForecastService;
import softuni.exam.util.ValidationUtils;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static softuni.exam.constants.Messages.INVALID_FORECAST;
import static softuni.exam.constants.Messages.VALID_FORECAST_FORMAT;
import static softuni.exam.constants.Paths.FORECASTS_PATH;

@Service
public class ForecastServiceImpl implements ForecastService {

    public final String PRINT_FORMAT = "City: %s:%n -min temperature: %.2f%n --max temperature: %.2f%n ---sunrise: %s:%n ----sunset: %s:%n";

    private final ForecastRepository forecastRepository;
    private final ModelMapper mapper;
    private final ValidationUtils validationUtils;
    private final XmlParser xmlParser;
    private final CityRepository cityRepository;

    public ForecastServiceImpl(ForecastRepository forecastRepository,
                               ModelMapper mapper,
                               ValidationUtils validationUtils,
                               XmlParser xmlParser,
                               CityRepository cityRepository) {

        this.forecastRepository = forecastRepository;
        this.mapper = mapper;
        this.validationUtils = validationUtils;
        this.xmlParser = xmlParser;
        this.cityRepository = cityRepository;
    }

    @Override
    public boolean areImported() {
        return forecastRepository.count() > 0;
    }

    @Override
    public String readForecastsFromFile() throws IOException {
        return Files.readString(FORECASTS_PATH);
    }

    @Override
    public String importForecasts() throws IOException, JAXBException {

        final StringBuilder sb = new StringBuilder();

        final File file = FORECASTS_PATH.toFile();

        final ForecastImportWrapperDto forecastImportWrapperDto =
                xmlParser.fromFile(file, ForecastImportWrapperDto.class);

        List<ForecastImportDto> forecasts = forecastImportWrapperDto.getForecasts();

        for (ForecastImportDto forecast : forecasts) {
            boolean isValid = validationUtils.isValid(forecast);

            if (isValid) {
                Optional<City> cityById = cityRepository.findFirstById(forecast.getCity());

                Optional<Forecast> forecastExist = this.forecastRepository
                        .findByCityIdAndDayOfWeek(forecast.getCity(), forecast.getDayOfWeek());


                if (cityById.isPresent() && forecastExist.isEmpty()) {
                    City city = cityById.get();

                    final Forecast forecastToSave = this.mapper.map(forecast, Forecast.class);

                    forecastToSave.setCity(city);

//                    forecastToSave.setSunset(LocalTime.parse(forecast.getSunset(),
//                            DateTimeFormatter.ofPattern("HH:mm:ss")));

                    sb.append(String.format(VALID_FORECAST_FORMAT, forecastToSave.getDayOfWeek(),
                            forecastToSave.getMaxTemperature()));

                    this.forecastRepository.saveAndFlush(forecastToSave);

                } else {
                    sb.append(INVALID_FORECAST).append(System.lineSeparator());
                }

            }
            sb.append(INVALID_FORECAST).append(System.lineSeparator());
        }

        return sb.toString();
    }

    @Override
    public String exportForecasts() {
        Optional<Set<Forecast>> forecasts =
                forecastRepository.findAllByDayOfWeekAndCityPopulationLessThanOrderByMaxTemperatureDescIdAsc(DayOfWeek.SUNDAY, 150000L);

        String collect = forecasts.get()
                .stream()
                .map(forecast -> String.format(PRINT_FORMAT, forecast.getCity().getCityName(),
                        forecast.getMinTemperature(),
                        forecast.getMaxTemperature(),
                        forecast.getSunrise(),
                        forecast.getSunset()))
                .collect(Collectors.joining(System.lineSeparator()));

        return collect;
    }
}
