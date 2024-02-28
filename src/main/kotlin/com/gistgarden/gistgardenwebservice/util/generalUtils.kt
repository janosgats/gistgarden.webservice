package com.gistgarden.gistgardenwebservice.util

import com.gistgarden.gistgardenwebservice.util.problemrelay.exception.ProducedProblemRelayException
import com.gistgarden.gistgardenwebservice.util.problemrelay.model.ProblemMarker


fun assertWith(markerToThrowIfConditionIsFalse: ProblemMarker, conditionSupplier: () -> Boolean) {
    if (!conditionSupplier()) {
        throw ProducedProblemRelayException(markerToThrowIfConditionIsFalse)
    }
}