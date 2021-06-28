package ro.asis.order.service.service

import org.springframework.stereotype.Service
import ro.asis.client.client.ClientApiClient
import ro.asis.client.dto.Client
import ro.asis.commons.exceptions.ResourceNotFoundException
import ro.asis.commons.model.Order
import ro.asis.order.client.OrderApiClient
import ro.asis.order.service.model.entity.DetailedOrderEntity
import ro.asis.order.service.model.entity.OrderEntity
import ro.asis.order.service.repository.DetailedOrderRepository
import ro.asis.provider.client.ProviderApiClient
import ro.asis.provider.dto.Provider

@Service
class DetailedOrderService(
    private val orderApiClient: OrderApiClient,
    private val clientApiClient: ClientApiClient,
    private val providerApiClient: ProviderApiClient,
    private val repository: DetailedOrderRepository
) {
    fun createDetailedOrder(order: OrderEntity): DetailedOrderEntity {
        val client = clientApiClient.getClient(order.clientId)
            .orElseThrow { ResourceNotFoundException("Could not find client with id ${order.clientId}") }
        val provider = providerApiClient.getProvider(order.providerId)
            .orElseThrow { ResourceNotFoundException("Could not find provider with id ${order.providerId}") }
        return repository.save(
            DetailedOrderEntity(
                orderId = order.id,
                clientId = client.id,
                providerId = provider.id,
                clientName = "${client.firstName} ${client.lastName}",
                providerName = provider.name
            )
        )
    }

    fun findDetailedOrderByOrderId(orderId: String): DetailedOrderEntity =
        repository.findByOrderId(orderId)
            .orElseThrow { ResourceNotFoundException("Could not find detailed order for order with id $orderId") }

    fun findDetailedOrderByClientId(clientId: String): DetailedOrderEntity =
        repository.findByClientId(clientId)
            .orElseThrow { ResourceNotFoundException("Could not find detailed order for client with id $clientId") }

    fun findDetailedOrderByProviderId(providerId: String): DetailedOrderEntity =
        repository.findByProviderId(providerId)
            .orElseThrow { ResourceNotFoundException("Could not find detailed order for provider with id $providerId") }

    fun findDetailedOrderByOrder(order: OrderEntity): DetailedOrderEntity = findDetailedOrderByOrderId(order.id)

    fun findOrderForDetailedOrder(detailedOrder: DetailedOrderEntity): Order =
        orderApiClient.getOrder(detailedOrder.orderId)
            .orElseThrow { ResourceNotFoundException("Could not find order with id ${detailedOrder.orderId}") }

    fun findClientForDetailedOrder(detailedOrder: DetailedOrderEntity): Client =
        clientApiClient.getClient(detailedOrder.clientId)
            .orElseThrow { ResourceNotFoundException("Could not find client with id ${detailedOrder.clientId}") }

    fun findProviderForDetailedOrder(detailedOrder: DetailedOrderEntity): Provider =
        providerApiClient.getProvider(detailedOrder.providerId)
            .orElseThrow { ResourceNotFoundException("Could not find provider with id ${detailedOrder.providerId}") }
}
