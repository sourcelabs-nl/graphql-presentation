package nl.sourcelabs.graphql

import graphql.GraphQL
import graphql.Scalars.GraphQLString
import graphql.schema.GraphQLFieldDefinition.newFieldDefinition
import graphql.schema.GraphQLObjectType.newObject
import graphql.schema.GraphQLSchema
import graphql.schema.StaticDataFetcher

// Code first example
fun main(args: Array<String>) {
    // Define the schema with builders
    val queryType = newObject()
            .name("Query")
            .field(newFieldDefinition()
                    .name("hello")
                    .type(GraphQLString)
                    .dataFetcher(StaticDataFetcher("world"))
            )
            .build()
    // Create the executable schema
    val graphQLSchema: GraphQLSchema = GraphQLSchema.newSchema().query(queryType).build()
    // Build GraphQL
    val graphQL = GraphQL.newGraphQL(graphQLSchema).build()
    // Execute a query
    val executionResult = graphQL.execute("{ hello }")
    println(executionResult.getData<Any>())
}