package eu.darken.backend

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import eu.darken.backend.common.exts.logger
import eu.darken.backend.common.moshi.InstantAdapter
import eu.darken.backend.common.moshi.UUIDAdapter
import javax.inject.Singleton


@Module
class MoshiModule {
    private val log = logger(MoshiModule::class)

    init {
        log.info("Init done")
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
                .add(UUIDAdapter())
                .add(InstantAdapter())
                .add(KotlinJsonAdapterFactory())
                .build()
    }
}
