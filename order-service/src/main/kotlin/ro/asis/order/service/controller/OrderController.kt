package ro.asis.order.service.controller

import com.github.fge.jsonpatch.JsonPatch
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import ro.asis.commons.exceptions.ResourceNotFoundException
import ro.asis.commons.filters.OrderFilters
import ro.asis.commons.model.Order
import ro.asis.order.dto.DetailedOrder
import ro.asis.order.service.model.entity.OrderEntity
import ro.asis.order.service.model.mappers.DetailedOrderMapper
import ro.asis.order.service.model.mappers.OrderMapper
import ro.asis.order.service.service.OrderService
import javax.validation.Valid

@RestController
@RequestMapping("orders")
class OrderController(
    private val service: OrderService,
    private val orderMapper: OrderMapper,
    private val detailedOrderMapper: DetailedOrderMapper
) {
    companion object {
        private val LOG = LoggerFactory.getLogger(OrderEntity::class.java)
    }

    @GetMapping
    fun getAllOrders(filters: OrderFilters): List<Order> = orderMapper.toApi(service.findAllOrders(filters))

    @GetMapping("{orderId}")
    fun getOrder(@PathVariable orderId: String): Order = service.findOrder(orderId)
        .map { orderMapper.toApi(it) }
        .orElseThrow { ResourceNotFoundException("Could not find order with id $orderId") }

    @GetMapping("{orderId}/detailed")
    fun getDetailedOrder(@PathVariable orderId: String): DetailedOrder =
        detailedOrderMapper.toApi(service.findDetailedOrder(orderId))

    @PostMapping
    fun addOrder(@Valid @RequestBody order: Order): Order =
        orderMapper.toApi(service.addOrder(orderMapper.toEntity(order)))
    //TODO add event so detailed order is created when a new order is placed

    @PatchMapping("{orderId}")
    fun patchOrder(@PathVariable orderId: String, @RequestBody patch: JsonPatch): Order =
        orderMapper.toApi(service.patchOrder(orderId, patch))

    @DeleteMapping("{orderId}")
    fun deleteOrder(@PathVariable orderId: String): Order = service.deleteOrder(orderId)
        .map { orderMapper.toApi(it) }
        .orElseGet { null }
}
