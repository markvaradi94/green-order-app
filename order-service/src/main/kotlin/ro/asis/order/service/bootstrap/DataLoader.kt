package ro.asis.order.service.bootstrap

import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import ro.asis.commons.enums.OrderStatus
import ro.asis.commons.enums.OrderStatus.PLACED
import ro.asis.commons.model.GreenBag
import ro.asis.order.service.model.entity.OrderEntity
import ro.asis.order.service.service.OrderService

@Component
class DataLoader(private val service: OrderService) : CommandLineRunner {
    override fun run(vararg args: String?) {
//        service.addOrder(
//            OrderEntity(
//                clientId = "60c9ad6cd43d704b65ad814a",
//                providerId = "60cb73b151e33b6549ea6039",
//                status = PLACED,
//                bags = listOf(
//                    GreenBag(
//                        description = "Reheated pizza slices",
//                        price = 10.5,
//                        quantity = 2,
//                    )
//                ),
//                totalPrice = 21.0
//            )
//        )
    }
}
