package ro.asis.order.events

import com.fasterxml.jackson.annotation.JsonProperty
import ro.asis.commons.enums.EventType
import ro.asis.commons.enums.EventType.MODIFY
import ro.asis.commons.model.Order

data class OrderModifiedEvent(
    @JsonProperty("orderId")
    val orderId: String,

    @JsonProperty("editedOrder")
    val editedOrder: Order,

    @JsonProperty("eventType")
    val eventType: EventType = MODIFY
)
