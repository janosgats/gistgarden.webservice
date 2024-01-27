package com.gistgarden.gistgardenwebservice.util.problemrelaymarkers.ggws.markers

import com.gistgarden.gistgardenwebservice.util.problemrelay.model.ProblemMarker
import com.gistgarden.gistgardenwebservice.util.problemrelay.model.ProblemTypeImpl
import com.gistgarden.gistgardenwebservice.util.problemrelaymarkers.ProblemRelayDomains
import com.gistgarden.gistgardenwebservice.util.problemrelaymarkers.ggws.GistGardenWsProblemMainType


enum class GroupProblemMarker(
    subTypeId: Int,
    suggestedHttpResponseCode: Int?
) : ProblemMarker {

    GROUP_NOT_FOUND(1, 404),
    ;

    override val mainType = GistGardenWsProblemMainType.GROUP

    override val subType = ProblemTypeImpl(subTypeId, this.name, suggestedHttpResponseCode)

    override val domain = ProblemRelayDomains.GgWs
}
