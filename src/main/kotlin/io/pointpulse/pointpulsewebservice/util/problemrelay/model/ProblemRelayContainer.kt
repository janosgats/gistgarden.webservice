package io.pointpulse.pointpulsewebservice.util.problemrelay.model

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode

interface ProblemRelayContainer {
    val marker: ProblemMarker
    val message: String?
    val payload: Any?

    fun matchesMarker(markerToMatch: ProblemMarker): Boolean {
        return marker.matches(markerToMatch)
    }
}

interface ProducedProblemRelayContainer : ProblemRelayContainer {
    val suggestedHttpResponseCode: Int?
}

interface ReceivedProblemRelayContainer : ProblemRelayContainer {
    override val payload: JsonNode?
    val version: String
    val responseCode: Int
    val responseBody: ObjectNode
    val payloadSerializationSucceeded: Boolean
    val timestamp: String
}

class ReceivedProblemRelayContainerImpl(
    override val marker: ProblemMarker,
    override val message: String?,
    override val payload: JsonNode?,
    override val version: String,
    override val responseCode: Int,
    override val responseBody: ObjectNode,
    override val payloadSerializationSucceeded: Boolean,
    override val timestamp: String,
) : ReceivedProblemRelayContainer

class ProducedProblemRelayContainerImpl @JvmOverloads constructor(
    override val marker: ProblemMarker,
    override val message: String? = null,
    override val payload: Any? = null,
    private val problemContainerSuggestedHttpResponseCode: Int? = null,

    ) : ProducedProblemRelayContainer {
    override val suggestedHttpResponseCode: Int?
        get() = problemContainerSuggestedHttpResponseCode ?: marker.subType.suggestedHttpResponseCode ?: marker.mainType.suggestedHttpResponseCode
}