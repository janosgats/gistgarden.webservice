package io.pointpulse.pointpulsewebservice.util.problemrelaymarkers.ppcommon.markers

import io.pointpulse.pointpulsewebservice.util.problemrelay.model.ProblemMarker
import io.pointpulse.pointpulsewebservice.util.problemrelay.model.ProblemTypeImpl
import io.pointpulse.pointpulsewebservice.util.problemrelaymarkers.ProblemRelayDomains
import io.pointpulse.pointpulsewebservice.util.problemrelaymarkers.ppcommon.PointPulseCommonProblemMainType


enum class InvalidFieldsProblemMarker(
    subTypeId: Int,
    suggestedHttpResponseCode: Int?
) : ProblemMarker {

    INVALID_FIELDS(1, 400);

    override val mainType = PointPulseCommonProblemMainType.INVALID_FIELDS

    override val subType = ProblemTypeImpl(subTypeId, this.name, suggestedHttpResponseCode)

    override val domain = ProblemRelayDomains.PpCommon
}
