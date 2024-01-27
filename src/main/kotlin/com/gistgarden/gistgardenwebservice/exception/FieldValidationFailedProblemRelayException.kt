package com.gistgarden.gistgardenwebservice.exception

import com.gistgarden.gistgardenwebservice.model.FieldValidationError
import com.gistgarden.gistgardenwebservice.util.problemrelay.exception.ProducedProblemRelayException
import com.gistgarden.gistgardenwebservice.util.problemrelaymarkers.ggcommon.markers.InvalidFieldsProblemMarker


class FieldValidationFailedProblemRelayException constructor(
    val fieldValidationErrors: Collection<FieldValidationError>,
    message: String? = null,
) : ProducedProblemRelayException(InvalidFieldsProblemMarker.INVALID_FIELDS, message, fieldValidationErrors) {

}