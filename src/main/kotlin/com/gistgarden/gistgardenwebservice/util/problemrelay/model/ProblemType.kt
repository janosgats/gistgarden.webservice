package com.gistgarden.gistgardenwebservice.util.problemrelay.model

interface ProblemType {
    val id: Int
    val readableName: String? // we shouldn't call this "name" because that conflicts with the final "name" field in enums
    val suggestedHttpResponseCode: Int?
}

data class ProblemTypeImpl(
    override val id: Int,
    override val readableName: String?,
    override val suggestedHttpResponseCode: Int? = null,
) : ProblemType