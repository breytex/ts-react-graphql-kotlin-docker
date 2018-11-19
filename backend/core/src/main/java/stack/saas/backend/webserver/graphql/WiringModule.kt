package stack.saas.backend.webserver.graphql

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoSet
import stack.saas.backend.common.graphql.DirectiveWiring
import stack.saas.backend.webserver.graphql.components.hello.schema.directive.StringValidationDirectiveWiring


@Module
abstract class WiringModule {

    @Binds @IntoSet abstract fun stringValidation(w: StringValidationDirectiveWiring): DirectiveWiring

}