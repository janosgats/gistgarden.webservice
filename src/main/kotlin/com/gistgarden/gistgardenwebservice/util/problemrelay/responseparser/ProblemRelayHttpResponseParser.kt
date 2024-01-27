package com.gistgarden.gistgardenwebservice.util.problemrelay.responseparser

import com.fasterxml.jackson.databind.node.ObjectNode
import com.gistgarden.gistgardenwebservice.util.problemrelay.ProblemRelayConstants
import com.gistgarden.gistgardenwebservice.util.problemrelay.exception.ProblemRelayParsingException
import com.gistgarden.gistgardenwebservice.util.problemrelay.model.ProblemMarkerUtils
import com.gistgarden.gistgardenwebservice.util.problemrelay.model.ReceivedProblemRelayContainer
import com.gistgarden.gistgardenwebservice.util.problemrelay.model.ReceivedProblemRelayContainerImpl
import org.springframework.web.reactive.function.client.ClientResponse
import reactor.core.publisher.Mono

open class ParsingResult(
    val isProblemRelay: Boolean,
    val parsedProblem: ReceivedProblemRelayContainer?,
) {
    companion object {
        val NOT_PROBLEM_RELAY = ParsingResult(false, null)

        fun of(parsedProblem: ReceivedProblemRelayContainer) = ParsingResult(true, parsedProblem)
    }
}

object ProblemRelayHttpResponseParser {
    fun parseReceivedResponse(clientResponseToParse: ClientResponse): Mono<ParsingResult> {
        return parseReceivedResponse(
            clientResponseToParse.statusCode().value(),
            clientResponseToParse.headers().header(ProblemRelayConstants.HTTP_HEADER_NAME_PROBLEM_RELAY_VERSION).firstOrNull(),
            ClientResponseBodyToObjectNodeMonoableAdapter(clientResponseToParse)
        )
    }

    fun parseReceivedResponse(responseCode: Int, versionHeaderValue: String?, bodyMono: BodyToObjectNodeMonoable): Mono<ParsingResult> {
        if (versionHeaderValue == null) {
            return Mono.just(ParsingResult.NOT_PROBLEM_RELAY)
        }
        val objectNodeBodyMono = bodyMono.toObjectNodeMono()

        return when (versionHeaderValue) {
            "1" -> parseResponseForVersion1(responseCode, objectNodeBodyMono)
            else -> throw ProblemRelayParsingException("Unsupported ProblemRelay version received - version: $versionHeaderValue")
        }

    }

    private fun parseResponseForVersion1(
        responseCode: Int,
        bodyMono: Mono<ObjectNode>
    ): Mono<ParsingResult> {
        val LOG_CONTEXT = "parseResponseForVersion1"
        return bodyMono
            .map { body ->
                return@map ParsingResult.of(
                    ReceivedProblemRelayContainerImpl(
                        version = "1",
                        responseCode = responseCode,
                        responseBody = body,
                        marker = ProblemMarkerUtils.parseFromJson(
                            body.getRequiredRealObject("marker", LOG_CONTEXT)
                        ),
                        message = body.getOptionalRealString("message"),
                        payload = body.get("payload"),
                        payloadSerializationSucceeded = body.getOptionalRealBoolean("payloadSerializationSucceeded") ?: false,
                        timestamp = body.getRequiredRealString("timestamp", LOG_CONTEXT),
                    )
                )
            }
    }
}