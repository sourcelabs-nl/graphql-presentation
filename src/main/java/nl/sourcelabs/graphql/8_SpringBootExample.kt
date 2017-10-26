package nl.sourcelabs.graphql

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.coxautodev.graphql.tools.GraphQLResolver
import graphql.schema.DataFetchingEnvironment
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

/**
 * This is an example application demonstrating a schema-first approach using graphql-java-tools in a Spring boot application.
 * The graphql-spring-boot-starter takes care of configuring the graphql-servlet and the type/schema configuration for graphql-java-tools.
 */
@SpringBootApplication
open class SpringBootExample {

    // Resolves the fields of the Query type
    @Bean
    open fun queryResolver() = object : GraphQLQueryResolver {
        fun orders() = OrderRepository.getOrders()
        fun order(id: Long, env: DataFetchingEnvironment) = OrderRepository.getOrderById(id)
    }

    // Resolves the fields of the OrderItem type
    @Bean
    open fun orderItemResolver() = object : GraphQLResolver<OrderItem> {
        // resolves the product information for an orderItem
        fun product(orderItem: OrderItem, env: DataFetchingEnvironment) = ProductRepository.getProductById(orderItem.productId)

        // resolves the shipment information for an orderItem
        fun shipment(orderItem: OrderItem, env: DataFetchingEnvironment) = ShipmentRepository.getShipmentByOrderItemId(orderItem.id!!)
    }


    // Resolves the fields of the Mutation type. This is a modification we are receiving but works just like any other resolver.
    @Bean
    open fun mutationResolver() = object : GraphQLMutationResolver {
        fun createOrder(orderInput: OrderInput) = OrderRepository.addOrder(orderInput.toOrder())
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(SpringBootExample::class.java, *args)
        }
    }
}