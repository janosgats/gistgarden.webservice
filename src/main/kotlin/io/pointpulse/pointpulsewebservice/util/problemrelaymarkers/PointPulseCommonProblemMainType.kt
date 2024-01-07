package io.pointpulse.pointpulsewebservice.util.problemrelaymarkers

import io.pointpulse.pointpulsewebservice.util.problemrelay.model.ProblemType

enum class PointPulseCommonProblemMainType(
    override val id: Int,
    override val suggestedHttpResponseCode: Int? = null,
) : ProblemType {
    FIELD_VALIDATION_FAILED(1),
    ;

    override val readableName: String = this.name
}
