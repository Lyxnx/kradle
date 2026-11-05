package io.github.lyxnx.kradle.dsl

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY)
@RequiresOptIn(
    message = "This API is not intended to be used outside of Kradle and is effectively internal",
    level = RequiresOptIn.Level.ERROR
)
public annotation class InternalKradleApi
