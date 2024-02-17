package com.gistgarden.gistgardenwebservice.util.problemrelaymarkers.ggws

import com.gistgarden.gistgardenwebservice.util.problemrelay.model.ProblemType

enum class GistGardenWsProblemMainType(
    override val id: Int,
    override val suggestedHttpResponseCode: Int? = null,
) : ProblemType {
    USER(2),
    GROUP(3),
    GROUP_MANAGEMENT(4),
    TOPIC(5),
    UserNotAuthorizedFor(6),
    TOPIC_COMMENT(7),
    ;

    override val readableName: String = this.name
}
