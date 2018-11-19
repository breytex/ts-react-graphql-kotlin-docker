package stack.saas.backend.webserver.graphql

import graphql.schema.idl.SchemaDirectiveWiring
import graphql.schema.idl.SchemaDirectiveWiringEnvironment
import graphql.schema.idl.WiringFactory
import stack.saas.backend.common.graphql.DirectiveWiring
import javax.inject.Inject

class CustomWiringFactory @Inject constructor(val wirings: Set<@JvmSuppressWildcards DirectiveWiring>) : WiringFactory {

    override fun providesSchemaDirectiveWiring(environment: SchemaDirectiveWiringEnvironment<*>?): Boolean {
        return true
    }

    override fun getSchemaDirectiveWiring(environment: SchemaDirectiveWiringEnvironment<*>): SchemaDirectiveWiring? {
        return wirings.asSequence()
            .filter { it.isResponsible(environment) }
            .singleOrNull()
    }

}