package nl.sourcelabs.graphql

import java.util.concurrent.atomic.AtomicInteger

data class Order(val id: Int? = null, val totalPrice: String)

object OrderRepository {

    private val orderMap = mutableMapOf(123 to Order(123, "19.99"))
    private val orderIdGenerator = AtomicInteger(200)

    fun getOrderById(id: Int) = orderMap[id]

    fun addOrder(newOrder: Order): Order {
        val copy = newOrder.copy(id = orderIdGenerator.incrementAndGet())
        orderMap.put(copy.id!!, copy)
        return copy
    }
}