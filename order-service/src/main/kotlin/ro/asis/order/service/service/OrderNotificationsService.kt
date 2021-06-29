package ro.asis.order.service.service

import org.slf4j.LoggerFactory
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service
import ro.asis.order.events.OrderCreatedEvent
import ro.asis.order.events.OrderDeletedEvent
import ro.asis.order.events.OrderModifiedEvent
import ro.asis.order.service.model.entity.OrderEntity
import ro.asis.order.service.model.mappers.OrderMapper

@Service
class OrderNotificationsService(
    private val mapper: OrderMapper,
    private val rabbitTemplate: RabbitTemplate,
    private val orderExchange: TopicExchange
) {
    companion object {
        private val LOG = LoggerFactory.getLogger(OrderEntity::class.java)
    }

    fun notifyOrderCreated(order: OrderEntity) {
        val event = OrderCreatedEvent(
            orderId = order.id,
            clientId = order.clientId,
            providerId = order.providerId
        )

        LOG.info("Sending event $event")
        rabbitTemplate.convertAndSend(orderExchange.name, "green.orders.new", event)
    }

    fun notifyOrderDeleted(order: OrderEntity) {
        val event = OrderDeletedEvent(orderId = order.id)

        LOG.info("Sending event $event")
        rabbitTemplate.convertAndSend(orderExchange.name, "green.orders.delete", event)
    }

    fun notifyOrderEdited(order: OrderEntity) {
        val event = OrderModifiedEvent(
            orderId = order.id,
            editedOrder = mapper.toApi(order)
        )

        LOG.info("Sending event $event")
        rabbitTemplate.convertAndSend(orderExchange.name, "green.orders.edit", event)
    }
}
