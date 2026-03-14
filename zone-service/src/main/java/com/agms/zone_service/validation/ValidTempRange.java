package com.agms.zone_service.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Class-level constraint that verifies minTemp < maxTemp when both are provided.
 * Apply this annotation on the class (not a field).
 */
@Documented
@Constraint(validatedBy = TempRangeValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTempRange {

    String message() default "minTemp must be less than maxTemp";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
