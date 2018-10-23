package msifeed.mc.aorta.config;

import com.google.common.eventbus.Subscribe;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Constructor;

public class JsonConfig<T> {
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
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

    public T get() {
        return value;
    }

    @Subscribe
    public void onReloadEvent(ConfigEvent.Reload event) {
        if (mode.useConfigFile()) {
            File configFile = getConfigFile();
            read(configFile);
            if (value == null)
                value = getDefaultConfig();
            write(configFile);
        }
    }

    @Subscribe
    public void onCollectEvent(ConfigEvent.Collect event) {
        event.configs.put(filename, gson.toJson(value));
    }

    @Subscribe
    public void onOverrideEvent(ConfigEvent.Override event) {
        final String s = event.configs.get(filename);
        try {
            if (s != null)
                value = gson.fromJson(s, type.getType());
        } catch (Exception e) {
            ConfigManager.logger.error(e);
        }
    }

    private void read(File configFile) {
        if (!configFile.exists())
            return;

        try {
            value = gson.fromJson(new FileReader(configFile), type.getType());
        } catch (IOException e) {
            ConfigManager.logger.error("Error while reading '{}' config: {}", filename, e.getMessage());
        }
    }

    private void write(File configFile) {
        try (Writer writer = new FileWriter(configFile)) {
            gson.toJson(value, writer);
        } catch (IOException e) {
            ConfigManager.logger.error("Error while writing '{}' config: {}", filename, e.getMessage());
        }
    }

    private File getConfigFile() {
        return new File(ConfigManager.config_dir, filename);
    }

    private T getDefaultConfig() {
        try {
            Constructor<T> constructor = (Constructor<T>) type.getRawType().getConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (ReflectiveOperationException e) {
            ConfigManager.logger.error("Getting default config '{}' : {}", filename, e.getMessage());
            throw new RuntimeException("Failed to get default config for " + type.getType().getTypeName());
        }
    }
}
