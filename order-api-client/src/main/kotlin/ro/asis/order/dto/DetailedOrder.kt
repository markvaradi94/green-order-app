package ro.asis.order.dto

import com.fasterxml.jackson.annotation.JsonProperty
import org.bson.types.ObjectId
import ro.asis.commons.enums.OrderStatus
import ro.asis.commons.model.GreenBag

data class DetailedOrder(
    @JsonProperty("id")
    var id: String = ObjectId.get().toHexString(),

    @JsonProperty("orderId")
    var orderId: String,

    @JsonProperty("clientId")
    var clientId: String,

    @JsonProperty("providerId")
    var providerId: String,

    @JsonProperty("bags")
    var bags: List<GreenBag>,

    @JsonProperty("status")
    var status: OrderStatus,

    @JsonProperty("totalPrice")
    var totalPrice: Double,

    @JsonProperty("clientName")
    var clientName: String,

    @JsonProperty("providerName")
    var providerName: String
)
