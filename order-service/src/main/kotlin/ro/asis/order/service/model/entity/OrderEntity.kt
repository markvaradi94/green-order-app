package ro.asis.order.service.model.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import ro.asis.commons.enums.OrderStatus
import ro.asis.commons.model.GreenBag

@Document(collection = "orders")
class OrderEntity(
    @Id
    var id: String = ObjectId.get().toHexString(),

    var clientId: String,
    var providerId: String,
    var status: OrderStatus,
    var bags: List<GreenBag> = listOf(),
    var totalPrice: Double
)
