package msifeed.mc.aorta.core;

import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.registry.GameRegistry;
import msifeed.mc.aorta.core.chat.ChatHandler;
import msifeed.mc.aorta.core.props.CharacterProperty;
import msifeed.mc.aorta.core.things.ItemBattleTool;
import msifeed.mc.aorta.core.things.ItemCharTool;
import msifeed.mc.aorta.core.things.ItemDebugTool;
import net.minecraftforge.common.MinecraftForge;

public class Core {
    @SidedProxy(
            serverSide = "msifeed.mc.aorta.core.CoreGuiHandler",
            clientSide = "msifeed.mc.aorta.core.client.CoreGuiHandlerClient"
    )
    public static CoreGuiHandler GUI_EXEC;

    public void init() {
        MinecraftForge.EVENT_BUS.register(new CharacterProperty.Handler());
        MinecraftForge.EVENT_BUS.register(new ChatHandler());

        GameRegistry.registerItem(new ItemDebugTool(), ItemDebugTool.ITEM_NAME);
        GameRegistry.registerItem(new ItemCharTool(), ItemCharTool.ITEM_NAME);
        GameRegistry.registerItem(new ItemBattleTool(), ItemBattleTool.ITEM_NAME);
    }
}
