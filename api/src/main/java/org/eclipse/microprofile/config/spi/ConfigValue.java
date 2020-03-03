package org.eclipse.microprofile.config.spi;

public class ConfigValue {
    private final String key;
    private final String value;
    private final Class configSource;

    ConfigValue(final String key, final String value, final Class configSource) {
        this.key = key;
        this.value = value;
        this.configSource = configSource;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public Class getConfigSource() {
        return configSource;
    }

    public static ConfigValue of(final String key, final String value, final Class configSource) {
        return new ConfigValue(key, value, configSource);
    }
}
