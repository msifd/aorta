package msifeed.mc.sys.config;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class ConfigBuilder<T> {
    private final TypeToken<T> type;
    private final String filename;
    private boolean sync = false;
    private final GsonBuilder gsonBuilder = new GsonBuilder();

    private ConfigBuilder(TypeToken<T> type, String filename) {
        this.type = type;
        this.filename = filename;

        gsonBuilder.setPrettyPrinting();
    }

    public static <T> ConfigBuilder<T> of(Class<T> clazz, String filename) {
         return new ConfigBuilder<>(TypeToken.get(clazz), filename);
    }

    public static <T> ConfigBuilder<T> of(TypeToken<T> type, String filename) {
        return new ConfigBuilder<>(type, filename);
    }

    public ConfigBuilder<T> addAdapter(Type type, Object typeAdapter) {
        this.gsonBuilder.registerTypeAdapter(type, typeAdapter);
        return this;
    }

    public ConfigBuilder<T> sync() {
        this.sync = true;
        return this;
    }

    public JsonConfig<T> create() {
        final JsonConfig<T> handler = new JsonConfig<>(type, filename, gsonBuilder.create(), sync);
        ConfigManager.register(handler);
        return handler;
    }
}
