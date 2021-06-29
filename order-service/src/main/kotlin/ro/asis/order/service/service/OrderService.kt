package ro.asis.order.service.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fge.jsonpatch.JsonPatch
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ro.asis.commons.exceptions.ResourceNotFoundException
import ro.asis.commons.filters.OrderFilters
import ro.asis.order.service.model.entity.DetailedOrderEntity
import ro.asis.order.service.model.entity.OrderEntity
import ro.asis.order.service.repository.OrderDao
import ro.asis.order.service.repository.OrderRepository
import ro.asis.order.service.service.validator.OrderValidator
import java.util.*

@Service
class OrderService(
    private val dao: OrderDao,
    private val mapper: ObjectMapper,
    private val validator: OrderValidator,
    private val repository: OrderRepository,
    private val detailedOrderService: DetailedOrderService,
    private val notificationsService: OrderNotificationsService
) {
    companion object {
        private val LOG = LoggerFactory.getLogger(OrderEntity::class.java)
    }

    fun findAllOrders(filters: OrderFilters): List<OrderEntity> = dao.findOrders(filters)

    fun findOrder(orderId: String): Optional<OrderEntity> = repository.findById(orderId)

    fun findDetailedOrder(orderId: String): DetailedOrderEntity {
        val order = getOrThrow(orderId)
        return detailedOrderService.findDetailedOrderByOrder(order)
    }

    fun addOrder(order: OrderEntity): OrderEntity {
        validator.validateNewOrThrow(order)
        val dbOrder = repository.save(order)
        notificationsService.notifyOrderCreated(dbOrder)
        return dbOrder
    }

    fun deleteOrder(orderId: String): Optional<OrderEntity> {
        validator.validateExistsOrThrow(orderId)
        val orderToDelete = repository.findById(orderId)
        orderToDelete.ifPresent { deleteExistingOrder(it) }
        return orderToDelete
    }

    fun patchOrder(orderId: String, patch: JsonPatch): OrderEntity {
        validator.validateExistsOrThrow(orderId)

        val dbOrder = getOrThrow(orderId)
        val patchedOrderJson = patch.apply(mapper.valueToTree(dbOrder))
        val patchedOrder = mapper.treeToValue(patchedOrderJson, OrderEntity::class.java)

        validator.validateReplaceOrThrow(orderId, patchedOrder)
        copyOrder(patchedOrder, dbOrder)
        notificationsService.notifyOrderEdited(dbOrder)

        return repository.save(dbOrder)
    }

    private fun copyOrder(newOrder: OrderEntity, dbOrder: OrderEntity) {
        LOG.info("Copying order: $newOrder")
        dbOrder.bags = newOrder.bags
        dbOrder.status = newOrder.status
        dbOrder.totalPrice = newOrder.totalPrice
    }

    private fun deleteExistingOrder(order: OrderEntity) {
        LOG.info("Deleting order: $order")
        notificationsService.notifyOrderDeleted(order)
        repository.delete(order)
    }

    private fun getOrThrow(orderId: String): OrderEntity = repository
        .findById(orderId)
        .orElseThrow { ResourceNotFoundException("Could not find order with id $orderId") }
}
