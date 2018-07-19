package msifeed.mc.aorta;

import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import msifeed.mc.aorta.chat.Speechat;
import msifeed.mc.aorta.core.Core;
import msifeed.mc.aorta.genesis.Genesis;
import msifeed.mc.aorta.network.Networking;
import msifeed.mc.aorta.tweaks.EnableDesertRain;

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
        CORE.init();
        GENESIS.generate();
        SPEECHAT.init();

        Networking.init();
    }

    public void postInit() {
        EnableDesertRain.apply();
    }

    public void serverStarting(FMLServerStartingEvent event) {
        CORE.serverStarting(event);
    }
}