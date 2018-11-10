package stack.saas.backend

import dagger.Module
import dagger.Provides
import io.vertx.core.Verticle
import io.vertx.core.spi.VerticleFactory
import javax.inject.Provider
import javax.inject.Singleton

@Module
class DaggerVerticleFactoryModule {

    /**
     * Provides a single instance of the Daggerized [VerticleFactory].
     *
     * Note: The [JvmSuppressWildcards] annotation is a Kotlin specific thing.
     * It advise the Kotlin compiler to not generate wildcard generics. Otherwise the Dagger code generation will fail.
     */
    @JvmSuppressWildcards
    @Provides
    @Singleton
    fun provideVerticleFactory(verticleMap: Map<Class<*>, Provider<Verticle>>): VerticleFactory = DaggerVerticleFactory(verticleMap)
}
