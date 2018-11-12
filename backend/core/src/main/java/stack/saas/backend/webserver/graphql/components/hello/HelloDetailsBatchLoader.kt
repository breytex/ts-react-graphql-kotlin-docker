package stack.saas.backend.webserver.graphql.components.hello

import org.bson.types.ObjectId
import org.dataloader.BatchLoader
import stack.saas.backend.common.logger
import stack.saas.backend.webserver.graphql.components.hello.schema.HelloDetails
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage
import javax.inject.Inject

class HelloDetailsBatchLoader @Inject constructor(private val helloRepo: HelloRepo) : BatchLoader<ObjectId, HelloDetails> {
    val log = logger(this::class)

    override fun load(keys: MutableList<ObjectId>): CompletionStage<MutableList<HelloDetails?>> {
        val start = System.currentTimeMillis()
        val future = CompletableFuture<MutableList<HelloDetails?>>()

        val helloMap = mutableMapOf<ObjectId, HelloDetails>()
        helloRepo.getAllDetails(keys)
                .doOnSuccess { log.info("##### Got ${it.size} HelloDetails in ${System.currentTimeMillis() - start}ms") }
                .flattenAsObservable { it }
                .map {
                    helloMap[it.helloId] = it
                    return@map it
                }
                .toList()
                .map {
                    val hellos = mutableListOf<HelloDetails?>()
                    keys.forEach { id -> hellos.add(helloMap.get(id)) }
                    return@map hellos
                }
                .doFinally { log.info("##### HelloDetailsBatchLoader done in ${System.currentTimeMillis() - start}ms (${keys.size} items)") }
                .subscribe({ future.complete(it) }, { future.completeExceptionally(it) })
        return future
    }

}