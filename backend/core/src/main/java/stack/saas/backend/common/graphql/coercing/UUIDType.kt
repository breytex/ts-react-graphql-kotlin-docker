package stack.saas.backend.common.graphql.coercing

import graphql.schema.*
import java.util.*


class UUIDCoercing : Coercing<UUID, String> {
    companion object {
        val type = GraphQLScalarType("UUID", "A formatted java.util.UUID", UUIDCoercing())
    }

    override fun parseValue(input: Any?): UUID = try {
        UUID.fromString(serialize(input))
    } catch (e: Exception) {
        throw CoercingParseValueException(e)
    }

    override fun parseLiteral(input: Any?): UUID? = try {
        val isoString = (input as? UUID)?.toString()
        UUID.fromString(isoString)
    } catch (e: Exception) {
        throw CoercingParseLiteralException(e)
    }

    override fun serialize(fetchedResult: Any?): String = try {
        fetchedResult.toString()
    } catch (e: Exception) {
        throw CoercingSerializeException(e)
    }
}