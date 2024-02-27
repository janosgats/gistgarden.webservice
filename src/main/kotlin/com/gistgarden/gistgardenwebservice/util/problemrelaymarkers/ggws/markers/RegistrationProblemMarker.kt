package com.gistgarden.gistgardenwebservice.util.problemrelaymarkers.ggws.markers

import com.gistgarden.gistgardenwebservice.util.problemrelay.model.ProblemMarker
import com.gistgarden.gistgardenwebservice.util.problemrelay.model.ProblemTypeImpl
import com.gistgarden.gistgardenwebservice.util.problemrelaymarkers.ProblemRelayDomains
import com.gistgarden.gistgardenwebservice.util.problemrelaymarkers.ggws.GistGardenWsProblemMainType


enum class RegistrationProblemMarker(
    subTypeId: Int,
    suggestedHttpResponseCode: Int?
) : ProblemMarker {
    EMAIL_PASSWORD_REGISTRATION_INQUIRY_NOT_FOUND(1, 404),
    EMAIL_IS_ALREADY_TAKEN(2, 409),
    ;

    override val mainType = GistGardenWsProblemMainType.REGISTRATION

    override val subType = ProblemTypeImpl(subTypeId, this.name, suggestedHttpResponseCode)

    override val domain = ProblemRelayDomains.GgWs
}
