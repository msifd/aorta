package msifeed.mc.aorta.defines;

import com.google.gson.reflect.TypeToken;
import msifeed.mc.aorta.config.ConfigManager;
import msifeed.mc.aorta.config.ConfigMode;
import msifeed.mc.aorta.config.JsonConfig;
import msifeed.mc.aorta.core.rules.FightAction;
import msifeed.mc.aorta.defines.data.AortaDefines;
import msifeed.mc.aorta.defines.data.FightRules;

import java.util.Arrays;

public class Defines {
    private JsonConfig<AortaDefines> config = ConfigManager.getConfig(ConfigMode.SYNC, TypeToken.get(AortaDefines.class), "defines.json");
    private JsonConfig<FightRules> rules = ConfigManager.getConfig(ConfigMode.SYNC, TypeToken.get(FightRules.class), "rules.json");

    public Defines() {
        // Default rules for client
        final FightRules fr = new FightRules();
        for (FightAction fa : FightAction.values())
            fr.modifiers.put(fa, Arrays.asList(0.25, 0.25, 0.25, 0.25));
        rules.set(fr);
    }

    public AortaDefines get() {
        return config.get();
    }

    public FightRules rules() {
        return rules.get();
    }
}
