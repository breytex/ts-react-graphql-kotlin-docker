package scripts.seeder

import dagger.BindsInstance
import dagger.Component
import eu.darken.backend.ConfigModule
import eu.darken.backend.MongoModule
import eu.darken.backend.MoshiModule
import io.vertx.reactivex.core.Vertx
import javax.inject.Singleton


@Singleton
@Component(modules = [
    ConfigModule::class,
    MongoModule::class,
    MoshiModule::class
])
interface SeederComponent {
    fun seeder(): Seeder

    @Component.Builder
    interface Builder {
        @BindsInstance fun vertx(vertx: Vertx): Builder
        fun build(): SeederComponent
    }
}