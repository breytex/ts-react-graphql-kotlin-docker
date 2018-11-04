package eu.darken.backend.webserver.graphql.extensions

import com.expedia.graphql.schema.hooks.NoopSchemaGeneratorHooks
import graphql.schema.GraphQLType
import java.time.Instant
import javax.inject.Inject
import kotlin.reflect.KClass
import kotlin.reflect.KType

class CustomSchemaGeneratorHooks @Inject constructor() : NoopSchemaGeneratorHooks() {

    override fun willGenerateGraphQLType(type: KType): GraphQLType? {
        return when (type.classifier as? KClass<*>) {
            Instant::class -> InstantCoercing.type
            else -> null
        }
    }
}

