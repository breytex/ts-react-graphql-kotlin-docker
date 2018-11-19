package stack.saas.backend.webserver.graphql

import com.expedia.graphql.schema.generator.directive.DirectiveWiringHelper
import com.expedia.graphql.schema.hooks.SchemaGeneratorHooks
import graphql.schema.GraphQLType
import org.bson.types.ObjectId
import stack.saas.backend.common.graphql.coercing.InstantCoercing
import stack.saas.backend.common.graphql.coercing.ObjectIdCoercing
import stack.saas.backend.common.graphql.coercing.UUIDCoercing
import java.time.Instant
import java.util.*
import javax.inject.Inject
import kotlin.reflect.KClass
import kotlin.reflect.KType

class CustomSchemaGeneratorHooks @Inject constructor(customWiringFactory: CustomWiringFactory) : SchemaGeneratorHooks {
    private val directiveWiringHelper = DirectiveWiringHelper(customWiringFactory)

    override fun willGenerateGraphQLType(type: KType): GraphQLType? {
        return when (type.classifier as? KClass<*>) {
            Instant::class -> InstantCoercing.type
            UUID::class -> UUIDCoercing.type
            ObjectId::class -> ObjectIdCoercing.type
            else -> null
        }
    }

    override fun onRewireGraphQLType(type: KType, generatedType: GraphQLType): GraphQLType {
        return directiveWiringHelper.onWire(generatedType)
    }
}

