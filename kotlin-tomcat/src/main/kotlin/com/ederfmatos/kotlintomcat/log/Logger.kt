package com.ederfmatos.kotlintomcat.log

import kotlin.reflect.KClass

sealed interface Logger {
    fun info(messageBlock: Context.() -> Unit)

    data class Context(var message: String, val params: MutableMap<String, Any>) {
        fun withMessage(message: String): Context {
            this.message = message
            return this
        }

        fun withParam(key: String, value: Any): Context {
            this.params[key] = value
            return this
        }
    }

    companion object {
        fun create(clazz: KClass<*>): Logger {
            return KotlinLogger(clazz)
        }
    }
}