package nl.sourcelabs.graphql

import graphql.GraphQL
import graphql.Scalars
import graphql.schema.*

// Code first example
fun main(args: Array<String>) {
    // Define the schema with builders
    val orderType = GraphQLObjectType.newObject()
            .name("Order")
            .field(GraphQLFieldDefinition.newFieldDefinition()
                    .name("totalPrice")
                    .type(Scalars.GraphQLString)).build()

    val queryType = GraphQLObjectType.newObject()
            .name("Query")
            .field(GraphQLFieldDefinition.newFieldDefinition()
                    .name("order")
                    .argument(GraphQLArgument("id", Scalars.GraphQLLong))
                    .type(orderType)
                    .dataFetcher { env: DataFetchingEnvironment -> OrderRepository.getOrderById(env.getArgument<Long>("id")) }
                    .build()
            ).build()
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