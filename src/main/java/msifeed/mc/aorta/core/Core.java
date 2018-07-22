package msifeed.mc.aorta.core;

import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.registry.GameRegistry;
import msifeed.mc.aorta.attributes.AttributeHandler;
import msifeed.mc.aorta.core.commands.TraitsCommand;
import msifeed.mc.aorta.core.props.CharacterAttribute;
import msifeed.mc.aorta.core.props.TraitsAttribute;
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

    public void init() {
        AttributeHandler.INSTANCE.registerAttribute(CharacterAttribute.INSTANCE);
        AttributeHandler.INSTANCE.registerAttribute(TraitsAttribute.INSTANCE);

        TraitDecoder.init();

        GameRegistry.registerItem(new ItemDebugTool(), ItemDebugTool.ITEM_NAME);
        GameRegistry.registerItem(new ItemCharTool(), ItemCharTool.ITEM_NAME);
        GameRegistry.registerItem(new ItemBattleTool(), ItemBattleTool.ITEM_NAME);
    }

    public void registerCommands(CommandHandler handler) {
        handler.registerCommand(new TraitsCommand());
    }
}
