package com.gistgarden.gistgardenwebservice.util.problemrelay.exception

import com.gistgarden.gistgardenwebservice.util.problemrelay.model.*

open class ProblemRelayException(
    open val problem: ProblemRelayContainer,
    cause: Throwable? = null
) : RuntimeException(problem.message, cause)


open class ProducedProblemRelayException(
    override val problem: ProducedProblemRelayContainer,

    cause: Throwable? = null,
) : ProblemRelayException(problem, cause) {
    @JvmOverloads
    constructor(
        marker: ProblemMarker,
        message: String? = null,
        payload: Any? = null,
        suggestedHttpResponseCode: Int? = null,
        cause: Throwable? = null,
    ) : this(ProducedProblemRelayContainerImpl(marker, message, payload, suggestedHttpResponseCode), cause)
}


class ReceivedProblemRelayException(
    override val problem: ReceivedProblemRelayContainer,

    cause: Throwable? = null,
) : ProblemRelayException(problem, cause)