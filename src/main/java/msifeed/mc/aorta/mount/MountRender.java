package msifeed.mc.aorta.mount;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBoat;
import net.minecraft.client.model.ModelCow;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class MountRender extends RenderLiving {
    private static final ResourceLocation texture = new ResourceLocation("textures/entity/cow/cow.png");



    public MountRender() {
        super(new ModelCow(), 1);
//        setRenderPassModel(mainModel);
    }

    public static void init() {
        RenderingRegistry.registerEntityRenderingHandler(MountEntity.class, new MountRender());
    }

    @Override
    public void doRender(EntityLivingBase entity, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
        final MountEntity mount = (MountEntity) entity;
        final Render render = RenderManager.instance.getEntityRenderObject(mount.mimicEntity);

        if (render instanceof RenderLiving) {
            final RenderLiving renderLiving = (RenderLiving) render;
            ((RenderLiving) render).doRender(entity, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
        } else {
            super.doRender(entity, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
        }
    }

    @Override
    public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
        super.doRender(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
        return texture;
    }
}
