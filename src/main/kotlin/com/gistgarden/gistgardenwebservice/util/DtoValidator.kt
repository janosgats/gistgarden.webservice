package com.gistgarden.gistgardenwebservice.util

import com.gistgarden.gistgardenwebservice.exception.FieldValidationFailedProblemRelayException
import com.gistgarden.gistgardenwebservice.model.FieldValidationError
import jakarta.validation.ConstraintViolation
import jakarta.validation.Validation
import jakarta.validation.Validator

object DtoValidator {
    private var validator: Validator = Validation.buildDefaultValidatorFactory().validator

    fun assert(dtoToValidate: Any) {
        val violations = getViolations(dtoToValidate)

        if (violations.isNotEmpty()) {
            throw FieldValidationFailedProblemRelayException(
                fieldValidationErrors = violations.map {
                    FieldValidationError(
                        fieldKey = it.propertyPath.toString(),
                        errorMessage = it.message,
                    )
                }
            )
        }
    }

    fun isValid(toValidate: Any): Boolean = getViolations(toValidate).isEmpty()


    fun <T> getViolations(dtoToValidate: T): MutableSet<ConstraintViolation<T>> {
        return validator.validate(dtoToValidate)
    }
}