package com.agms.zone_service.validation;

import com.agms.zone_service.dto.ZoneRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validates that ZoneRequest.minTemp is strictly less than ZoneRequest.maxTemp
 * when both values are provided.
 */
public class TempRangeValidator implements ConstraintValidator<ValidTempRange, ZoneRequest> {

    @Override
    public boolean isValid(ZoneRequest request, ConstraintValidatorContext context) {
        if (request == null) return true;

        Double min = request.getMinTemp();
        Double max = request.getMaxTemp();

        // If either is absent, skip this cross-field check
        if (min == null || max == null) return true;

        boolean valid = min < max;
        if (!valid) {
            // Attach the violation to the minTemp field for cleaner error messages
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                            "minTemp (" + min + ") must be strictly less than maxTemp (" + max + ")")
                    .addPropertyNode("minTemp")
                    .addConstraintViolation();
        }
        return valid;
    }
}
