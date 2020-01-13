package msifeed.mc.aorta;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import msifeed.mc.aorta.client.GuiHandler;
import msifeed.mc.aorta.core.Core;
import msifeed.mc.aorta.core.commands.DumpRollsCommand;
import msifeed.mc.aorta.core.commands.RollCommand;
import msifeed.mc.aorta.tools.AortaCommand;
import msifeed.mc.aorta.tools.ToolItems;
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
import msifeed.mc.extensions.tweaks.*;
import msifeed.mc.genesis.Genesis;
import msifeed.mc.sys.config.ConfigManager;
import net.minecraft.command.CommandHandler;
import net.minecraft.server.MinecraftServer;

public class Aorta {
    @SidedProxy(
            serverSide = "msifeed.mc.genesis.Genesis",
            clientSide = "msifeed.mc.genesis.GenesisClient"
    )
    private static Genesis genesis;
    @SidedProxy(
            serverSide = "msifeed.mc.aorta.client.GuiHandler",
            clientSide = "msifeed.mc.aorta.client.GuiHandlerClient"
    )
    public static GuiHandler GUI_HANDLER;
    public static Defines DEFINES = new Defines();

    private ExternalLogs externalLogs = new ExternalLogs();
    private EntityControl entityControl = new EntityControl();

    public void preInit(FMLPreInitializationEvent event) {
        genesis.init();
        EnableDesertRain.apply();
    }

    public void init() {
        externalLogs.init();
        Core.init();

        EnvironmentManager.init();
        entityControl.init();
        RenameRpc.init();
        ToolItems.init();
        RemoteBookManager.init();
        Locks.init();
        Speechat.init();
        Nametag.INSTANCE.init();
        MakeEveryoneHealthy.apply();
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
        handler.registerCommand(new AortaCommand());
        handler.registerCommand(new RenameCommand());
        handler.registerCommand(new UnstuckCommand());
        handler.registerCommand(new DumpRollsCommand());
        handler.registerCommand(new ItemMetaCommand());
        handler.registerCommand(new RollCommand());

        Speechat.registerCommands(handler);
        EnvironmentManager.registerCommands(handler);
    }

    public void serverStopping(FMLServerStoppingEvent event) {
        ConfigManager.save();
    }
}