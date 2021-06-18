package ro.asis.order.service.repository

import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import ro.asis.commons.enums.OrderStatus.valueOf
import ro.asis.commons.filters.OrderFilters
import ro.asis.order.service.model.entity.OrderEntity
import java.util.*

@Repository
class OrderDao(private val mongo: MongoTemplate) {
    fun findOrders(filters: OrderFilters): List<OrderEntity> {
        val query = Query()
        val criteria = buildCriteria(filters)

        if (criteria.isNotEmpty()) query.addCriteria(Criteria().andOperator(*criteria.toTypedArray()))

        return mongo.find(query, OrderEntity::class.java).toList()
    }

    private fun buildCriteria(filters: OrderFilters): List<Criteria> {
        val criteria = mutableListOf<Criteria>()

        Optional.ofNullable(filters.id)
            .ifPresent { criteria.add(Criteria.where("id").`is`(it)) }
        Optional.ofNullable(filters.clientId)
            .ifPresent { criteria.add(Criteria.where("clientId").`is`(it)) }
        Optional.ofNullable(filters.providerId)
            .ifPresent { criteria.add(Criteria.where("providerId").`is`(it)) }
        Optional.ofNullable(filters.status)
            .ifPresent { criteria.add(Criteria.where("status").`is`(valueOf(it.uppercase()).name)) }

        return criteria
    }
}
