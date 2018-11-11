package stack.saas.backend.common.graphql

import graphql.GraphQLException

open class PublicGraphQLException constructor(message: String?, cause: Throwable?) : GraphQLException(message, cause) {
    constructor() : this(null, null)
    constructor(message: String?) : this(message, null)
    constructor(throwable: Throwable?) : this(null, throwable)
}