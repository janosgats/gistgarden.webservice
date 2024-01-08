package io.pointpulse.pointpulsewebservice.util.problemrelaymarkers.markers

import io.pointpulse.pointpulsewebservice.util.problemrelay.model.ProblemMarker
import io.pointpulse.pointpulsewebservice.util.problemrelay.model.ProblemTypeImpl
import io.pointpulse.pointpulsewebservice.util.problemrelaymarkers.PointPulseCommonProblemMainType


enum class TopicProblemMarker(
    subTypeId: Int,
    suggestedHttpResponseCode: Int?
) : ProblemMarker {

    TOPIC_NOT_FOUND(1, 404),
    ;

    override val mainType = PointPulseCommonProblemMainType.TOPIC

    override val subType = ProblemTypeImpl(subTypeId, this.name, suggestedHttpResponseCode)

    override val domain = "io.pointpulse.pointpulse-webservice"
}
