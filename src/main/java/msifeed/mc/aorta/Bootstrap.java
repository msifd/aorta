package msifeed.mc.aorta;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

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
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        AORTA.init();
    }
}
