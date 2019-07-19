package msifeed.mc.aorta.sys.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import msifeed.mc.aorta.sys.config.adapters.ZoneIdAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.lang.reflect.Constructor;
import java.time.ZoneId;

public class JsonConfig<T> {
    private static final Logger LOGGER = LogManager.getLogger("Aorta.Config");
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(ZoneId.class, new ZoneIdAdapter())
            .create();
    private ConfigMode mode;
    private TypeToken<T> type;
    private String filename;
    private T value;

    JsonConfig(ConfigMode mode, TypeToken<T> type, String filename) {
        this.mode = mode;
        this.type = type;
        this.filename = filename;
        this.value = getDefaultConfig();
    }

    public boolean valid() {
        return value != null;
    }

    public T get() {
        return value;
    }

    public void set(T v) {
        value = v;
    }

    boolean isSyncable() {
        return mode.sync;
    }

    String getFilename() {
        return filename;
    }

    String toJson() {
        return GSON.toJson(value);
    }

    void fromJson(String json) {
        try {
            value = GSON.fromJson(json, type.getType());
        } catch (Exception e) {
            LOGGER.error("Error while parsing JSON of config '{}': '{}'", filename, e.getMessage());
            throw e;
        }
    }

    void reload() {
        if (mode.doFileIO()) {
            read();
            if (value == null)
                value = getDefaultConfig();
            write();
        }
    }

    void save() {
        if (mode.doFileIO()) {
            if (value == null)
                value = getDefaultConfig();
            write();
        }
    }

    private void read() {
        final File filepath = ConfigManager.getConfigFile(filename);
        if (!filepath.exists())
            return;

        try {
            value = GSON.fromJson(new FileReader(filepath), type.getType());
        } catch (IOException e) {
            LOGGER.error("Error while reading '{}' config: {}", filename, e.getMessage());
            throw new RuntimeException("Failed to read config file: '" + filename  + "'");
        }
    }

    private void write() {
        try (Writer writer = new FileWriter(ConfigManager.getConfigFile(filename))) {
            GSON.toJson(value, writer);
        } catch (IOException e) {
            LOGGER.error("Error while writing '{}' config: {}", filename, e.getMessage());
            throw new RuntimeException("Failed to write config file: '" + filename  + "'");
        }
    }

    private T getDefaultConfig() {
        try {
            Constructor<T> constructor = (Constructor<T>) type.getRawType().getConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (ReflectiveOperationException e) {
            LOGGER.error("Getting default config '{}' : {}", filename, e.getMessage());
            throw new RuntimeException("Failed to get default config for " + type.getType().getTypeName());
        }
    }
}
