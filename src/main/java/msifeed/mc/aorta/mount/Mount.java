package msifeed.mc.aorta.mount;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.Bootstrap;
import net.minecraft.entity.EntityList;

public class Mount {
    public static void init() {
//        final int id = EntityRegistry.findGlobalUniqueEntityId();
        EntityRegistry.registerModEntity(MountEntity.class, "mount", 0, Bootstrap.INSTANCE, 80, 3, true);
        GameRegistry.registerItem(new MountEggItem(), "mountegg");

//        final int id = EntityList.getEntityID(new MountEntity(null));
//        final int id = EntityRegistry.instance().lookupModSpawn(MountEntity.class, false).getModEntityId();
//        EntityList.entityEggs.put(id, new EntityList.EntityEggInfo(id, 0xff00ff, 0x00ff00));

        if (FMLCommonHandler.instance().getSide().isClient()) {
            MountRender.init();
        }
    }
}
