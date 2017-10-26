package nl.sourcelabs.graphql

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.coxautodev.graphql.tools.GraphQLResolver
import graphql.GraphQLError
import graphql.schema.DataFetchingEnvironment
import graphql.servlet.DefaultGraphQLErrorHandler
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

/**
 * This is an example application demonstrating a schema-first approach using graphql-java-tools in a Spring boot application.
 * The graphql-spring-boot-starter takes care of configuring the graphql-servlet and the type/schema configuration for graphql-java-tools.
 * Check the order-example.graphqls file for the schema details.
 */
@SpringBootApplication
open class SpringBootExample {

    /**
     * Resolves the fields for the (root) Query type
     */
    @Bean
    open fun queryResolver() = object : GraphQLQueryResolver {
        fun orders() = OrderRepository.getOrders()
        fun order(id: Long, env: DataFetchingEnvironment) = OrderRepository.getOrderById(id)
    }

    /**
     * Resolves the fields for the OrderItem type
     */
    @Bean
    open fun orderItemResolver() = object : GraphQLResolver<OrderItem> {
        /**
         * Resolves the product information for an orderItem
         */
        fun product(orderItem: OrderItem, env: DataFetchingEnvironment) = ProductRepository.getProductById(orderItem.productId)

        /**
         * Resolves the shipment information for an orderItem
         */
        fun shipment(orderItem: OrderItem, env: DataFetchingEnvironment) = ShipmentRepository.getShipmentByOrderItemId(orderItem.id!!)

        /**
         * Example of a field that will throw an exception
         */
        fun exception(orderItem: OrderItem, env: DataFetchingEnvironment): String = throw RuntimeException("something really wrong here")
    }

    /**
     * Resolves the fields for the Mutation type. This is a modification we are receiving but works just like any other resolver.
     */
    @Bean
    open fun mutationResolver() = object : GraphQLMutationResolver {
        fun createOrder(orderInput: OrderInput) = OrderRepository.addOrder(orderInput.toOrder())
    }

    /**
     * GraphQL servlet by default filters all the exception that happen while fetching data, during development this can be annoying.
     * We can expose al details by overriding the default GraphQLErrorHandler and expose all errors (including stacktraces) 1-on-1.
     * Don`t use in production!
     */
    @Bean
    open fun graphQLErrorHandler() = object : DefaultGraphQLErrorHandler() {
        override fun processErrors(errors: List<GraphQLError>) = errors
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(SpringBootExample::class.java, *args)
        }
    }
}