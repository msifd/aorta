package msifeed.mc.sys.attributes;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.entity.Entity;

public final class AttributeUpdateEvent extends Event {
    public final Entity entity;
    public final EntityAttribute attr;

    AttributeUpdateEvent(Entity entity, EntityAttribute attr) {
        this.entity = entity;
        this.attr = attr;
    }
}
