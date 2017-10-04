package nl.sourcelabs.graphql

import graphql.GraphQL
import graphql.Scalars
import graphql.schema.*
import graphql.schema.GraphQLArgument.newArgument

// Define a plain GraphQL Java DataFetcher
class OrderDataFetcher : DataFetcher<Order> {
    override fun get(env: DataFetchingEnvironment) = OrderRepository.getOrderById(env.arguments["id"] as Int)
}

fun main(args: Array<String>) {
    // Construct a schema from code
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
    // Create the executable schema
    val graphQLSchema: GraphQLSchema = GraphQLSchema.newSchema().query(queryType).build()
    // Build GraphQL
    val graphQL = GraphQL.newGraphQL(graphQLSchema).build()
    // Execute a query
    val executionResult = graphQL.execute("{ orderById(id: 123) { totalPrice } }")
    println(executionResult.getData<Any>())
}