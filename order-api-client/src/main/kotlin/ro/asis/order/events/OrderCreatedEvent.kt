package ro.asis.order.events

import com.fasterxml.jackson.annotation.JsonProperty
import ro.asis.commons.enums.EventType
import ro.asis.commons.enums.EventType.CREATE

data class OrderCreatedEvent(
    @JsonProperty("orderId")
    val orderId: String,

    @JsonProperty("clientId")
    val clientId: String,

    @JsonProperty("providerId")
    val providerId: String,

    @JsonProperty("eventType")
    val eventType: EventType = CREATE
)
