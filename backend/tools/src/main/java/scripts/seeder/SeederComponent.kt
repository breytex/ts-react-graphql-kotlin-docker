package scripts.seeder

import dagger.BindsInstance
import dagger.Component
import io.vertx.reactivex.core.Vertx
import stack.saas.backend.ConfigModule
import stack.saas.backend.MongoModule
import stack.saas.backend.MoshiModule
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