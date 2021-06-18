package ro.asis.order.service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient

@EnableEurekaClient
@SpringBootApplication(
    scanBasePackages = [
        "ro.asis.commons",
        "ro.asis.client.client",
        "ro.asis.account.client",
        "ro.asis.provider.client",
        "ro.asis.order"
    ]
)
class OrderServiceApplication

fun main(args: Array<String>) {
    runApplication<OrderServiceApplication>(*args)
}
