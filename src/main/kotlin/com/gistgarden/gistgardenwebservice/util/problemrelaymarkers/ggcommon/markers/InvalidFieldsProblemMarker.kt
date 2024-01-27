package com.gistgarden.gistgardenwebservice.util.problemrelaymarkers.ggcommon.markers

import com.gistgarden.gistgardenwebservice.util.problemrelay.model.ProblemMarker
import com.gistgarden.gistgardenwebservice.util.problemrelay.model.ProblemTypeImpl
import com.gistgarden.gistgardenwebservice.util.problemrelaymarkers.ProblemRelayDomains
import com.gistgarden.gistgardenwebservice.util.problemrelaymarkers.ggcommon.GistGardenCommonProblemMainType


enum class InvalidFieldsProblemMarker(
    subTypeId: Int,
    suggestedHttpResponseCode: Int?
) : ProblemMarker {

    INVALID_FIELDS(1, 400);

    override val mainType = GistGardenCommonProblemMainType.INVALID_FIELDS

    override val subType = ProblemTypeImpl(subTypeId, this.name, suggestedHttpResponseCode)

    override val domain = ProblemRelayDomains.GgCommon
}
