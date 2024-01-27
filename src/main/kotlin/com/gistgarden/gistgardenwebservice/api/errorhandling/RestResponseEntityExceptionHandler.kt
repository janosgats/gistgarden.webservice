package com.gistgarden.gistgardenwebservice.api.errorhandling

import com.fasterxml.jackson.databind.node.ObjectNode
import com.gistgarden.gistgardenwebservice.util.logging.LoggerDelegate
import com.gistgarden.gistgardenwebservice.util.problemrelay.exception.ProblemRelayException
import com.gistgarden.gistgardenwebservice.util.problemrelay.exception.ProducedProblemRelayException
import com.gistgarden.gistgardenwebservice.util.problemrelay.exception.ReceivedProblemRelayException
import com.gistgarden.gistgardenwebservice.util.problemrelay.responsefactory.ProblemRelayHttpResponseFactory
import com.gistgarden.gistgardenwebservice.util.problemrelay.responsefactory.ProblemRelayResponseInstruction
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class RestResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {
    private val log by LoggerDelegate()

    @ExceptionHandler(ProducedProblemRelayException::class, ReceivedProblemRelayException::class)
    protected fun handleProblemRelayException(e: ProblemRelayException, request: WebRequest): ResponseEntity<ObjectNode> {
        val problemMarker = e.problem.marker
        log.trace(
            "handleProblemRelayException() caught exception while executing api request: " +
                    "${problemMarker.domain}: " +
                    "${problemMarker.mainType.readableName}(${problemMarker.mainType.id}) > " +
                    "${problemMarker.subType.readableName}(${problemMarker.subType.id}) ",
            e
        )

        val responseInstruction: ProblemRelayResponseInstruction = if (e is ProducedProblemRelayException) {
            ProblemRelayHttpResponseFactory.createV1(e.problem)
        } else {
            log.error("handleProducedProblemRelayException() caught ReceivedProblemRelayException while executing api request!", e)
            ProblemRelayHttpResponseFactory.createV1((e as ReceivedProblemRelayException).problem)
        }

        val headers = HttpHeaders()
        headers.add(responseInstruction.header.name, responseInstruction.header.value)
        return ResponseEntity(responseInstruction.body, headers, responseInstruction.statusCode)
    }
}