package msifeed.mc.aorta.defines;

import com.google.gson.reflect.TypeToken;
import msifeed.mc.aorta.config.ConfigManager;
import msifeed.mc.aorta.config.ConfigMode;
import msifeed.mc.aorta.config.JsonConfig;
import msifeed.mc.aorta.defines.data.AortaDefines;
import msifeed.mc.aorta.defines.data.FightRules;

public class Defines {
    private JsonConfig<AortaDefines> config = ConfigManager.getConfig(ConfigMode.SYNC, TypeToken.get(AortaDefines.class), "defines.json");
    private JsonConfig<FightRules> rules = ConfigManager.getConfig(ConfigMode.SYNC, TypeToken.get(FightRules.class), "rules.json");

    public AortaDefines get() {
        return config.get();
    }

    public FightRules rules() {
        return rules.get();
    }
}
