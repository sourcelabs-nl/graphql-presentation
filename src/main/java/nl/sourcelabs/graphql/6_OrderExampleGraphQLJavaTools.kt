package nl.sourcelabs.graphql.schema

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.coxautodev.graphql.tools.SchemaParser
import graphql.GraphQL
import graphql.schema.GraphQLSchema
import nl.sourcelabs.graphql.OrderRepository

class OrderExampleGraphQLJavaTools {
    // Define a GraphQL Java Tools QueryResolver
    class QueryResolver : GraphQLQueryResolver {
        fun order(id: Long) = OrderRepository.getOrderById(id)
    }

    // Schema first with GraphQL Java Tools
    fun execute() {
        // Define the schema
        val schema = """
        type Query {
            order(id: Long): Order
        }
        type Order {
            totalPrice: String
        }
    """
        // Create the executable schema
        val graphQLSchema: GraphQLSchema = SchemaParser.newParser()
                .schemaString(schema)
                .resolvers(QueryResolver())
                .build()
                .makeExecutableSchema()
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
    OrderExampleGraphQLJavaTools().execute()
}