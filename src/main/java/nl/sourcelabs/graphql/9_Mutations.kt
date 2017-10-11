package nl.sourcelabs.graphql

import graphql.ExecutionInput.newExecutionInput
import graphql.GraphQL
import graphql.Scalars
import graphql.schema.GraphQLArgument.newArgument
import graphql.schema.GraphQLFieldDefinition
import graphql.schema.GraphQLFieldDefinition.newFieldDefinition
import graphql.schema.GraphQLObjectType
import graphql.schema.GraphQLSchema

// The mutation query.
val mutationQuery = "\n" +
        // define the mutation name and possible variables
        "mutation CreateOrder(\$totalPrice: String!) {\n" +
        "    createOrder(totalPrice: \$totalPrice) {\n" +
        // Define the data that we want to see after the mutation has been executed
        "        id\n" +
        "        totalPrice\n" +
        "    }\n" +
        "}"

// Order type with id and totalPrice fields.
var orderType = GraphQLObjectType.newObject()
        .name("Order")
        .field(newFieldDefinition()
                .name("id")
                .type(Scalars.GraphQLInt))
        .field(newFieldDefinition()
                .name("totalPrice")
                .type(Scalars.GraphQLString))
        .build()

// Mandatory query object
val queryType = GraphQLObjectType.newObject()
        .name("Query")
        .field(GraphQLFieldDefinition.newFieldDefinition()
                .name("order")
                .type(orderType)
                .argument(newArgument()
                        .name("id")
                        .type(Scalars.GraphQLInt)
                )
                .dataFetcher(OrderExampleDataFetcher.OrderDataFetcher()) // This defines how the data is fetched for field orderById
        )
        .build()

// The mutation type
val mutationType = GraphQLObjectType.newObject()
        .name("Mutation")
        .description("Defines the mutations on this model")
        .field(GraphQLFieldDefinition.newFieldDefinition()
                // mutation operation is just a field.
                .name("createOrder")
                .type(orderType)
                // input parameter for the mutation
                .argument(newArgument()
                        .name("totalPrice")
                        .type(Scalars.GraphQLString)
                )
                // like any field a mutation is also bound to a dataFetcher
                .dataFetcher { env ->
                    val totalPrice = env.getArgument<String>("totalPrice")
                    val order = Order(totalPrice = totalPrice)
                    // Stores the order and returns with generated id
                    OrderRepository.addOrder(order)
                }
        ).build()

fun main(args: Array<String>) {
    // Create the executable schema
    val graphQLSchema: GraphQLSchema = GraphQLSchema.newSchema().query(queryType).mutation(mutationType).build()
    // Build GraphQL
    val graphQL = GraphQL.newGraphQL(graphQLSchema).build()
    // Execute a query
    val executionResult = graphQL.execute(newExecutionInput()
            .query(mutationQuery)
            .variables(mapOf<String, String>("totalPrice" to "1.99")))
    println(executionResult.toSpecification())
}