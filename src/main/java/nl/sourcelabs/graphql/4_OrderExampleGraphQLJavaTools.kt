package nl.sourcelabs.graphql.schema

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.coxautodev.graphql.tools.SchemaParser
import graphql.GraphQL
import graphql.schema.GraphQLSchema
import nl.sourcelabs.graphql.Order

class OrderResolver : GraphQLQueryResolver {
    fun getOrderById(id: Int) = when (id) {
        123 -> Order("19.99")
        else -> null
    }
}

fun main(args: Array<String>) {
    val schema = """
        type Query {
            orderById(id: Int): Order
        }
        type Order {
            totalPrice: String
        }
    """

    val graphQLSchema: GraphQLSchema = SchemaParser.newParser()
            .schemaString(schema)
            .resolvers(OrderResolver())
            .build()
            .makeExecutableSchema()

    val graphQL = GraphQL.newGraphQL(graphQLSchema).build()
    val executionResult = graphQL.execute("{ orderById(id: 123) { totalPrice } }")

    println(executionResult.getData<Any>())
}