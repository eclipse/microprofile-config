package org.eclipse.microprofile.config.spi;

public interface ConfigSourceInterceptor {
    String getValue(ConfigSourceInterceptorContext context) throws Exception;
}
