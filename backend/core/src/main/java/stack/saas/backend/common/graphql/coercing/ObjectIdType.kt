package stack.saas.backend.common.graphql.coercing

import graphql.schema.*
import org.bson.types.ObjectId


class ObjectIdCoercing : Coercing<ObjectId, String> {
    companion object {
        val type = GraphQLScalarType("UUID", "A formatted java.util.UUID", UUIDCoercing())
    }

    override fun parseValue(input: Any?): ObjectId = try {
        ObjectId(serialize(input))
    } catch (e: Exception) {
        throw CoercingParseValueException(e)
    }

    override fun parseLiteral(input: Any?): ObjectId? = try {
        ObjectId((input as? ObjectId)?.toHexString())
    } catch (e: Exception) {
        throw CoercingParseLiteralException(e)
    }

    override fun serialize(fetchedResult: Any?): String = try {
        fetchedResult.toString()
    } catch (e: Exception) {
        throw CoercingSerializeException(e)
    }
}