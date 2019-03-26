package msifeed.mc.aorta.mount;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;

public class MountEntity extends EntityLiving {
    EntityLivingBase mimicEntity = null;

    public MountEntity(World world) {
        super(world);
        isImmuneToFire = true;

        setSize(0.9F, 1.3F);
        getNavigator().setAvoidsWater(true);

        tasks.addTask(0, new EntityAISwimming(this));
//        tasks.addTask(1, new EntityAIPanic(this, 1.2D));
//        tasks.addTask(1, new EntityAIRunAroundLikeCrazy(this, 1.2D));
//        tasks.addTask(2, new EntityAIMate(this, 1.0D));
//        tasks.addTask(4, new EntityAIFollowParent(this, 1.0D));
//        tasks.addTask(6, new EntityAIWander(this, 0.7D));
        tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        tasks.addTask(8, new EntityAILookIdle(this));
    }

    @Override
    public boolean isAIEnabled() {
        return true;
    }

    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();

        if (mimicEntity == null) {
            final EntityLivingBase closest = getClosestEntity();
            if (closest != null)
                mimic(closest);
        }
    }

    private EntityLivingBase getClosestEntity() {
        double closestDist = Double.MAX_VALUE;
        EntityLivingBase result = null;

        final List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(
                this,
                this.boundingBox.addCoord(5, 5, 5),
                e -> e.isEntityAlive() && e instanceof EntityLivingBase && !(e instanceof EntityPlayer));

        for (EntityLivingBase e : (List<EntityLivingBase>) list) {
            double dist = e.getDistanceSq(this.posX, this.posY, this.posZ);

            if (dist < closestDist) {
                result = e;
                closestDist = dist;
            }
        }

        return result;
    }

    private void mimic(EntityLivingBase entity) {
        mimicEntity = entity;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(1);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.225D);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);

        if (mimicEntity != null) {
            final NBTTagCompound mimicNbt = new NBTTagCompound();
            mimicEntity.writeEntityToNBT(mimicNbt);
            compound.setTag("aorta.mount.mimic", mimicNbt);
            compound.setString("aorta.mount.mimic_class", mimicEntity.getClass().getName());
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);

        final NBTTagCompound mimicNbt = compound.getCompoundTag("aorta.mount.mimic");
        final String mimicClass = compound.getString("aorta.mount.mimic_class");
        try {
            final Class c = Class.forName(mimicClass);
            if (EntityLivingBase.class.isAssignableFrom(c)) {
                final EntityLivingBase e = (EntityLivingBase) c.newInstance();
                e.readEntityFromNBT(mimicNbt);
                mimic(e);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public boolean interact(EntityPlayer player) {
        final ItemStack itemstack = player.inventory.getCurrentItem();
        if (itemstack != null && itemstack.interactWithEntity(player, this)) {
            return true;
        } else if (riddenByEntity == null && !player.isSneaking()) {
            mountBy(player);
            return true;
        } else {
            return super.interact(player);
        }
    }

    private void mountBy(EntityPlayer player) {
        player.rotationYaw = this.rotationYaw;
        player.rotationPitch = this.rotationPitch;

        if (!this.worldObj.isRemote)
            player.mountEntity(this);
    }

    @Override
    public void moveEntityWithHeading(float strafing, float forwarding) {
        if (this.riddenByEntity != null) {
            this.prevRotationYaw = this.rotationYaw = this.riddenByEntity.rotationYaw;
            this.rotationPitch = this.riddenByEntity.rotationPitch * 0.5F;
            this.setRotation(this.rotationYaw, this.rotationPitch);
            this.rotationYawHead = this.renderYawOffset = this.rotationYaw;
            strafing = ((EntityLivingBase) this.riddenByEntity).moveStrafing * 0.5F;
            forwarding = ((EntityLivingBase) this.riddenByEntity).moveForward;

            if (forwarding <= 0.0F) {
                forwarding *= 0.25F;
                //            this.field_110285_bP = 0;
            }

            //        if (this.onGround && this.jumpPower == 0.0F && this.isRearing() && !this.field_110294_bI)
            //        {
            //            strafing = 0.0F;
            //            forwarding = 0.0F;
            //        }

            //        if (this.jumpPower > 0.0F && !this.isHorseJumping() && this.onGround)
            //        {
            //            this.motionY = this.getHorseJumpStrength() * (double)this.jumpPower;
            //
            //            if (this.isPotionActive(Potion.jump))
            //            {
            //                this.motionY += (double)((float)(this.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);
            //            }
            //
            //            this.setHorseJumping(true);
            //            this.isAirBorne = true;
            //
            //            if (forwarding > 0.0F)
            //            {
            //                float f2 = MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F);
            //                float f3 = MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F);
            //                this.motionX += (double)(-0.4F * f2 * this.jumpPower);
            //                this.motionZ += (double)(0.4F * f3 * this.jumpPower);
            //                this.playSound("mob.horse.jump", 0.4F, 1.0F);
            //            }
            //
            //            this.jumpPower = 0.0F;
            //            net.minecraftforge.common.ForgeHooks.onLivingJump(this);
            //        }

            this.stepHeight = 1.0F;
            this.jumpMovementFactor = this.getAIMoveSpeed() * 0.1F;

            if (!this.worldObj.isRemote) {
                this.setAIMoveSpeed((float) this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue());
                super.moveEntityWithHeading(strafing, forwarding);
            }

            //        if (this.onGround)
            //        {
            //            this.jumpPower = 0.0F;
            //            this.setHorseJumping(false);
            //        }

            this.prevLimbSwingAmount = this.limbSwingAmount;
            double d1 = this.posX - this.prevPosX;
            double d0 = this.posZ - this.prevPosZ;
            float f4 = MathHelper.sqrt_double(d1 * d1 + d0 * d0) * 4.0F;

            if (f4 > 1.0F) {
                f4 = 1.0F;
            }

            this.limbSwingAmount += (f4 - this.limbSwingAmount) * 0.4F;
            this.limbSwing += this.limbSwingAmount;
        } else {
            this.stepHeight = 0.5F;
            this.jumpMovementFactor = 0.02F;
            super.moveEntityWithHeading(strafing, forwarding);
        }
    }
}
