package com.gistgarden.gistgardenwebservice.util.problemrelaymarkers.ggws.markers

import com.gistgarden.gistgardenwebservice.util.problemrelay.model.ProblemMarker
import com.gistgarden.gistgardenwebservice.util.problemrelay.model.ProblemTypeImpl
import com.gistgarden.gistgardenwebservice.util.problemrelaymarkers.ProblemRelayDomains
import com.gistgarden.gistgardenwebservice.util.problemrelaymarkers.ggws.GistGardenWsProblemMainType


enum class UserProblemMarker(
    subTypeId: Int,
    suggestedHttpResponseCode: Int?
) : ProblemMarker {

    USER_NOT_FOUND(1, 404),
    ;

    override val mainType = GistGardenWsProblemMainType.USER

    override val subType = ProblemTypeImpl(subTypeId, this.name, suggestedHttpResponseCode)

    override val domain = ProblemRelayDomains.GgWs
}
