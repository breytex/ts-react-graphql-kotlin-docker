package scripts.seeder

import dagger.BindsInstance
import dagger.Component
import io.vertx.reactivex.core.Vertx
import stack.saas.backend.ConfigModule
import stack.saas.backend.MongoModule
import javax.inject.Singleton


@Singleton
@Component(modules = [
    ConfigModule::class,
    MongoModule::class
])
interface SeederComponent {
    fun seeder(): Seeder

    @Component.Builder
    interface Builder {
        @BindsInstance fun vertx(vertx: Vertx): Builder
        fun build(): SeederComponent
    }
}