package msifeed.mc.more.crabs;

import cpw.mods.fml.common.registry.GameRegistry;
import msifeed.mc.more.crabs.action.ActionRegistry;
import msifeed.mc.more.crabs.character.CharRpc;
import msifeed.mc.more.crabs.combat.CombatManager;
import msifeed.mc.more.crabs.meta.MetaRpc;
import msifeed.mc.more.crabs.utils.CharacterAttribute;
import msifeed.mc.more.crabs.utils.MetaAttribute;
import msifeed.mc.more.tools.ItemBattleStick;
import msifeed.mc.more.tools.ItemCharSheet;
import msifeed.mc.more.tools.ItemHealthController;
import msifeed.mc.sys.attributes.AttributeHandler;
import msifeed.mc.sys.rpc.Rpc;

public class Crabs {
    private CombatManager combatManager = new CombatManager();

    public Crabs() {
        ActionRegistry.INSTANCE.init();
        combatManager.init();
    }

    public void init() {
        AttributeHandler.INSTANCE.registerAttribute(CharacterAttribute.INSTANCE);
        AttributeHandler.INSTANCE.registerAttribute(MetaAttribute.INSTANCE);

        Rpc.register(CharRpc.INSTANCE);
        Rpc.register(MetaRpc.INSTANCE);

        GameRegistry.registerItem(new ItemBattleStick(), ItemBattleStick.ITEM_NAME);
        GameRegistry.registerItem(new ItemCharSheet(), ItemCharSheet.ITEM_NAME);
        GameRegistry.registerItem(new ItemHealthController(), ItemHealthController.ITEM_NAME);
    }
}
