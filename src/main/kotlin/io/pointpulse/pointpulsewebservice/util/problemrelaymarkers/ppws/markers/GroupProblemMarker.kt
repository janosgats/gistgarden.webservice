package io.pointpulse.pointpulsewebservice.util.problemrelaymarkers.ppws.markers

import io.pointpulse.pointpulsewebservice.util.problemrelay.model.ProblemMarker
import io.pointpulse.pointpulsewebservice.util.problemrelay.model.ProblemTypeImpl
import io.pointpulse.pointpulsewebservice.util.problemrelaymarkers.ProblemRelayDomains
import io.pointpulse.pointpulsewebservice.util.problemrelaymarkers.ppws.PointPulseWsProblemMainType


enum class GroupProblemMarker(
    subTypeId: Int,
    suggestedHttpResponseCode: Int?
) : ProblemMarker {

    GROUP_NOT_FOUND(1, 404),
    ;

    override val mainType = PointPulseWsProblemMainType.GROUP

    override val subType = ProblemTypeImpl(subTypeId, this.name, suggestedHttpResponseCode)

    override val domain = ProblemRelayDomains.PpWs
}
