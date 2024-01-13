package io.pointpulse.pointpulsewebservice.util.problemrelaymarkers.ppws

import io.pointpulse.pointpulsewebservice.util.problemrelay.model.ProblemType

enum class PointPulseWsProblemMainType(
    override val id: Int,
    override val suggestedHttpResponseCode: Int? = null,
) : ProblemType {
    USER(2),
    GROUP(3),
    GROUP_MANAGEMENT(4),
    TOPIC(5),
    ;

    override val readableName: String = this.name
}
