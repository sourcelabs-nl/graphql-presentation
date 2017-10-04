package nl.sourcelabs.graphql

import graphql.GraphQL
import graphql.schema.GraphQLSchema
import graphql.schema.StaticDataFetcher
import graphql.schema.idl.RuntimeWiring.newRuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser

// Schema first example
fun main(args: Array<String>) {
    // Define schema, just a string describing the types.
    val schema = """
        type Query {
            hello: String
        }
    """
    val typeDefinitionRegistry = SchemaParser().parse(schema)
    // Wire behaviour to the types
    val runtimeWiring = newRuntimeWiring()
            .type("Query", { builder -> builder.dataFetcher("hello", StaticDataFetcher("world")) })
            .build()
    // Make the schema executable
    val graphQLSchema: GraphQLSchema = SchemaGenerator().makeExecutableSchema(typeDefinitionRegistry, runtimeWiring)
    // Build GraphQL
    val graphQL = GraphQL.newGraphQL(graphQLSchema).build()
    // Execute a query
    val executionResult = graphQL.execute("{ hello }")
    println(executionResult.getData<Any>())
}