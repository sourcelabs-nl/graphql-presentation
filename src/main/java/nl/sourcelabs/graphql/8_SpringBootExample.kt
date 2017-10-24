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

    // We need to register at least 1 bean of type GraphQLResolver
    @Bean
    open fun queryResolver() = object : GraphQLQueryResolver {
        fun order(id: Long, env: DataFetchingEnvironment): Order? {
            return OrderRepository.getOrderById(id)
        }
    }

    @Bean
    open fun productResolver() = object : GraphQLResolver<OrderItem> {
        fun product(orderItem: OrderItem, env: DataFetchingEnvironment): Product? {
            return Product("http://image.url")
        }
    }

    @Bean
    open fun mutationResolver() = object : GraphQLMutationResolver {
        fun createOrder(order: OrderInput): Order? = Order(totalPrice = order.totalPrice)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(SpringBootExample::class.java, *args)
        }
    }
}