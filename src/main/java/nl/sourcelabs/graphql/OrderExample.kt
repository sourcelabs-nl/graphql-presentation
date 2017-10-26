package nl.sourcelabs.graphql

import java.math.BigDecimal
import java.util.concurrent.atomic.AtomicLong

/**
 * Return types
 */
data class Product(var id: String? = null, val title: String, val price: BigDecimal, val imageUrl: String)

data class Shipment(val status: String)
data class OrderItem(var id: Long? = null, val quantity: Int, var shipment: Shipment? = null, var product: Product? = null, val productId: String)
data class Order(val id: Long? = null, val totalPrice: BigDecimal, val orderItems: List<OrderItem>)

/**
 * Input types
 */
data class OrderInput(val orderItems: List<OrderItemInput>)

data class OrderItemInput(val quantity: Int, val productId: String) {
    fun toOrderItem() = OrderItem(quantity = this.quantity, productId = this.productId)
}

/**
 * Type aliases to make the maps with dummy data a bit more descriptive
 */
typealias OrderId = Long
typealias OrderItemId = Long
typealias ProductId = String

/**
 * Dummy Repository implementations to simulate an external service or persistent storage
 */
object OrderRepository {
    private val orderIdGenerator = AtomicLong(0)
    private val orderItemIdGenerator = AtomicLong(0)
    private val dummyData: MutableMap<OrderId, Order> = mutableMapOf(4370307900L to Order(id = 4370307900L, totalPrice = 5899.49.bd, orderItems = listOf(OrderItem(id = 234, quantity = 1, productId = "123"), OrderItem(id = 235, quantity = 1, productId = "234"))))

    fun getOrderById(id: OrderId) = dummyData[id]
    fun addOrder(newOrder: Order): Order = with(newOrder.copy(id = orderIdGenerator.incrementAndGet(), orderItems = newOrder.orderItems.map { it.copy(id = orderItemIdGenerator.incrementAndGet()) })) { dummyData.put(this.id!!, this); this }
    fun getOrders(): List<Order> = dummyData.values.toList()
}

object ProductRepository {
    private val dummyData = mutableMapOf<ProductId, Product>("123" to Product(title = "MacBook Pro", imageUrl = "http://image.url/123.png", price = 2999.99.bd), "234" to Product(title = "Dell XPS 15", imageUrl = "http://image.url/234.png", price = 2899.50.bd))
    fun getProductById(id: String) = dummyData[id]
}

object ShipmentRepository {
    private val dummyData = mutableMapOf<OrderItemId, Shipment>(234L to Shipment("Delivered"), 235L to Shipment("Shipped"))
    fun getShipmentByOrderItemId(orderItemId: Long): Shipment? = dummyData[orderItemId]
}

/**
 * Extension properties to make conversions a bit easier
 */
val Double.bd: BigDecimal
    get() = BigDecimal(this)

val List<OrderItemInput>.totalPrice: BigDecimal
    get() = this.sumByDouble { ProductRepository.getProductById(it.productId)?.price?.toDouble() ?: 0.0 * it.quantity }.bd
