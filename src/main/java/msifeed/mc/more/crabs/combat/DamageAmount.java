package msifeed.mc.more.crabs.combat;

import net.minecraft.util.DamageSource;

public final class DamageAmount {
    public final DamageSource source;
    public float amount;

    public DamageAmount(DamageSource source, float amount) {
        this.source = source;
        this.amount = amount;
    }
}
