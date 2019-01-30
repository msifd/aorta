package msifeed.mc.aorta;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;

@Mod(modid = Aorta.MODID, name = Aorta.NAME, version = Aorta.VERSION)
public class Bootstrap {
    @Mod.Instance
    public static Bootstrap INSTANCE;

    @SidedProxy(
            serverSide = "msifeed.mc.aorta.Aorta",
            clientSide = "msifeed.mc.aorta.AortaClient"
    )
    public static Aorta AORTA;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        AORTA.preInit(event);
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
