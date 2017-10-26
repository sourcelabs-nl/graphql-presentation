package nl.sourcelabs.graphql

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.coxautodev.graphql.tools.GraphQLResolver
import graphql.schema.DataFetchingEnvironment
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

// graphql-spring-boot-starter example
@SpringBootApplication
open class SpringBootExample {

    // Resolves the fields of the Query type
    @Bean
    open fun queryResolver() = object : GraphQLQueryResolver {
        fun orders() = OrderRepository.getOrders()

        fun order(id: Long, env: DataFetchingEnvironment): Order? {
            val order = OrderRepository.getOrderById(id)
            env.arguments.put("order", order) // we pass the order to the downstream resolvers (shipment) so we can use it to fetch order related data
            return order
        }
    }

    // Resolves the field of the OrderItem type
    @Bean
    open fun orderItemResolver() = object : GraphQLResolver<OrderItem> {
        // resolves the product information for an orderItem
        fun product(orderItem: OrderItem, env: DataFetchingEnvironment): Product? {
            return ProductRepository.getProductById(orderItem.productId)
        }

        // resolves the shipment information for an orderItem
        fun shipment(orderItem: OrderItem, env: DataFetchingEnvironment): Shipment? {
            return ShipmentRepository.getShipmentByOrderId(env.getArgument<Order>("order").id)
        }
    }

    // Handles the mutations on the Mutation type
    @Bean
    open fun mutationResolver() = object : GraphQLMutationResolver {
        fun createOrder(orderInput: OrderInput): Order {
            val newOrder = Order(totalPrice = orderInput.orderItems.totalPrice, orderItems = orderInput.orderItems.map { it.toOrderItem() })
            return OrderRepository.addOrder(newOrder)
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(SpringBootExample::class.java, *args)
        }
    }
}