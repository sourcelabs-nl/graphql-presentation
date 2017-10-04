package nl.sourcelabs.graphql

data class Order(val totalPrice: String)

object OrderRepository {
    fun getOrderById(id: Int) = when (id) {
        123 -> Order("19.99")
        else -> null
    }
}