package msifeed.mc.more.crabs;

import msifeed.mc.more.crabs.action.ActionRegistry;
import msifeed.mc.more.crabs.character.CharRpc;
import msifeed.mc.more.crabs.combat.CombatManager;
import msifeed.mc.more.crabs.combat.CombatRpc;
import msifeed.mc.more.crabs.meta.MetaRpc;
import msifeed.mc.more.crabs.utils.ActionAttribute;
import msifeed.mc.more.crabs.utils.CharacterAttribute;
import msifeed.mc.more.crabs.utils.CombatAttribute;
import msifeed.mc.more.crabs.utils.MetaAttribute;
import msifeed.mc.sys.attributes.AttributeHandler;
import msifeed.mc.sys.rpc.Rpc;

public class Crabs {
    public void preInit() {
        ActionRegistry.INSTANCE.init();
        CombatManager.INSTANCE.init();

        AttributeHandler.registerAttribute(CharacterAttribute.INSTANCE);
        AttributeHandler.registerAttribute(MetaAttribute.INSTANCE);
        AttributeHandler.registerAttribute(CombatAttribute.INSTANCE);
        AttributeHandler.registerAttribute(ActionAttribute.INSTANCE);

        Rpc.register(CharRpc.INSTANCE);
        Rpc.register(MetaRpc.INSTANCE);
        Rpc.register(CombatRpc.INSTANCE);
    }
}
