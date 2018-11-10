package stack.saas.backend.webserver.graphql.extensions

import graphql.schema.*
import java.time.Instant


class InstantCoercing : Coercing<Instant, String> {
    companion object {
        val type = GraphQLScalarType("Instant", "A ISO-8601 formatted java.time.Instant", InstantCoercing())
    }

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