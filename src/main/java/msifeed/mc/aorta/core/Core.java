package msifeed.mc.aorta.core;

import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import msifeed.mc.aorta.core.commands.TraitsCommand;
import msifeed.mc.aorta.core.props.PropsHandler;
import msifeed.mc.aorta.core.things.ItemBattleTool;
import msifeed.mc.aorta.core.things.ItemCharTool;
import msifeed.mc.aorta.core.things.ItemDebugTool;
import msifeed.mc.aorta.core.traits.TraitDecoder;
import net.minecraft.command.CommandHandler;
import net.minecraftforge.common.MinecraftForge;

public class Core {
    @SidedProxy(
            serverSide = "msifeed.mc.aorta.core.CoreGuiHandler",
            clientSide = "msifeed.mc.aorta.core.client.CoreGuiHandlerClient"
    )
    public static CoreGuiHandler GUI_EXEC;

    public void init() {
        TraitDecoder.init();

        MinecraftForge.EVENT_BUS.register(new PropsHandler());

        GameRegistry.registerItem(new ItemDebugTool(), ItemDebugTool.ITEM_NAME);
        GameRegistry.registerItem(new ItemCharTool(), ItemCharTool.ITEM_NAME);
        GameRegistry.registerItem(new ItemBattleTool(), ItemBattleTool.ITEM_NAME);
    }

    public void serverStarting(FMLServerStartingEvent event) {
        registerCommands((CommandHandler) event.getServer().getCommandManager());
    }

    protected void registerCommands(CommandHandler handler) {
        handler.registerCommand(new TraitsCommand());
    }
}
