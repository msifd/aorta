package msifeed.mc.aorta;

import cpw.mods.fml.common.SidedProxy;
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

    public void init() {
        Networking.INSTANCE.init();
        CORE.init();
        GENESIS.generate();

        EnableDesertRain.apply();
    }
}