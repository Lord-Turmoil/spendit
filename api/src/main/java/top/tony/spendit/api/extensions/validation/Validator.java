/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package top.tony.spendit.api.extensions.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import top.tony.spendit.api.exceptions.BadRequestException;

public class Validator {
    private Validator() {}

    public static void validate(Object object) {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            jakarta.validation.Validator validator = factory.getValidator();
            var violations = validator.validate(object);
            if (!violations.isEmpty()) {
                var message = String.join("\n", violations.stream().map(ConstraintViolation::getMessage).toArray(String[]::new));
                throw new BadRequestException(message);
            }
        }
    }
}
