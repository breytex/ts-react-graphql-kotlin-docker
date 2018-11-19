package stack.saas.backend.webserver.graphql.components.hello.schema.directive

import com.expedia.graphql.annotations.GraphQLDirective

@GraphQLDirective
annotation class StringValidation(val lowerCase: Boolean = false, val defaultString: String = "Matthias")