package ro.asis.order.service.repository

import org.springframework.data.mongodb.repository.MongoRepository
import ro.asis.order.service.model.entity.OrderEntity
import java.util.*

interface OrderRepository : MongoRepository<OrderEntity, String> {
    fun existsByClientId(clientId: String): Boolean
    fun existsByProviderId(providerId: String): Boolean
    fun existsByClientIdAndProviderId(clientId: String, providerId: String): Boolean
    fun findByClientId(clientId: String): Optional<OrderEntity>
    fun findByProviderId(providerId: String): Optional<OrderEntity>
    fun findByClientIdAndProviderId(clientId: String, providerId: String): Optional<OrderEntity>
}
