package io.pointpulse.pointpulsewebservice.util.problemrelay.model

import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import io.pointpulse.pointpulsewebservice.util.problemrelay.responseparser.getOptionalRealString
import io.pointpulse.pointpulsewebservice.util.problemrelay.responseparser.getRequiredRealInt
import io.pointpulse.pointpulsewebservice.util.problemrelay.responseparser.getRequiredRealString

interface ProblemMarker {
    val domain: String
    val mainType: ProblemType
    val subType: ProblemType

    fun toJsonBody(): ObjectNode {
        return ObjectNode(JsonNodeFactory.instance).apply {
            put("domain", domain)

            put("mainTypeId", mainType.id)
            mainType.readableName?.let { put("mainTypeName", it) }

            put("subTypeId", subType.id)
            subType.readableName?.let { put("subTypeName", it) }
        }
    }

    fun matches(marker: ProblemMarker): Boolean {
        return ProblemMarkerUtils.areMarkersEqual(this, marker)
    }

    fun matches(container: ProblemRelayContainer): Boolean {
        return matches(container.marker)
    }
}

open class ProblemMarkerImpl(
    override val domain: String,
    override val mainType: ProblemType,
    override val subType: ProblemType,
) : ProblemMarker {
    constructor(domain: String, mainTypeId: Int, subTypeId: Int) : this(domain, ProblemTypeImpl(mainTypeId, null), ProblemTypeImpl(subTypeId, null))
}

object ProblemMarkerUtils {
    fun areMarkersEqual(a: ProblemMarker, b: ProblemMarker): Boolean {
        return a.domain == b.domain && a.mainType.id == b.mainType.id && a.subType.id == b.subType.id
    }

    fun parseFromJson(json: ObjectNode): ProblemMarkerImpl {
        val PARSING_LOG_CONTEXT = "parsing ProblemMarker"

        return ProblemMarkerImpl(
            domain = json.getRequiredRealString("domain", PARSING_LOG_CONTEXT),
            mainType = ProblemTypeImpl(json.getRequiredRealInt("mainTypeId", PARSING_LOG_CONTEXT), json.getOptionalRealString("mainTypeName")),
            subType = ProblemTypeImpl(json.getRequiredRealInt("subTypeId", PARSING_LOG_CONTEXT), json.getOptionalRealString("subTypeName")),
        )
    }
}