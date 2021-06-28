package ro.asis.order.service.service.validator

import org.springframework.stereotype.Component
import ro.asis.client.client.ClientApiClient
import ro.asis.commons.exceptions.ValidationException
import ro.asis.order.service.model.entity.OrderEntity
import ro.asis.order.service.repository.OrderRepository
import ro.asis.provider.client.ProviderApiClient
import java.util.*
import java.util.Optional.empty
import java.util.Optional.of

@Component
class OrderValidator(
    private val repository: OrderRepository,
    private val clientApiClient: ClientApiClient,
    private val providerApiClient: ProviderApiClient
) {

    fun validateReplaceOrThrow(orderId: String, newOrder: OrderEntity) =
        exists(orderId)
            .or { validate(newOrder) }
            .ifPresent { throw it }

    fun validateNewOrThrow(order: OrderEntity) = validate(order).ifPresent { throw it }

    fun validateExistsOrThrow(orderId: String) = exists(orderId).ifPresent { throw it }

    private fun validate(order: OrderEntity): Optional<ValidationException> {
        orderNotEmpty(order).ifPresent { throw it }
        clientDoesNotExistOrIsInvalid(order.clientId).ifPresent { throw it }
        providerDoesNotExistOrIsInvalid(order.providerId).ifPresent { throw it }
        return empty()
    }

    //TODO add provider inventory verification for when the order is placed
    private fun orderNotEmpty(order: OrderEntity): Optional<ValidationException> {
        return if (order.bags.isEmpty()) of(ValidationException("Cannot place an empty order."))
        else empty()
    }

    private fun providerDoesNotExistOrIsInvalid(providerId: String): Optional<ValidationException> {
        return if (!providerExists(providerId)) of(ValidationException("Cannot find provider with id $providerId"))
        else if (providerId.isBlank()) of(ValidationException("Provider id cannot be blank"))
        else empty()
    }

    private fun clientDoesNotExistOrIsInvalid(clientId: String): Optional<ValidationException> {
        return if (!clientExists(clientId)) of(ValidationException("Cannot find client with id $clientId"))
        else if (clientId.isBlank()) of(ValidationException("Client id cannot be blank"))
        else empty()
    }

    private fun providerExists(providerId: String): Boolean = providerApiClient.getProvider(providerId).isPresent

    private fun clientExists(clientId: String): Boolean = clientApiClient.getClient(clientId).isPresent

    private fun exists(orderId: String): Optional<ValidationException> {
        return if (repository.existsById(orderId)) empty()
        else of(ValidationException("Order with id $orderId doesn't exist"))
    }
}
