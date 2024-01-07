package io.pointpulse.pointpulsewebservice.model


data class FieldValidationError(
    val fieldKey: String,
    val errorMessage: String,
)