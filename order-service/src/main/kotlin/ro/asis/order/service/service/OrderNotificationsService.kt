package ro.asis.order.service.service

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service
import ro.asis.order.service.model.mappers.OrderMapper

@Service
class OrderNotificationsService(
    private val mapper: OrderMapper,
    private val rabbitTemplate: RabbitTemplate
) {
}
