package com.ederfmatos.javatomcat.log;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import lombok.Getter;

public sealed interface Logger permits DefaultLogger {
    static Logger create(Class<?> clazz) {
        return new DefaultLogger(clazz);
    }

    @Getter
    class Context {
        private String message;
        private final Map<String, Object> params = new HashMap<>();

        public Context withMessage(String message) {
            this.message = message;
            return this;
        }

        public void withParam(String key, Object value) {
            this.params.put(key, value);
        }
    }

    void info(Consumer<Context> log);
}
