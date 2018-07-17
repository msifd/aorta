package msifeed.mc.aorta;

import cpw.mods.fml.common.SidedProxy;
import msifeed.mc.aorta.chat.ChatHandler;
import msifeed.mc.aorta.chat.Speechat;
import msifeed.mc.aorta.core.Core;
import msifeed.mc.aorta.genesis.Genesis;
import msifeed.mc.aorta.network.Networking;
import msifeed.mc.aorta.tweaks.EnableDesertRain;
import net.minecraftforge.common.MinecraftForge;

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

        EnableDesertRain.apply();

        Networking.INSTANCE.init();
    }
}