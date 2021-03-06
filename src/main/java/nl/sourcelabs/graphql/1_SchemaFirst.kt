package nl.sourcelabs.graphql

import graphql.ExecutionResult
import graphql.GraphQL
import graphql.schema.GraphQLSchema
import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import graphql.schema.idl.TypeRuntimeWiring

// Schema first example
fun main(args: Array<String>) {
    // Define schema, just a string describing the types.
    val schema = """
        type Query {
            order(id: Long): Order
        }
        type Order {
            totalPrice: String
        }
    """
    val typeDefinitionRegistry = SchemaParser().parse(schema)
    // Wire behaviour to the types
    val runtimeWiring = RuntimeWiring.newRuntimeWiring()
            .type("Query", { builder: TypeRuntimeWiring.Builder ->
                builder.dataFetcher("order", { env ->
                    OrderRepository.getOrderById(env.getArgument<Long>("id"))
                })
            }).build()
    // Make the schema executable
    val graphQLSchema: GraphQLSchema = SchemaGenerator().makeExecutableSchema(typeDefinitionRegistry, runtimeWiring)
    // Build GraphQL
    val graphQL: GraphQL = GraphQL.newGraphQL(graphQLSchema).build()
    // Execute a query
    val executionResult: ExecutionResult = graphQL.execute("""
                query {
                    order(id: 4370307900) {
                        totalPrice
                    }
                }
                """)
    println(executionResult.toSpecification())
}