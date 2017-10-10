package nl.sourcelabs.graphql

import graphql.GraphQL
import io.leangen.graphql.GraphQLSchemaGenerator
import io.leangen.graphql.annotations.GraphQLArgument
import io.leangen.graphql.annotations.GraphQLQuery

// Define your plain services
object OrderService {
    // Bind Java to GraphQL SPQR in the code
    @GraphQLQuery(name = "orderById")
    fun orderById(@GraphQLArgument(name = "id") id: Long) = OrderRepository.getOrderById(id)
}

// Dynamic schema generation example with GraphQL SPQR
fun main(args: Array<String>) {
    // Create the executable schema
    val graphQLSchema = GraphQLSchemaGenerator()
            .withOperationsFromSingleton(OrderService) // Registers the code containing relevant annotations
            .generate()
    // Build GraphQL
    val graphQL = GraphQL.newGraphQL(graphQLSchema).build()
    // Execute a query
    val executionResult = graphQL.execute("""
                {
                    orderById(id: 123) {
                        totalPrice
                    }
                }
                """)
    println(executionResult.toSpecification())
}