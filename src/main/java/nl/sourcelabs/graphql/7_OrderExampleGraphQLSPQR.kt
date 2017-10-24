package nl.sourcelabs.graphql

import graphql.GraphQL
import io.leangen.graphql.GraphQLSchemaGenerator
import io.leangen.graphql.annotations.GraphQLArgument
import io.leangen.graphql.annotations.GraphQLQuery

class OrderExampleGraphQLSPQR {

    // Define your plain services
    object OrderService {
        // Bind Java to GraphQL SPQR in the code
        @GraphQLQuery(name = "order")
        fun order(@GraphQLArgument(name = "id") id: Long) = OrderRepository.getOrderById(id)
    }

    val graphQLSchema = GraphQLSchemaGenerator()
            .withOperationsFromSingleton(OrderService) // Registers the code containing relevant annotations
            .generate()

    // Dynamic schema generation example with GraphQL SPQR
    fun execute() {
        // Create the executable schema
        val graphQLSchema = GraphQLSchemaGenerator()
                .withOperationsFromSingleton(OrderService) // Registers the code containing relevant annotations
                .generate()
        // Build GraphQL
        val graphQL = GraphQL.newGraphQL(graphQLSchema).build()
        // Execute a query
        val executionResult = graphQL.execute("""
                query {
                    order(id: 4370307900) {
                        totalPrice
                    }
                }
                """)
        println(executionResult.toSpecification())
    }
}

fun main(args: Array<String>) {
    OrderExampleGraphQLSPQR().execute()
}