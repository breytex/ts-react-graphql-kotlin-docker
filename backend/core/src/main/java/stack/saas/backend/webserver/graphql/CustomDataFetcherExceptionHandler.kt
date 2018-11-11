package stack.saas.backend.webserver.graphql

import graphql.ErrorType
import graphql.GraphQLError
import graphql.GraphqlErrorHelper
import graphql.execution.DataFetcherExceptionHandler
import graphql.execution.DataFetcherExceptionHandlerParameters
import graphql.execution.ExecutionPath
import graphql.language.SourceLocation
import stack.saas.backend.Application
import stack.saas.backend.common.graphql.PublicGraphQLException
import stack.saas.backend.common.logger
import java.lang.reflect.InvocationTargetException
import java.util.*
import javax.inject.Inject

class CustomDataFetcherExceptionHandler @Inject constructor() : DataFetcherExceptionHandler {
    val log = logger(this::class)

    override fun accept(handlerParameters: DataFetcherExceptionHandlerParameters) {
        log.warn(handlerParameters.exception)

        val sourceLocation = handlerParameters.field.sourceLocation
        val path = handlerParameters.path

        var rootCause = handlerParameters.exception
        while (rootCause is InvocationTargetException && rootCause.targetException != null || rootCause.cause != null) {
            rootCause = if (rootCause is InvocationTargetException) rootCause.targetException else rootCause.cause

            if (rootCause is PublicGraphQLException) break
        }

        val error: GraphQLError = PublicGraphQLError(path, rootCause, sourceLocation)

        handlerParameters.executionContext.addError(error, path)
    }
}

class PublicGraphQLError(executionPath: ExecutionPath, exception: Throwable, sourceLocation: SourceLocation) : GraphQLError {
    private val errorMessage: String
    private val errorExtensions: Map<String, Any>?
    private val executionPath: MutableList<Any> = executionPath.toList()
    private val sourceLocationList = listOf(sourceLocation)
    private val stackTrace: List<String>

    init {
        stackTrace = mutableListOf()
        if (Application.isDebug) {
            exception.stackTrace.forEach { stackTrace.add(it.toString()) }
        }

        var message = exception.message
        if (message == null) message = exception.javaClass.canonicalName
        errorMessage = String.format("Exception while fetching data (%s) : %s", path, message)

        var extensions: MutableMap<String, Any>? = null
        if (exception is GraphQLError) {
            val map = (exception as GraphQLError).extensions
            if (map != null) {
                extensions = LinkedHashMap()
                extensions.putAll(map)
            }
        }
        errorExtensions = extensions
    }

    fun getStackTrace(): List<String> {
        return stackTrace
    }

    override fun getPath(): MutableList<Any> {
        return executionPath
    }

    override fun getMessage(): String {
        return errorMessage
    }

    override fun getLocations(): List<SourceLocation> {
        return sourceLocationList
    }

    override fun getExtensions(): Map<String, Any>? {
        return errorExtensions
    }

    override fun getErrorType(): ErrorType {
        return ErrorType.DataFetchingException
    }

    override fun toString(): String {
        return "PublicGraphQLError{path=${path}exception=${errorMessage}locations=$locations${'}'}"
    }

    override fun equals(other: Any?): Boolean {
        return GraphqlErrorHelper.equals(this, other)
    }

    override fun hashCode(): Int {
        return GraphqlErrorHelper.hashCode(this)
    }
}