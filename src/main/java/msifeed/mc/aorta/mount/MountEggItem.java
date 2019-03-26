package msifeed.mc.aorta.mount;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class MountEggItem extends Item {
    @SideOnly(Side.CLIENT)
    private IIcon theIcon;

    public MountEggItem() {
        setUnlocalizedName("mountegg");
        setTextureName("spawn_egg");
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (world.isRemote)
            return itemStack;

        final MovingObjectPosition pos = this.getMovingObjectPositionFromPlayer(world, player, true);
        if (pos == null)
            return itemStack;

        final MountEntity entity = new MountEntity(world);
        entity.setLocationAndAngles(pos.blockX, pos.blockY + 1, pos.blockZ, MathHelper.wrapAngleTo180_float(world.rand.nextFloat() * 360.0F), 0.0F);
        entity.rotationYawHead = entity.rotationYaw;
        entity.renderYawOffset = entity.rotationYaw;
        entity.onSpawnWithEgg(null);
        world.spawnEntityInWorld(entity);
        entity.playLivingSound();

        return itemStack;
    }

    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(int damage, int pass) {
        return pass > 0 ? this.theIcon : super.getIconFromDamageForRenderPass(damage, pass);
    }

    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack itemStack, int pass) {
        return pass == 0 ? 0xff00ff : 0x00ff00;
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister p_94581_1_) {
        super.registerIcons(p_94581_1_);
        this.theIcon = p_94581_1_.registerIcon(this.getIconString() + "_overlay");
    }
}
