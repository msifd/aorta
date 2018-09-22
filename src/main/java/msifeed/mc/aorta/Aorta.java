package msifeed.mc.aorta;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import msifeed.mc.aorta.attributes.AttributeHandler;
import msifeed.mc.aorta.chat.Speechat;
import msifeed.mc.aorta.client.GuiHandler;
import msifeed.mc.aorta.core.Core;
import msifeed.mc.aorta.genesis.Genesis;
import msifeed.mc.aorta.tweaks.EnableDesertRain;
import msifeed.mc.aorta.tweaks.MakeEveryoneHealthy;
import msifeed.mc.aorta.tweaks.MakeFoodEdible;
import net.minecraft.command.CommandHandler;
import net.minecraft.server.MinecraftServer;

public class Aorta {
    public static final String MODID = "aorta";
    public static final String NAME = "Aorta";
    public static final String VERSION = "@VERSION@";

    @SidedProxy(
            serverSide = "msifeed.mc.aorta.core.Core",
            clientSide = "msifeed.mc.aorta.core.CoreClient"
    )
    public static Core CORE;

    @SidedProxy(
            serverSide = "msifeed.mc.aorta.genesis.Genesis",
            clientSide = "msifeed.mc.aorta.genesis.GenesisClient"
    )
    public static Genesis GENESIS;

    @SidedProxy(
            serverSide = "msifeed.mc.aorta.client.GuiHandler",
            clientSide = "msifeed.mc.aorta.client.GuiHandlerClient"
    )
    public static GuiHandler GUI_HANDLER;

    public static Speechat SPEECHAT = new Speechat();

    public void preInit() {
        CORE.preInit();
    }

    public void init() {
        AttributeHandler.INSTANCE.init();
        CORE.init();
        GENESIS.init();
        SPEECHAT.init();

        MakeEveryoneHealthy.apply();
    }

    public void postInit() {
        EnableDesertRain.apply();
        MakeFoodEdible.apply();
    }

    public void serverStarting(FMLServerStartedEvent event) {
        final MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        final CommandHandler commandHandler = (CommandHandler) server.getCommandManager();
        CORE.registerCommands(commandHandler);
        SPEECHAT.registerCommands(commandHandler);
    }
}