package stack.saas.backend.webserver.graphql.components.hello.schema.directive

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironmentBuilder
import graphql.schema.GraphQLFieldDefinition
import graphql.schema.idl.SchemaDirectiveWiringEnvironment
import stack.saas.backend.common.graphql.DirectiveWiring
import stack.saas.backend.common.graphql.DirectiveWiring.Companion.getDirectiveName
import javax.inject.Inject

class StringValidationDirectiveWiring @Inject constructor() : DirectiveWiring {
    private val dirName = getDirectiveName(StringValidation::class)

    override fun isResponsible(environment: SchemaDirectiveWiringEnvironment<*>): Boolean {
        (environment.element as? GraphQLFieldDefinition)?.arguments?.forEach { arg ->
            if (arg.getDirective(dirName) != null) {
                return true
            }
        }
        return false
    }

    override fun onField(wiringEnv: SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition>): GraphQLFieldDefinition {
        val field = wiringEnv.element
        val originalDataFetcher: DataFetcher<Any> = field.dataFetcher

        val defaultValueFetcher = DataFetcher<Any> { dataEnv ->
            val newArguments = HashMap(dataEnv.arguments)
            wiringEnv.element.arguments.asSequence()
                .map { gqlArg ->
                    val strArg: String? = dataEnv.getArgument(gqlArg.name) as String?
                    Pair(gqlArg, strArg)
                }
                .forEach { (gqlArg, value) ->
                    if (gqlArg.getDirective(dirName).getArgument(StringValidation::lowerCase.name).value as Boolean) {
                        newArguments[gqlArg.name] = value?.toLowerCase()
                    }
                    if (value.isNullOrEmpty()) {
                        newArguments[gqlArg.name] = gqlArg.getDirective(dirName).getArgument(StringValidation::defaultString.name).value as String
                    }
                }
            val newEnv = DataFetchingEnvironmentBuilder.newDataFetchingEnvironment(dataEnv)
                .arguments(newArguments)
                .build()
            originalDataFetcher.get(newEnv)
        }
        return field.transform { it.dataFetcher(defaultValueFetcher) }
    }
}