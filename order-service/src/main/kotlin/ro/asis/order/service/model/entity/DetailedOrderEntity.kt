package ro.asis.order.service.model.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "detailed_orders")
class DetailedOrderEntity(
    @Id
    var id: String? = ObjectId.get().toHexString(),

    var orderId: String,
    var clientId: String,
    var providerId: String,
    var clientName: String,
    var providerName: String
)
