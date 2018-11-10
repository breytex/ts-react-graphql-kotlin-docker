package stack.saas.backend.common.graphql

import com.expedia.graphql.TopLevelObjectDef

fun List<Any>.toTopLevelObjectDefs() = this.map {
    TopLevelObjectDef(it)
}

fun Set<Any>.toTopLevelObjectDefs() = this.map {
    TopLevelObjectDef(it)
}