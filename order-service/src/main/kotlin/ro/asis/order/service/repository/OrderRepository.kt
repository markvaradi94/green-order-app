package ro.asis.order.service.repository

import org.springframework.data.mongodb.repository.MongoRepository
import ro.asis.order.service.model.entity.OrderEntity

interface OrderRepository : MongoRepository<OrderEntity, String>
