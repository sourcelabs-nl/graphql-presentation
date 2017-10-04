package nl.sourcelabs.graphql.schema

import graphql.GraphQL
import graphql.Scalars.GraphQLString
import graphql.schema.GraphQLFieldDefinition.newFieldDefinition
import graphql.schema.GraphQLObjectType.newObject
import graphql.schema.GraphQLSchema
import graphql.schema.StaticDataFetcher


fun main(args: Array<String>) {
    val queryType = newObject()
            .name("Query")
            .field(newFieldDefinition()
                    .name("hello")
                    .type(GraphQLString)
                    .dataFetcher(StaticDataFetcher("world"))
            )
            .build()

    val graphQLSchema: GraphQLSchema = GraphQLSchema.newSchema().query(queryType).build()
    val graphQL = GraphQL.newGraphQL(graphQLSchema).build()
    val executionResult = graphQL.execute("{ hello }")

    println(executionResult.getData<Any>())
}