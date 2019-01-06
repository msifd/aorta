package msifeed.mc.aorta.environment.client;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import msifeed.mc.aorta.environment.EnvironmentManager;
import msifeed.mc.aorta.environment.WorldEnv;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class WeatherRenderer {
    private static final ResourceLocation locationSnowPng = new ResourceLocation("textures/environment/snow.png");
    private final float[] rainXCoords = new float[1024];
    private final float[] rainYCoords = new float[1024];
    private int rendererUpdateCount;
    private float snowStrength = 0;
    private Random random = new Random();

    public WeatherRenderer() {
        for (int i = 0; i < 32; ++i) {
            for (int j = 0; j < 32; ++j) {
                float f2 = (float)(j - 16);
                float f3 = (float)(i - 16);
                float f4 = MathHelper.sqrt_float(f2 * f2 + f3 * f3);
                this.rainXCoords[i << 5 | j] = -f3 / f4;
                this.rainYCoords[i << 5 | j] = f2 / f4;
            }
        }
    }

    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent event) {
        final Minecraft mc = Minecraft.getMinecraft();
        final WorldEnv worldEnv = EnvironmentManager.getStatus(mc.theWorld.provider.dimensionId);

        if (worldEnv.snow) {
            if (snowStrength < 1.5)
                snowStrength += 0.01f;
        } else if (snowStrength > 0) {
            snowStrength -= 0.01f;
        }
        renderSnow(event.partialTicks);

        rendererUpdateCount++;
    }

    private void renderSnow(float partialTicks)
    {
        if (snowStrength <= 0)
            return;

        final Minecraft mc = Minecraft.getMinecraft();
        final EntityRenderer er = mc.entityRenderer;
        final WorldClient world = mc.theWorld;
        final EntityLivingBase entity = mc.renderViewEntity;
        final Tessellator tessellator = Tessellator.instance;

        er.enableLightmap((double)partialTicks);

        int posX = MathHelper.floor_double(entity.posX);
        int posY = MathHelper.floor_double(entity.posY);
        int posZ = MathHelper.floor_double(entity.posZ);

        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
        double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks;
        double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks;
        double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks;
        int k = MathHelper.floor_double(d1);
        int b0 = mc.gameSettings.fancyGraphics ? 5 : 10;

        byte b1 = -1;
        float f5 = (float)rendererUpdateCount + partialTicks;

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        for (int l = posZ - b0; l <= posZ + b0; ++l) {
            for (int i1 = posX - b0; i1 <= posX + b0; ++i1) {
                int j1 = (l - posZ + 16) * 32 + i1 - posX + 16;
                float f6 = this.rainXCoords[j1] * 0.5F;
                float f7 = this.rainYCoords[j1] * 0.5F;

                int ph = world.getPrecipitationHeight(i1, l);
                int l1 = posY - b0;
                int i2 = posY + b0;

                if (l1 < ph)
                {
                    l1 = ph;
                }

                if (i2 < ph)
                {
                    i2 = ph;
                }

                float f8 = 1.0F;
                int j2 = ph;

                if (ph < k)
                {
                    j2 = k;
                }

                if (l1 != i2)
                {
                    random.setSeed((long)(i1 * i1 * 3121 + i1 * 45238971 ^ l * l * 418711 + l * 13761));
                    if (b1 != 1) {
                        b1 = 1;
                        mc.getTextureManager().bindTexture(locationSnowPng);
                        tessellator.startDrawingQuads();
                    }

                    float f10 = ((float)(rendererUpdateCount & 511) + partialTicks) / 512.0F;
                    float f16 = random.nextFloat() + f5 * 0.01F * (float)random.nextGaussian();
                    float f11 = random.nextFloat() + f5 * (float)random.nextGaussian() * 0.001F;
                    double d4 = (double)((float)i1 + 0.5F) - entity.posX;
                    double d5 = (double)((float)l + 0.5F) - entity.posZ;
                    float f14 = MathHelper.sqrt_double(d4 * d4 + d5 * d5) / (float)b0;
                    tessellator.setBrightness((world.getLightBrightnessForSkyBlocks(i1, j2, l, 0) * 3 + 15728880) / 4);
                    tessellator.setColorRGBA_F(1, 1, 1, ((1.0F - f14 * f14) * 0.3F + 0.5F) * snowStrength);
                    tessellator.setTranslation(-d0 * 1.0D, -d1 * 1.0D, -d2 * 1.0D);
                    tessellator.addVertexWithUV((double)((float)i1 - f6) + 0.5D, (double)l1, (double)((float)l - f7) + 0.5D, (double)(0.0F * f8 + f16), (double)((float)l1 * f8 / 4.0F + f10 * f8 + f11));
                    tessellator.addVertexWithUV((double)((float)i1 + f6) + 0.5D, (double)l1, (double)((float)l + f7) + 0.5D, (double)(1.0F * f8 + f16), (double)((float)l1 * f8 / 4.0F + f10 * f8 + f11));
                    tessellator.addVertexWithUV((double)((float)i1 + f6) + 0.5D, (double)i2, (double)((float)l + f7) + 0.5D, (double)(1.0F * f8 + f16), (double)((float)i2 * f8 / 4.0F + f10 * f8 + f11));
                    tessellator.addVertexWithUV((double)((float)i1 - f6) + 0.5D, (double)i2, (double)((float)l - f7) + 0.5D, (double)(0.0F * f8 + f16), (double)((float)i2 * f8 / 4.0F + f10 * f8 + f11));
                    tessellator.setTranslation(0.0D, 0.0D, 0.0D);
                }
            }
        }

        if (b1 >= 0)
            tessellator.draw();

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
        er.disableLightmap((double)partialTicks);
    }
}
