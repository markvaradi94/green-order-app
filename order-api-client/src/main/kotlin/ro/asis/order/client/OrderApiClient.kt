package ro.asis.order.client

import com.github.fge.jsonpatch.JsonPatch
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpEntity.EMPTY
import org.springframework.http.HttpMethod.*
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForObject
import org.springframework.web.util.UriComponentsBuilder
import ro.asis.commons.filters.OrderFilters
import ro.asis.commons.model.Order
import java.util.*
import java.util.Optional.ofNullable

@Component
class OrderApiClient(
    @Value("\${order-service-location:NOT_DEFINED}")
    private val baseUrl: String,
    private val restTemplate: RestTemplate
) {
    companion object {
        private val LOG = LoggerFactory.getLogger(Order::class.java)
    }

    fun getAllOrders(filters: OrderFilters): List<Order> {
        val url = buildQueriedUrl(filters)

        return restTemplate.exchange(
            url,
            GET,
            EMPTY,
            object : ParameterizedTypeReference<List<Order>>() {}
        ).body ?: listOf()
    }

    fun getOrder(orderId: String): Optional<Order> {
        val url = UriComponentsBuilder.fromHttpUrl(baseUrl)
            .path("/orders/$orderId")
            .toUriString()

        return ofNullable(restTemplate.getForObject(url, Order::class.java))
    }

    fun addOrder(order: Order): Order {
        val url = UriComponentsBuilder.fromHttpUrl(baseUrl)
            .path("/orders")
            .toUriString()

        return restTemplate.postForObject(url, order, Order::class)
    }

    fun patchOrder(orderId: String, patch: JsonPatch): Optional<Order> {
        val url = UriComponentsBuilder.fromHttpUrl(baseUrl)
            .path("/orders/$orderId")
            .toUriString()
        val patchedOrder = HttpEntity(patch)

        return ofNullable(
            restTemplate.exchange(
                url,
                PATCH,
                patchedOrder,
                Order::class.java
            ).body
        )
    }

    fun deleteOrder(orderId: String): Optional<Order> {
        val url = UriComponentsBuilder.fromHttpUrl(baseUrl)
            .path("/orders/$orderId")
            .toUriString()

        return ofNullable(
            restTemplate.exchange(
                url,
                DELETE,
                EMPTY,
                Order::class.java
            ).body
        )
    }

    private fun buildQueriedUrl(filters: OrderFilters): String {
        val builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
            .path("/orders/")

        ofNullable(filters.id)
            .ifPresent { builder.queryParam("id", it) }
        ofNullable(filters.providerId)
            .ifPresent { builder.queryParam("providerId", it) }
        ofNullable(filters.clientId)
            .ifPresent { builder.queryParam("clientId", it) }
        ofNullable(filters.status)
            .ifPresent { builder.queryParam("status", it) }

        return builder.toUriString()
    }
}
