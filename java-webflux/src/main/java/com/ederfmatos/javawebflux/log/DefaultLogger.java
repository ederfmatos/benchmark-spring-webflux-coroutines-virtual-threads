package com.ederfmatos.javawebflux.log;

import java.util.function.Consumer;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

final class DefaultLogger implements Logger {
    private final org.slf4j.Logger logger;

    public DefaultLogger(Class<?> clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
    }

    @Override
    public void info(Consumer<Context> log) {
        var context = new Context();
        log.accept(context);
        context.getParams().forEach((key, value) -> {
            MDC.put(key, value.toString());
        });
        this.logger.info(context.getMessage());
        context.getParams().keySet().forEach(MDC::remove);
    }
}
