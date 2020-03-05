package msifeed.mc.more.client.combat.other;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class DamageParticle extends EntityFX {
    private final float damage;

    public DamageParticle(World world, double x, double y, double z, float damage) {
        super(world, x, y, z);

        this.motionY += 0.2;
        this.particleMaxAge = 20;
        this.noClip = true;

        this.damage = damage;
    }

    @Override
    public void renderParticle(Tessellator tess, float x, float y, float z, float p_70539_5_, float p_70539_6_, float p_70539_7_) {
//        super.renderParticle(tess, p_70539_2_, p_70539_3_, p_70539_4_, p_70539_5_, p_70539_6_, p_70539_7_);

        final Minecraft mc = Minecraft.getMinecraft();
        final FontRenderer fr = mc.fontRenderer;
        final RenderManager rm = RenderManager.instance;

        GL11.glPushMatrix();

        final double f11 = this.prevPosX + (this.posX - this.prevPosX) * (double)x - interpPosX;
        final double f12 = this.prevPosY + (this.posY - this.prevPosY) * (double)y - interpPosY;
        final double f13 = this.prevPosZ + (this.posZ - this.prevPosZ) * (double)z - interpPosZ;

        GL11.glTranslated(f11, f12, f13);
        GL11.glRotatef(-rm.playerViewY, 0.0F, 1.0F, 0.0F);
        final float f1 = 0.08f;
        GL11.glScalef(-f1, -f1, f1);

        fr.drawString(String.valueOf(this.damage), 0, 0, 0xff1111);

        GL11.glPopMatrix();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
    }
}
