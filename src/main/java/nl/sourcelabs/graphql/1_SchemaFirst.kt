package nl.sourcelabs.graphql

import graphql.GraphQL
import graphql.schema.GraphQLSchema
import graphql.schema.StaticDataFetcher
import graphql.schema.idl.RuntimeWiring.newRuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser


fun main(args: Array<String>) {
    val schema = """
        type Query {
            hello: String
        }
    """
    val typeDefinitionRegistry = SchemaParser().parse(schema)
    val runtimeWiring = newRuntimeWiring()
            .type("Query", { builder -> builder.dataFetcher("hello", StaticDataFetcher("world")) })
            .build()

    val graphQLSchema: GraphQLSchema = SchemaGenerator().makeExecutableSchema(typeDefinitionRegistry, runtimeWiring)
    val graphQL = GraphQL.newGraphQL(graphQLSchema).build()
    val executionResult = graphQL.execute("{ hello }")

    println(executionResult.getData<Any>())
}