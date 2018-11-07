package eu.darken.backend.webserver.graphql.schemas.hello

import eu.darken.backend.common.exts.logger
import org.dataloader.BatchLoader
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage
import javax.inject.Inject

class HelloDetailsBatchLoader @Inject constructor(private val helloRepo: HelloRepo) : BatchLoader<String, HelloDetails> {
    val log = logger(this::class)

    override fun load(keys: MutableList<String>): CompletionStage<MutableList<HelloDetails?>> {
        val start = System.currentTimeMillis()
        val future = CompletableFuture<MutableList<HelloDetails?>>()

        val helloMap = mutableMapOf<String, HelloDetails>()
        helloRepo.getAllDetails(keys)
                .doOnSuccess { log.info("##### Got ${it.size} HelloDetails in ${System.currentTimeMillis() - start}ms") }
                .flattenAsObservable { it }
                .map {
                    helloMap[it.helloId.toString()] = it
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