package softuni.exam.instagraphlite.util;

import org.springframework.stereotype.Component;

import javax.validation.Validator;

@Component
public class ValidationUtilsImpl implements ValidationUtils {
    private final Validator validator;

    public ValidationUtilsImpl(Validator validator) {
        this.validator = validator;
    }

    @Override
    public <T> Boolean isValid(T entity) {
        return validator.validate(entity).isEmpty();
    }
}
