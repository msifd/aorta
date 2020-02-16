package msifeed.mc.more;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import msifeed.mc.commons.defines.DefineCommand;
import msifeed.mc.commons.defines.Defines;
import msifeed.mc.commons.logs.ExternalLogs;
import msifeed.mc.commons.traits.TraitListCommand;
import msifeed.mc.commons.traits.TraitSetCommand;
import msifeed.mc.extensions.books.RemoteBookManager;
import msifeed.mc.extensions.chat.Speechat;
import msifeed.mc.extensions.environment.EnvironmentManager;
import msifeed.mc.extensions.itemmeta.ItemMetaCommand;
import msifeed.mc.extensions.locks.Locks;
import msifeed.mc.extensions.nametag.Nametag;
import msifeed.mc.extensions.rename.RenameCommand;
import msifeed.mc.extensions.rename.RenameRpc;
import msifeed.mc.extensions.tweaks.EntityControl;
import msifeed.mc.extensions.tweaks.MakeFoodEdible;
import msifeed.mc.extensions.tweaks.UnstuckCommand;
import msifeed.mc.genesis.Genesis;
import msifeed.mc.more.client.common.GuiHandler;
import msifeed.mc.more.commands.MoreCommand;
import msifeed.mc.more.commands.RollCommand;
import msifeed.mc.more.crabs.Crabs;
import msifeed.mc.more.tools.ToolItems;
import net.minecraft.command.CommandHandler;
import net.minecraft.server.MinecraftServer;

public class More {
    @SidedProxy(
            serverSide = "msifeed.mc.genesis.Genesis",
            clientSide = "msifeed.mc.genesis.GenesisClient"
    )
    private static Genesis genesis;
    @SidedProxy(
            serverSide = "msifeed.mc.more.client.common.GuiHandler",
            clientSide = "msifeed.mc.more.client.common.GuiHandlerClient"
    )
    public static GuiHandler GUI_HANDLER;
    public static Defines DEFINES = new Defines();

    private Crabs crabs = new Crabs();
    private ExternalLogs externalLogs = new ExternalLogs();
    private EntityControl entityControl = new EntityControl();
    private Speechat speechat = new Speechat();
    private Locks locks = new Locks();

    public void preInit() {
    }

    public void init() {
        genesis.init();

        crabs.init();
        externalLogs.init();

        entityControl.init();
        speechat.init();
        locks.init();

        EnvironmentManager.init();
        RemoteBookManager.init();
        Nametag.init();
        RenameRpc.init();

        ToolItems.init();
//        GameRegistry.registerItem(new ItemDebugTool(), ItemDebugTool.ITEM_NAME);
//        GameRegistry.registerItem(new ItemCharSheet(), ItemCharSheet.ITEM_NAME);
//        GameRegistry.registerItem(new ItemStatusTool(), ItemStatusTool.ITEM_NAME);
//        GameRegistry.registerItem(new ItemRollerTool(), ItemRollerTool.ITEM_NAME);
//        GameRegistry.registerItem(new ItemDesignerTool(), ItemDesignerTool.ID);
    }

    public void postInit() {
        MakeFoodEdible.apply();
    }

    public void serverStarting(FMLServerStartingEvent event) {
        final MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        final CommandHandler handler = (CommandHandler) server.getCommandManager();

        handler.registerCommand(new DefineCommand());
        handler.registerCommand(new TraitListCommand());
        handler.registerCommand(new TraitSetCommand());
        handler.registerCommand(new MoreCommand());
        handler.registerCommand(new RenameCommand());
        handler.registerCommand(new UnstuckCommand());
        handler.registerCommand(new ItemMetaCommand());
        handler.registerCommand(new RollCommand());

        Speechat.registerCommands(handler);
        EnvironmentManager.registerCommands(handler);
    }
}
