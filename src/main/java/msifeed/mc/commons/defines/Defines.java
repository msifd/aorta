package msifeed.mc.commons.defines;

import msifeed.mc.extensions.chat.ChatDefines;
import msifeed.mc.extensions.locks.LocksDefines;
import msifeed.mc.more.content.ItemDefines;
import msifeed.mc.more.crabs.combat.CombatDefines;
import msifeed.mc.sys.config.ConfigBuilder;
import msifeed.mc.sys.config.JsonConfig;

public class Defines {
    private JsonConfig<DefinesContent> config = ConfigBuilder.of(DefinesContent.class, "defines.json").sync().create();
    private JsonConfig<CombatDefines> combat = ConfigBuilder.of(CombatDefines.class, "combat.json").sync().create();

    public Defines() {
    }

    public DefinesContent get() {
        return config.get();
    }

    public CombatDefines combat() {
        return combat.get();
    }

    public static final class DefinesContent {
        public ChatDefines chat = new ChatDefines();
        public LocksDefines locks = new LocksDefines();
        public ItemDefines items = new ItemDefines();
    }
}
