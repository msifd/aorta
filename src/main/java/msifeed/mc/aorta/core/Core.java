package msifeed.mc.aorta.core;

import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.registry.GameRegistry;
import msifeed.mc.aorta.attributes.AttributeHandler;
import msifeed.mc.aorta.core.attributes.CharacterAttribute;
import msifeed.mc.aorta.core.attributes.StatusAttribute;
import msifeed.mc.aorta.core.commands.TraitListCommand;
import msifeed.mc.aorta.core.commands.TraitSetCommand;
import msifeed.mc.aorta.core.defines.CoreDefines;
import msifeed.mc.aorta.core.defines.DefinesProvider;
import msifeed.mc.aorta.core.meta.MetaCommand;
import msifeed.mc.aorta.core.things.ItemBattleTool;
import msifeed.mc.aorta.core.things.ItemCharTool;
import msifeed.mc.aorta.core.things.ItemDebugTool;
import msifeed.mc.aorta.core.traits.TraitDecoder;
import net.minecraft.command.CommandHandler;

public class Core {
    @SidedProxy(
            serverSide = "msifeed.mc.aorta.core.CoreGuiHandler",
            clientSide = "msifeed.mc.aorta.core.client.CoreGuiHandlerClient"
    )
    public static CoreGuiHandler GUI_EXEC;
    public static CoreDefines DEFINES;

    public void preInit() {
        DEFINES = DefinesProvider.load();
    }

    public void init() {
        AttributeHandler.INSTANCE.registerAttribute(CharacterAttribute.INSTANCE);
        AttributeHandler.INSTANCE.registerAttribute(StatusAttribute.INSTANCE);

        TraitDecoder.init();

        GameRegistry.registerItem(new ItemDebugTool(), ItemDebugTool.ITEM_NAME);
        GameRegistry.registerItem(new ItemCharTool(), ItemCharTool.ITEM_NAME);
        GameRegistry.registerItem(new ItemBattleTool(), ItemBattleTool.ITEM_NAME);
    }

    public void registerCommands(CommandHandler handler) {
        handler.registerCommand(new TraitListCommand());
        handler.registerCommand(new TraitSetCommand());
        handler.registerCommand(new MetaCommand());
    }
}
