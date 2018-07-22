package msifeed.mc.aorta;

import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import msifeed.mc.aorta.attributes.AttributeHandler;
import msifeed.mc.aorta.chat.Speechat;
import msifeed.mc.aorta.core.Core;
import msifeed.mc.aorta.genesis.Genesis;
import msifeed.mc.aorta.tweaks.EnableDesertRain;
import net.minecraft.command.CommandHandler;

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

    public static Speechat SPEECHAT = new Speechat();

    public void init() {
        AttributeHandler.INSTANCE.init();
        CORE.init();
        GENESIS.generate();
        SPEECHAT.init();
    }

    public void postInit() {
        EnableDesertRain.apply();
    }

    public void serverStarting(FMLServerStartingEvent event) {
        final CommandHandler commandHandler = (CommandHandler) event.getServer().getCommandManager();
        CORE.registerCommands(commandHandler);
        SPEECHAT.registerCommands(commandHandler);
    }
}