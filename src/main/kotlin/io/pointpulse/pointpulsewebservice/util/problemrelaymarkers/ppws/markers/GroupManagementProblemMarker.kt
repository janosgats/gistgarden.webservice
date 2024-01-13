package io.pointpulse.pointpulsewebservice.util.problemrelaymarkers.ppws.markers

import io.pointpulse.pointpulsewebservice.util.problemrelay.model.ProblemMarker
import io.pointpulse.pointpulsewebservice.util.problemrelay.model.ProblemTypeImpl
import io.pointpulse.pointpulsewebservice.util.problemrelaymarkers.ProblemRelayDomains
import io.pointpulse.pointpulsewebservice.util.problemrelaymarkers.ppws.PointPulseWsProblemMainType


enum class GroupManagementProblemMarker(
    subTypeId: Int,
    suggestedHttpResponseCode: Int?
) : ProblemMarker {
    USER_IS_NOT_AUTHORIZED_FOR_THIS_ACTION_BECAUSE_IT_IS_NOT_A_MEMBER_OF_THE_GROUP(1, 401),
    USER_IS_ALREADY_A_MEMBER_OF_THE_GROUP(2, 409),
    ;

    override val mainType = PointPulseWsProblemMainType.GROUP_MANAGEMENT

    override val subType = ProblemTypeImpl(subTypeId, this.name, suggestedHttpResponseCode)

    override val domain = ProblemRelayDomains.PpWs
}