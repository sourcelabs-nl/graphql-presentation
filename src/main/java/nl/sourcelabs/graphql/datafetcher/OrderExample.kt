package nl.sourcelabs.graphql.datafetcher

import graphql.GraphQL
import graphql.Scalars
import graphql.schema.*
import graphql.schema.GraphQLArgument.newArgument

data class Order(val totalPrice: String)

class OrderDataFetcher : DataFetcher<Order> {
    override fun get(env: DataFetchingEnvironment) = when (env.arguments["id"]) {
        123 -> Order("19.99")
        else -> null
    }
}

fun main(args: Array<String>) {
    val orderType = GraphQLObjectType.newObject()
            .name("Order")
            .field(GraphQLFieldDefinition.newFieldDefinition()
                    .name("totalPrice")
                    .type(Scalars.GraphQLString)
            )
            .build()

    val queryType = GraphQLObjectType.newObject()
            .name("Query")
            .field(GraphQLFieldDefinition.newFieldDefinition()
                    .name("orderById")
                    .type(orderType)
                    .argument(newArgument()
                            .name("id")
                            .type(Scalars.GraphQLInt)
                    )
                    .dataFetcher(OrderDataFetcher())
            )
            .build()

    val graphQLSchema: GraphQLSchema = GraphQLSchema.newSchema().query(queryType).build()
    val graphQL = GraphQL.newGraphQL(graphQLSchema).build()
    val executionResult = graphQL.execute("{ orderById(id: 123) { totalPrice } }")

    println(executionResult.getData<Any>())
}