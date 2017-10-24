package nl.sourcelabs.graphql

import graphql.GraphQL
import graphql.annotations.GraphQLDataFetcher
import graphql.annotations.GraphQLField
import graphql.annotations.GraphQLName
import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import graphql.schema.GraphQLSchema
import graphql.annotations.GraphQLAnnotations.`object` as graphQLObjectType

class OrderExampleGraphQLJavaAnnotations {

    class OrderDataFetcher : DataFetcher<Order> {
        override fun get(environment: DataFetchingEnvironment): Order {
            return Order(environment.getArgument<Long>("id").toString())
        }
    }

    class Order(@GraphQLField val totalPrice: String? = null)

    class Query {
        @GraphQLField
        @GraphQLDataFetcher(OrderDataFetcher::class)
        fun order(@GraphQLName("id") id: Long): Order? = null // return value is ignored
    }

    fun execute() {
        val queryType = graphQLObjectType(Query::class.java)

        val schema = GraphQLSchema.newSchema().query(queryType).build()

        val graphQL = GraphQL.newGraphQL(schema).build()

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


// Schema first with GraphQL Java Annotation
fun main(args: Array<String>) {
    OrderExampleGraphQLJavaAnnotations().execute()
}