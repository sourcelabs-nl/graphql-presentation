package nl.sourcelabs.graphql.schema

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.coxautodev.graphql.tools.SchemaParser
import graphql.GraphQL
import graphql.schema.GraphQLSchema
import nl.sourcelabs.graphql.OrderRepository

// Define a GraphQL Java Tools QueryResolver
class QueryResolver : GraphQLQueryResolver {
    fun orderById(id: Long) = OrderRepository.getOrderById(id)
}

// Schema first with GraphQL Java Tools
fun main(args: Array<String>) {
    // Define the schema
    val schema = """
        type Query {
            order(id: Int): Order
        }
        type Order {
            totalPrice: String
        }
    """
    // Create the executable schema
    val graphQLSchema: GraphQLSchema = SchemaParser.newParser()
            .schemaString(schema)
            .resolvers(object: GraphQLQueryResolver {
                fun order(id: Long) = OrderRepository.getOrderById(id)
            })
            .build()
            .makeExecutableSchema()
    // Build GraphQL
    val graphQL = GraphQL.newGraphQL(graphQLSchema).build()
    // Execute a query
    val executionResult = graphQL.execute("""
                {
                    order(id: 123) {
                        totalPrice
                    }
                }
                """)
    println(executionResult.toSpecification())
}