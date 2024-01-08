package io.pointpulse.pointpulsewebservice.util.problemrelaymarkers.markers

import io.pointpulse.pointpulsewebservice.util.problemrelay.model.ProblemMarker
import io.pointpulse.pointpulsewebservice.util.problemrelay.model.ProblemTypeImpl
import io.pointpulse.pointpulsewebservice.util.problemrelaymarkers.PointPulseCommonProblemMainType


enum class FieldValidationFailedProblemMarker(
    subTypeId: Int,
    suggestedHttpResponseCode: Int?
) : ProblemMarker {

    FIELD_VALIDATION_FAILED(1, 400);

    override val mainType = PointPulseCommonProblemMainType.FIELD_VALIDATION_FAILED

    override val subType = ProblemTypeImpl(subTypeId, this.name, suggestedHttpResponseCode)

    override val domain = "io.pointpulse"
}
