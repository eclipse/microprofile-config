package org.eclipse.microprofile.config.spi;

public interface ConfigSourceInterceptorContext {
    String getKey();

    Class getConfigSource();

    String proceed(String key) throws Exception;
}
