package com.gistgarden.gistgardenwebservice.util.problemrelay.responsefactory

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.gistgarden.gistgardenwebservice.util.problemrelay.ProblemRelayConstants
import com.gistgarden.gistgardenwebservice.util.problemrelay.model.ProducedProblemRelayContainer
import com.gistgarden.gistgardenwebservice.util.problemrelay.model.ReceivedProblemRelayContainer
import java.time.Instant

data class ProblemHeader(
    val name: String,
    val value: String,
)

data class ProblemRelayResponseInstruction(
    val body: ObjectNode,
    val statusCode: Int,
    val header: ProblemHeader,
)

private val objectMapper = ObjectMapper().apply {
    registerModule(JavaTimeModule())
    configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
}

object ProblemRelayHttpResponseFactory {
    fun createV1(problem: ProducedProblemRelayContainer): ProblemRelayResponseInstruction {
        var serializedPayload: JsonNode? = null
        var payloadSerializationSucceeded: Boolean

        try {
            if (problem.payload != null) {
                serializedPayload = objectMapper.valueToTree(problem.payload)
            }
            payloadSerializationSucceeded = true
        } catch (e: Exception) {
            payloadSerializationSucceeded = false
        }

        val body = ObjectNode(JsonNodeFactory.instance).apply {
            replace("marker", problem.marker.toJsonBody())
            serializedPayload?.let { replace("payload", it) }
            problem.message?.let { put("message", it) }
            put("payloadSerializationSucceeded", payloadSerializationSucceeded)
            put("timestamp", Instant.now().toString())
        }

        return ProblemRelayResponseInstruction(
            body,
            problem.suggestedHttpResponseCode ?: 409,
            ProblemHeader(ProblemRelayConstants.HTTP_HEADER_NAME_PROBLEM_RELAY_VERSION, "1")
        )
    }

    fun createV1(problem: ReceivedProblemRelayContainer): ProblemRelayResponseInstruction {
        var serializedPayload: JsonNode? = null
        var payloadSerializationSucceeded: Boolean

        try {
            if (problem.payload != null) {
                serializedPayload = objectMapper.valueToTree(problem.payload)
            }
            payloadSerializationSucceeded = true
        } catch (e: Exception) {
            payloadSerializationSucceeded = false
        }

        val body = ObjectNode(JsonNodeFactory.instance).apply {
            replace("marker", problem.marker.toJsonBody())
            serializedPayload?.let { replace("payload", it) }
            problem.message?.let { put("message", "Problem received from an upstream service: $it") }
            put("payloadSerializationSucceeded", payloadSerializationSucceeded)
            put("timestamp", Instant.now().toString())
        }

        return ProblemRelayResponseInstruction(body, problem.responseCode, ProblemHeader(ProblemRelayConstants.HTTP_HEADER_NAME_PROBLEM_RELAY_VERSION, "1"))
    }
}