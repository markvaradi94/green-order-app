package ro.asis.order.service.repository

import org.springframework.data.mongodb.repository.MongoRepository
import ro.asis.order.service.model.entity.DetailedOrderEntity
import java.util.*

interface DetailedOrderRepository : MongoRepository<DetailedOrderEntity, String> {
    fun findByClientId(clientId: String): Optional<DetailedOrderEntity>
    fun findByProviderId(providerId: String): Optional<DetailedOrderEntity>
    fun findByOrderId(orderId: String): Optional<DetailedOrderEntity>
    fun findByOrderIdAndClientIdAndProviderId(
        clientId: String,
        orderId: String,
        providerId: String
    ): Optional<DetailedOrderEntity>
}
