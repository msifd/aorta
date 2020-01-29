package msifeed.mc;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import msifeed.mc.aorta.Aorta;
import msifeed.mc.sys.attributes.AttributeHandler;
import msifeed.mc.sys.config.ConfigManager;
import msifeed.mc.sys.rpc.Rpc;

@Mod(modid = Bootstrap.MODID, name = Bootstrap.NAME, version = Bootstrap.VERSION)
public class Bootstrap {
    public static final String MODID = "more";
    public static final String NAME = "More";
    public static final String VERSION = "@VERSION@";

    @Mod.Instance
    public static Bootstrap INSTANCE;

    @SidedProxy(
            serverSide = "msifeed.mc.aorta.Aorta",
            clientSide = "msifeed.mc.aorta.AortaClient"
    )
    public static Aorta AORTA;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        AttributeHandler.init();
        Rpc.init();

        AORTA.preInit(event);

        ConfigManager.init(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        AORTA.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        AORTA.postInit();
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        AORTA.serverStarting(event);
    }

    @Mod.EventHandler
    public void serverStopping(FMLServerStoppingEvent event) {
        AORTA.serverStopping(event);
    }
}
