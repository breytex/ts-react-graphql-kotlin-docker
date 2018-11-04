package eu.darken.backend.common.exts

import com.expedia.graphql.TopLevelObjectDef

fun List<Any>.toTopLevelObjectDefs() = this.map {
    TopLevelObjectDef(it)
}