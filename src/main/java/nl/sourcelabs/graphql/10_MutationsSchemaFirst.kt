package nl.sourcelabs.graphql

fun main(args: Array<String>) {
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
    var orderType = graphql.schema.GraphQLObjectType.newObject()
            .name("Order")
            .field(graphql.schema.GraphQLFieldDefinition.newFieldDefinition()
                    .name("id")
                    .type(graphql.Scalars.GraphQLInt))
            .field(graphql.schema.GraphQLFieldDefinition.newFieldDefinition()
                    .name("totalPrice")
                    .type(graphql.Scalars.GraphQLString))
            .build()
    // Mandatory query object
    val queryType = graphql.schema.GraphQLObjectType.newObject()
            .name("Query")
            .field(graphql.schema.GraphQLFieldDefinition.newFieldDefinition()
                    .name("order")
                    .type(orderType)
                    .argument(graphql.schema.GraphQLArgument.newArgument()
                            .name("id")
                            .type(graphql.Scalars.GraphQLInt)
                    )
                    .dataFetcher(nl.sourcelabs.graphql.OrderExampleDataFetcher.OrderDataFetcher()) // This defines how the data is fetched for field orderById
            )
            .build()
    // The mutation type
    val mutationType = graphql.schema.GraphQLObjectType.newObject()
            .name("Mutation")
            .description("Defines the mutations on this model")
            .field(graphql.schema.GraphQLFieldDefinition.newFieldDefinition()
                    // mutation operation is just a field.
                    .name("createOrder")
                    .type(orderType)
                    // input parameter for the mutation
                    .argument(graphql.schema.GraphQLArgument.newArgument()
                            .name("totalPrice")
                            .type(graphql.Scalars.GraphQLString)
                    )
                    // like any field a mutation is also bound to a dataFetcher
                    .dataFetcher { env ->
                        val totalPrice = env.getArgument<Double>("totalPrice")
                        val order = Order(totalPrice = totalPrice.bd)
                        // Stores the order and returns with generated id
                        nl.sourcelabs.graphql.OrderRepository.addOrder(order)
                    }
            ).build()

    // Create the executable schema
    val graphQLSchema: graphql.schema.GraphQLSchema = graphql.schema.GraphQLSchema.newSchema().query(queryType).mutation(mutationType).build()
    // Build GraphQL
    val graphQL = graphql.GraphQL.newGraphQL(graphQLSchema).build()
    // Execute a query
    val executionResult = graphQL.execute(graphql.ExecutionInput.newExecutionInput()
            .query(mutationQuery)
            .variables(mapOf<String, Double>("totalPrice" to 1.99)))
    println(executionResult.toSpecification())
}