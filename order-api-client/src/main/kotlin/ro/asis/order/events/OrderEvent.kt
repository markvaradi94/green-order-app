package ro.asis.order.events

import ro.asis.commons.enums.EventType
import ro.asis.commons.model.Order

data class OrderEvent(
    private val order: Order,
    private val type: EventType
)
