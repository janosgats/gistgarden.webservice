package io.pointpulse.pointpulsewebservice.exception

import io.pointpulse.pointpulsewebservice.model.FieldValidationError
import io.pointpulse.pointpulsewebservice.util.problemrelay.exception.ProducedProblemRelayException
import io.pointpulse.pointpulsewebservice.util.problemrelaymarkers.FieldValidationFailedProblemMarker


class FieldValidationFailedProblemRelayException constructor(
    val fieldValidationErrors: Collection<FieldValidationError>,
    message: String? = null,
) : ProducedProblemRelayException(FieldValidationFailedProblemMarker.FIELD_VALIDATION_FAILED, message, fieldValidationErrors) {

}