package com.ederfmatos.kotlintomcat.log

import io.github.oshai.kotlinlogging.KotlinLogging
import org.slf4j.MDC
import kotlin.reflect.KClass

internal class KotlinLogger(clazz: KClass<*>) : Logger {
    private val logger = KotlinLogging.logger(clazz.qualifiedName!!)

    override fun info(messageBlock: Logger.Context.() -> Unit) {
        val context = Logger.Context("", mutableMapOf())
        messageBlock.invoke(context)
        context.params.forEach { (key, value) ->
            MDC.put(key, value.toString())
        }
        logger.info { context.message }
        context.params.keys.forEach(MDC::remove)
    }
}