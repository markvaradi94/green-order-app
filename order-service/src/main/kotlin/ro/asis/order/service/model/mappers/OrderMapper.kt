package ro.asis.order.service.model.mappers

import org.springframework.stereotype.Component
import ro.asis.commons.model.Order
import ro.asis.commons.utils.ModelMapper
import ro.asis.order.service.model.entity.OrderEntity

@Component
class OrderMapper : ModelMapper<Order, OrderEntity> {
    override fun toApi(source: OrderEntity): Order {
        return Order(
            id = source.id,
            clientId = source.clientId,
            providerId = source.providerId,
            status = source.status,
            bags = source.bags,
            totalPrice = source.totalPrice
        )
    }

    override fun toEntity(source: Order): OrderEntity {
        return OrderEntity(
            id = source.id,
            clientId = source.clientId,
            providerId = source.providerId,
            status = source.status,
            bags = source.bags,
            totalPrice = source.totalPrice
        )
    }
}
