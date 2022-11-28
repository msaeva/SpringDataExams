package softuni.exam.util;

import org.springframework.context.annotation.Configuration;

@Configuration
public interface ValidationUtils {
    <E> boolean isValid(E entity);
}
