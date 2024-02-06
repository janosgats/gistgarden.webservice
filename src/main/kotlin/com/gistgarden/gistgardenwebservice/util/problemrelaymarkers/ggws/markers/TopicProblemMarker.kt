package com.gistgarden.gistgardenwebservice.util.problemrelaymarkers.ggws.markers

import com.gistgarden.gistgardenwebservice.util.problemrelay.model.ProblemMarker
import com.gistgarden.gistgardenwebservice.util.problemrelay.model.ProblemTypeImpl
import com.gistgarden.gistgardenwebservice.util.problemrelaymarkers.ProblemRelayDomains
import com.gistgarden.gistgardenwebservice.util.problemrelaymarkers.ggws.GistGardenWsProblemMainType


enum class TopicProblemMarker(
    subTypeId: Int,
    suggestedHttpResponseCode: Int?
) : ProblemMarker {

    TOPIC_NOT_FOUND(1, 404),
    USER_IS_NOT_AUTHORIZED_FOR_THIS_ACTION_BECAUSE_IT_IS_NOT_THE_CREATOR_OF_THE_TOPIC(2, 403),
    ;

    override val mainType = GistGardenWsProblemMainType.TOPIC

    override val subType = ProblemTypeImpl(subTypeId, this.name, suggestedHttpResponseCode)

    override val domain = ProblemRelayDomains.GgWs
}
