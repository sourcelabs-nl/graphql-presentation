package nl.sourcelabs.graphql

import java.util.concurrent.atomic.AtomicLong

data class Product(val imageUrl: String)
data class Shipment(val status: String)
data class OrderItem(val shipment: Shipment)
data class Order(val id: Long? = null, val totalPrice: String, val orderItems: List<OrderItem>? = null)

data class OrderInput(val totalPrice: String, val orderItems: List<OrderItemInput>)
data class OrderItemInput(val productId: Long)

object OrderRepository {

    private val orderMap = mutableMapOf(4370307900L to Order(4370307900L, "23.93", listOf(
            OrderItem(Shipment("verzonden")),
            OrderItem(Shipment("verzonden"))
    )))
    private val orderIdGenerator = AtomicLong(200)

    fun getOrderById(id: Long) = orderMap[id]

    fun addOrder(newOrder: Order): Order {
        val copy = newOrder.copy(id = orderIdGenerator.incrementAndGet())
        orderMap.put(copy.id!!, copy)
        return copy
    }
}