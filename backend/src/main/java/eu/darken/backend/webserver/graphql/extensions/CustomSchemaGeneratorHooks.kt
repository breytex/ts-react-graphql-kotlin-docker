package eu.darken.backend.webserver.graphql.extensions

import com.expedia.graphql.schema.hooks.NoopSchemaGeneratorHooks
import graphql.schema.*
import java.time.Instant
import javax.inject.Inject
import kotlin.reflect.KClass
import kotlin.reflect.KType

class CustomSchemaGeneratorHooks @Inject constructor() : NoopSchemaGeneratorHooks() {

    override fun willGenerateGraphQLType(type: KType): GraphQLType? {
        return when (type.classifier as? KClass<*>) {
            Instant::class -> typeInstant
            else -> null
        }
    }
}

internal val typeInstant = GraphQLScalarType("Instant", "A ISO-8601 formatted java.time.Instant", InstantCoercing)

private object InstantCoercing : Coercing<Instant, String> {
    override fun parseValue(input: Any?): Instant = try {
        Instant.parse(serialize(input))
    } catch (e: Exception) {
        throw CoercingParseValueException(e)
    }

    override fun parseLiteral(input: Any?): Instant? = try {
        val isoString = (input as? Instant)?.toString()
        Instant.parse(isoString)
    } catch (e: Exception) {
        throw CoercingParseLiteralException(e)
    }

    override fun serialize(fetchedResult: Any?): String = try {
        fetchedResult.toString()
    } catch (e: Exception) {
        throw CoercingSerializeException(e)
    }
}