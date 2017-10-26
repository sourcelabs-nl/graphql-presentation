package nl.sourcelabs.graphql

import java.math.BigDecimal
import java.util.concurrent.atomic.AtomicLong

data class Product(var id: String? = null, val title: String, val price: BigDecimal, val imageUrl: String)
data class Shipment(val status: String)
data class OrderItem(val quantity: Int, var shipment: Shipment? = null, var product: Product? = null, val productId: String)
data class Order(val id: Long? = null, val totalPrice: BigDecimal, val orderItems: List<OrderItem>? = null)

data class OrderInput(val orderItems: List<OrderItemInput>)
data class OrderItemInput(val quantity: Int, val productId: String) {
    fun toOrderItem() = OrderItem(quantity = this.quantity, productId = this.productId)
}

typealias OrderId = Long
typealias ProductId = String

object OrderRepository {
    private val orderIdGenerator = AtomicLong(0)
    private val dummyData: MutableMap<OrderId, Order> = mutableMapOf(
            4370307900L to Order(id = 4370307900L, totalPrice = 5899.49.bd, orderItems = listOf(OrderItem(quantity = 1, productId = "123"), OrderItem(quantity = 1, productId = "234")))
    )

    fun getOrderById(id: OrderId) = dummyData[id]
    fun addOrder(newOrder: Order): Order = with(newOrder.copy(id = orderIdGenerator.incrementAndGet())) { dummyData.put(this.id!!, this); this }
    fun getOrders(): List<Order> = dummyData.values.toList()
}

object ProductRepository {
    private val dummyData = mutableMapOf<ProductId, Product>("123" to Product(title = "MacBook Pro", imageUrl = "http://image.url/123.png", price = 2999.99.bd), "234" to Product(title = "Dell XPS 15", imageUrl = "http://image.url/234.png", price = 2899.50.bd))
    fun getProductById(id: String) = dummyData[id]
}

object ShipmentRepository {
    private val dummyData = mutableMapOf<OrderId, Shipment>(4370307900L to Shipment("Shipped"))
    fun getShipmentByOrderId(id: Long?): Shipment? = dummyData[id]
}

val Double.bd: BigDecimal
    get() = BigDecimal(this)

val List<OrderItemInput>.totalPrice: BigDecimal
    get() {
        val list = this
        return list.sumByDouble { ProductRepository.getProductById(it.productId)?.price?.toDouble() ?: 0.0 * it.quantity }.bd
    }