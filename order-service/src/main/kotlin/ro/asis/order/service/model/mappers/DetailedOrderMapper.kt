package ro.asis.order.service.model.mappers

import org.springframework.stereotype.Component
import ro.asis.commons.utils.ModelMapper
import ro.asis.order.dto.DetailedOrder
import ro.asis.order.service.model.entity.DetailedOrderEntity
import ro.asis.order.service.service.DetailedOrderService

@Component
class DetailedOrderMapper(
    private val service: DetailedOrderService
) : ModelMapper<DetailedOrder, DetailedOrderEntity> {
    override fun toApi(source: DetailedOrderEntity): DetailedOrder {
        val order = service.findOrderForDetailedOrder(source)
        val client = service.findClientForDetailedOrder(source)
        val provider = service.findProviderForDetailedOrder(source)
        return DetailedOrder(
            id = source.id,
            orderId = source.orderId,
            clientId = source.clientId,
            providerId = source.providerId,
            bags = order.bags,
            status = order.status,
            totalPrice = order.totalPrice,
            clientName = "${client.firstName} ${client.lastName}",
            providerName = provider.name
        )
    }

    override fun toEntity(source: DetailedOrder): DetailedOrderEntity {
        return DetailedOrderEntity(
            id = source.id,
            orderId = source.orderId,
            clientId = source.clientId,
            providerId = source.providerId,
            clientName = source.clientName,
            providerName = source.providerName
        )
    }
}
