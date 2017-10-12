package nl.sourcelabs.graphql

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import com.coxautodev.graphql.tools.GraphQLQueryResolver
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

// graphql-spring-boot-starter example
@SpringBootApplication
open class SpringBootExample {

    // We need to register at least 1 bean of type GraphQLResolver
    @Bean
    open fun queryResolver() = object : GraphQLQueryResolver {
        fun order(id: Long): Order? = OrderRepository.getOrderById(id)
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