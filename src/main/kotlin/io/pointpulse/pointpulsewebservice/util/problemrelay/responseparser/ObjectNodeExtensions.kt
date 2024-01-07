package io.pointpulse.pointpulsewebservice.util.problemrelay.responseparser

import com.fasterxml.jackson.databind.node.ObjectNode

fun ObjectNode.getOptionalRealString(fieldName: String): String? {
    if (this.has(fieldName)) {
        return this.get(fieldName).asText()
    }
    return null
}


fun ObjectNode.getOptionalRealBoolean(fieldName: String): Boolean? {
    if (this.has(fieldName) && this.get(fieldName).isBoolean) {
        return this.get(fieldName).asBoolean()
    }
    return null
}

fun ObjectNode.getRequiredRealObject(fieldName: String, logContext: String): ObjectNode {
    if (!this.has(fieldName)) {
        throw RuntimeException("Missing field ($fieldName) while: $logContext!")
    }
    if (!this.get(fieldName).isObject) {
        throw RuntimeException("Expected field ($fieldName) to be an object while: $logContext!")
    }
    return this.get(fieldName) as ObjectNode
}

fun ObjectNode.getRequiredRealString(fieldName: String, logContext: String): String {
    if (!this.has(fieldName)) {
        throw RuntimeException("Missing field ($fieldName) while: $logContext!")
    }
    return this.get(fieldName).asText()
}

fun ObjectNode.getRequiredRealInt(fieldName: String, logContext: String): Int {
    if (!this.has(fieldName)) {
        throw RuntimeException("Missing field ($fieldName) while: $logContext!")
    }
    if (!this.get(fieldName).isNumber) {
        throw RuntimeException("Expected field ($fieldName) to be a number while: $logContext!")
    }
    return this.get(fieldName).asInt()
}