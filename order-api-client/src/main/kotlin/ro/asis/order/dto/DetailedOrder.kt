package ro.asis.order.dto

import ro.asis.commons.enums.OrderStatus
import ro.asis.commons.model.GreenBag

data class DetailedOrder(
    var id: String?,
    var orderId: String,
    var clientId: String,
    var providerId: String,
    var bags: List<GreenBag>,
    var status: OrderStatus,
    var totalPrice: Double,
    var clientName: String,
    var providerName: String
)
