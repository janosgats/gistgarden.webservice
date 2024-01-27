package com.gistgarden.gistgardenwebservice.model


data class FieldValidationError(
    val fieldKey: String,
    val errorMessage: String,
)