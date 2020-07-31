package msifeed.mc.more.crabs.combat;

import msifeed.mc.more.crabs.action.Action;
import msifeed.mc.more.crabs.character.Character;
import msifeed.mc.more.crabs.rolls.Modifiers;
import msifeed.mc.more.crabs.utils.ActionAttribute;
import msifeed.mc.more.crabs.utils.CharacterAttribute;
import msifeed.mc.more.crabs.utils.CombatAttribute;
import msifeed.mc.more.crabs.utils.MetaAttribute;
import net.minecraft.entity.EntityLivingBase;

import java.lang.ref.WeakReference;

public class FighterInfo {
    public final WeakReference<EntityLivingBase> weakEntity;
    public final CombatContext com;
    public final ActionContext act;
    public final Character chr;
    public final Modifiers mod;
    public Action comboAction = null;

    public FighterInfo(EntityLivingBase entity) {
        this.weakEntity = new WeakReference<>(entity);
        this.com = CombatAttribute.require(entity);
        this.act = ActionAttribute.require(entity);
        this.chr = CharacterAttribute.require(entity);
        this.mod = MetaAttribute.require(entity).modifiers;
    }

    public EntityLivingBase entity() {
        return this.weakEntity.get();
    }
}
