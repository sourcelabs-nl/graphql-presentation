package nl.sourcelabs.graphql

import graphql.GraphQL
import graphql.Scalars
import graphql.schema.*
import graphql.schema.GraphQLArgument.newArgument

// graphql-java DataFetcher example
class OrderExampleDataFetcher {

    // Define a plain GraphQL Java DataFetcher
    class OrderDataFetcher : DataFetcher<Order> {
        override fun get(env: DataFetchingEnvironment): Order? {
            return OrderRepository.getOrderById(env.getArgument<Long>("id"))
        }
    }

    fun execute() {
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
                        .name("order")
                        .type(orderType)
                        .argument(newArgument()
                                .name("id")
                                .type(Scalars.GraphQLLong)
                        )
                        .dataFetcher(OrderDataFetcher()) // This defines how the data is fetched for field order
                )
                .build()
        // Create the executable schema
        val graphQLSchema: GraphQLSchema = GraphQLSchema.newSchema().query(queryType).build()
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
    OrderExampleDataFetcher().execute()
}