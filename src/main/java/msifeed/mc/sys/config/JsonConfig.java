package msifeed.mc.sys.config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.lang.reflect.Constructor;

public class JsonConfig<T> {
    private static final Logger LOGGER = LogManager.getLogger("Aorta.Config");

    private TypeToken<T> type;
    private String filename;
    private final Gson gson;
    private boolean sync;

    private T value;

    JsonConfig(TypeToken<T> type, String filename, Gson gson, boolean sync) {
        this.type = type;
        this.filename = filename;
        this.gson = gson;
        this.sync = sync;
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
        return sync;
    }

    String getFilename() {
        return filename;
    }

    String toJson() {
        return gson.toJson(value);
    }

    void fromJson(String json) {
        try {
            value = gson.fromJson(json, type.getType());
        } catch (Exception e) {
            LOGGER.error("Error while parsing JSON of config '{}': '{}'", filename, e.getMessage());
            throw e;
        }
    }

    void reload() {
        read();
        if (value == null)
            value = getDefaultConfig();
        write();
    }

    void save() {
        if (value == null)
            value = getDefaultConfig();
        write();
    }

    private void read() {
        final File filepath = ConfigManager.getConfigFile(filename);
        if (!filepath.exists())
            return;

        try {
            value = gson.fromJson(new FileReader(filepath), type.getType());
        } catch (IOException e) {
            LOGGER.error("Error while reading '{}' config: {}", filename, e.getMessage());
            throw new RuntimeException("Failed to read config file: '" + filename  + "'");
        }
    }

    private void write() {
        try (Writer writer = new FileWriter(ConfigManager.getConfigFile(filename))) {
            gson.toJson(value, writer);
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
