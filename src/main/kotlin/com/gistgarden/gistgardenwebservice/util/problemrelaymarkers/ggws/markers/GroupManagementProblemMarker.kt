package com.gistgarden.gistgardenwebservice.util.problemrelaymarkers.ggws.markers

import com.gistgarden.gistgardenwebservice.util.problemrelay.model.ProblemMarker
import com.gistgarden.gistgardenwebservice.util.problemrelay.model.ProblemTypeImpl
import com.gistgarden.gistgardenwebservice.util.problemrelaymarkers.ProblemRelayDomains
import com.gistgarden.gistgardenwebservice.util.problemrelaymarkers.ggws.GistGardenWsProblemMainType


enum class GroupManagementProblemMarker(
    subTypeId: Int,
    suggestedHttpResponseCode: Int?
) : ProblemMarker {
    USER_IS_NOT_AUTHORIZED_FOR_THIS_ACTION_BECAUSE_IT_IS_NOT_A_MEMBER_OF_THE_GROUP(1, 401),
    USER_IS_ALREADY_A_MEMBER_OF_THE_GROUP(2, 409),
    ;

    override val mainType = GistGardenWsProblemMainType.GROUP_MANAGEMENT

    override val subType = ProblemTypeImpl(subTypeId, this.name, suggestedHttpResponseCode)

    override val domain = ProblemRelayDomains.GgWs
}
