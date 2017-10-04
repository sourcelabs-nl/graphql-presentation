package nl.sourcelabs.graphql

import graphql.GraphQL
import io.leangen.graphql.GraphQLSchemaGenerator
import io.leangen.graphql.annotations.GraphQLArgument
import io.leangen.graphql.annotations.GraphQLQuery

object OrderService {
    @GraphQLQuery(name = "orderById")
    fun orderById(@GraphQLArgument(name = "id") id: Int) = when (id) {
        123 -> Order("19.99")
        else -> null
    }
}

fun main(args: Array<String>) {

    val graphQLSchema = GraphQLSchemaGenerator()
            .withOperationsFromSingleton(OrderService)
            .generate()
    val graphQL = GraphQL.newGraphQL(graphQLSchema).build()
    val executionResult = graphQL.execute("{ orderById(id: 123) { totalPrice } }")

    println(executionResult.getData<Any>())
}