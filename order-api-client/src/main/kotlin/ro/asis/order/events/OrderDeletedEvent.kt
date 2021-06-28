package ro.asis.order.events

import com.fasterxml.jackson.annotation.JsonProperty
import ro.asis.commons.enums.EventType
import ro.asis.commons.enums.EventType.DELETE

data class OrderDeletedEvent(
    @JsonProperty("orderId")
    val orderId: String,

    @JsonProperty("eventType")
    val eventType: EventType = DELETE
)
