package nl.sourcelabs.graphql

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import com.coxautodev.graphql.tools.GraphQLQueryResolver
import graphql.GraphQLError
import graphql.servlet.DefaultGraphQLErrorHandler
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

// graphql-spring-boot-starter example
@SpringBootApplication
open class SpringBootExampleException {

    // We need to register at least 1 bean of type GraphQLResolver
    @Bean
    open fun queryResolver() = object : GraphQLQueryResolver {
        fun order(id: Long): Order? = throw RuntimeException("something really wrong here")
    }

    @Bean
    open fun mutationResolver() = object : GraphQLMutationResolver {
        fun createOrder(order: OrderInput): Order? = Order(totalPrice = order.orderItems.totalPrice)
    }

    // Graphql servlet filters all the DataFetching exception, during development this can be annoying.
    // Expose al details by overriding the default GraphQLErrorHandler
    @Bean
    open fun graphQLErrorHandler() = object : DefaultGraphQLErrorHandler() {
        override fun processErrors(errors: List<GraphQLError>): List<GraphQLError> {
            return errors
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(SpringBootExampleException::class.java, *args)
        }
    }
}