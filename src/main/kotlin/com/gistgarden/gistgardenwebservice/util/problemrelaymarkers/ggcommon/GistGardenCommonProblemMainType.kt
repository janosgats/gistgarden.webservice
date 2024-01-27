package com.gistgarden.gistgardenwebservice.util.problemrelaymarkers.ggcommon

import com.gistgarden.gistgardenwebservice.util.problemrelay.model.ProblemType

enum class GistGardenCommonProblemMainType(
    override val id: Int,
    override val suggestedHttpResponseCode: Int? = null,
) : ProblemType {
    INVALID_FIELDS(1),
    ;

    override val readableName: String = this.name
}
