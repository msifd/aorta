package msifeed.mc.aorta.defines;

import com.google.gson.reflect.TypeToken;
import msifeed.mc.aorta.core.rolls.FightAction;
import msifeed.mc.aorta.defines.data.AortaDefines;
import msifeed.mc.aorta.defines.data.FightRules;
import msifeed.mc.aorta.sys.config.ConfigManager;
import msifeed.mc.aorta.sys.config.JsonConfig;

import java.util.Arrays;

public class Defines {
    private JsonConfig<AortaDefines> config = ConfigManager.getLocalConfig(TypeToken.get(AortaDefines.class), "defines.json");
    private JsonConfig<FightRules> rules = ConfigManager.getLocalConfig(TypeToken.get(FightRules.class), "rules.json");

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
