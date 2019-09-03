package msifeed.mc.aorta.sys.attributes;

import net.minecraft.entity.Entity;

public class MissingRequiredAttributeException extends RuntimeException {
    private final EntityAttribute attribute;
    private final Entity entity;

    public MissingRequiredAttributeException(EntityAttribute attribute, Entity entity) {
        this.attribute = attribute;
        this.entity = entity;
    }

    @Override
    public String toString() {
        return String.format("%s '%s' is missing required %s (%s)",
                entity.getClass().getSimpleName(),
                entity.getCommandSenderName(),
                attribute.getClass().getSimpleName(),
                attribute.getName());
    }
}
