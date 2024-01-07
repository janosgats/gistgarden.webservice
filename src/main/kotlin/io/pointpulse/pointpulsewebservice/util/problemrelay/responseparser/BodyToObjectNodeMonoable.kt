package io.pointpulse.pointpulsewebservice.util.problemrelay.responseparser

import com.fasterxml.jackson.databind.node.ObjectNode
import org.springframework.web.reactive.function.client.ClientResponse
import reactor.core.publisher.Mono

interface BodyToObjectNodeMonoable {
    fun toObjectNodeMono(): Mono<ObjectNode>
}

class ClientResponseBodyToObjectNodeMonoableAdapter(
    val clientResponse: ClientResponse
) : BodyToObjectNodeMonoable {
    override fun toObjectNodeMono(): Mono<ObjectNode> {
        return clientResponse.bodyToMono(ObjectNode::class.java)
    }
}