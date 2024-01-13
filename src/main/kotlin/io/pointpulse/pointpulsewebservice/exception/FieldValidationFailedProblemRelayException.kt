package io.pointpulse.pointpulsewebservice.exception

import io.pointpulse.pointpulsewebservice.model.FieldValidationError
import io.pointpulse.pointpulsewebservice.util.problemrelay.exception.ProducedProblemRelayException
import io.pointpulse.pointpulsewebservice.util.problemrelaymarkers.ppcommon.markers.InvalidFieldsProblemMarker


class FieldValidationFailedProblemRelayException constructor(
    val fieldValidationErrors: Collection<FieldValidationError>,
    message: String? = null,
) : ProducedProblemRelayException(InvalidFieldsProblemMarker.INVALID_FIELDS, message, fieldValidationErrors) {

}