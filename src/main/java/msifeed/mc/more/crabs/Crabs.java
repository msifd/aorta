package msifeed.mc.more.crabs;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import msifeed.mc.more.crabs.character.CharRpc;
import msifeed.mc.more.crabs.combat.CombatManager;
import msifeed.mc.more.crabs.combat.CombatRpc;
import msifeed.mc.more.crabs.meta.MetaRpc;
import msifeed.mc.more.tools.ItemBattleStick;
import msifeed.mc.more.tools.ItemCharSheet;
import msifeed.mc.more.tools.ItemHealthController;
import msifeed.mc.sys.rpc.Rpc;
import net.minecraftforge.common.MinecraftForge;

public class Crabs {
    private CombatManager combatManager = new CombatManager();

    public void preInit(FMLPreInitializationEvent event) {
//        ActionProvider.INSTANCE.preInit();
    }

    public void init() {
//        AttributeHandler.INSTANCE.registerAttribute(CharacterAttribute.INSTANCE);
//        AttributeHandler.INSTANCE.registerAttribute(MetaAttribute.INSTANCE);

        MinecraftForge.EVENT_BUS.register(combatManager);

        Rpc.register(CharRpc.INSTANCE);
        Rpc.register(MetaRpc.INSTANCE);
        Rpc.register(CombatRpc.INSTANCE);

//        ContextManager.INSTANCE.onInit();
//        ActionManager.INSTANCE.onInit();
//        FightManager.INSTANCE.onInit();

        GameRegistry.registerItem(new ItemBattleStick(), ItemBattleStick.ITEM_NAME);
        GameRegistry.registerItem(new ItemCharSheet(), ItemCharSheet.ITEM_NAME);
        GameRegistry.registerItem(new ItemHealthController(), ItemHealthController.ITEM_NAME);
    }
}
