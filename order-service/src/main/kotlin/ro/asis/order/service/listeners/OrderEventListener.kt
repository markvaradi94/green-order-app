package ro.asis.order.service.listeners

import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import ro.asis.commons.enums.EventType.*
import ro.asis.order.events.OrderCreatedEvent
import ro.asis.order.events.OrderDeletedEvent
import ro.asis.order.events.OrderModifiedEvent
import ro.asis.order.service.model.entity.OrderEntity
import ro.asis.order.service.service.DetailedOrderService

@Component
class OrderEventListener(
    private val detailedOrderService: DetailedOrderService
) {
    companion object {
        private val LOG = LoggerFactory.getLogger(OrderEntity::class.java)
    }

    @RabbitListener(queues = ["#{newOrderQueue.name}"])
    fun processOrderCreated(event: OrderCreatedEvent) = callCreateDetailedOrder(event)

    @RabbitListener(queues = ["#{deleteOrderQueue.name}"])
    fun processOrderDeleted(event: OrderDeletedEvent) = callDeleteDetailedOrder(event)

    @RabbitListener(queues = ["#{editOrderQueue.name}"])
    fun processOrderEdited(event: OrderModifiedEvent) = callEditDetailedOrder(event)

    private fun callCreateDetailedOrder(event: OrderCreatedEvent) {
        if (event.eventType == CREATE) {
            LOG.info("Creating detailed order for order with id ${event.orderId}")
            detailedOrderService.createDetailedOrderForNewOrder(
                orderId = event.orderId,
                clientId = event.clientId,
                providerId = event.providerId
            )
        }
    }

    private fun callDeleteDetailedOrder(event: OrderDeletedEvent) {
        if (event.eventType == DELETE) {
            LOG.info("Deleting detailed order for order with id ${event.orderId}")
            detailedOrderService.deleteForOrder(event.orderId)
        }
    }

    private fun callEditDetailedOrder(event: OrderModifiedEvent) {
        if (event.eventType == MODIFY) {
            LOG.info("Detailed order was edited for order with id ${event.orderId}")
            LOG.info("$event")

            detailedOrderService.editForOrderChange(
                orderId = event.orderId,
                editedOrder = event.editedOrder
            )
        }
    }
}
