package io.pointpulse.pointpulsewebservice.util.problemrelaymarkers.ppcommon

import io.pointpulse.pointpulsewebservice.util.problemrelay.model.ProblemType

enum class PointPulseCommonProblemMainType(
    override val id: Int,
    override val suggestedHttpResponseCode: Int? = null,
) : ProblemType {
    INVALID_FIELDS(1),
    ;

    override val readableName: String = this.name
}
