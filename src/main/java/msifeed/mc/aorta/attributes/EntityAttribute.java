package msifeed.mc.aorta.attributes;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

import java.util.Optional;
import java.util.function.Consumer;

public abstract class EntityAttribute<T> {
    public abstract String getName();

    public abstract T init(Entity entity, World world, T currentValue);

    public abstract void saveNBTData(T value, NBTTagCompound root);

    public abstract T loadNBTData(NBTTagCompound root);

    public Optional<T> get(Entity entity) {
        final AttrProp<T> attr = getProp(entity);
        if (attr == null)
            return Optional.empty();
        return Optional.ofNullable(attr.value);
    }

    public final void set(Entity entity, T value) {
        final AttrProp<T> attr = getProp(entity);
        if (attr != null) {
            attr.value = value;
            sync(entity);
        }
    }

    public final void update(Entity entity, Consumer<T> fn) {
        final AttrProp<T> attr = getProp(entity);
        if (attr != null) {
            fn.accept(attr.value);
        }
    }

    public void onEntityConstruct(EntityEvent.EntityConstructing event) {
    }

    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
    }

    public void onPlayerStartedTracking(PlayerEvent.StartTracking event) {
    }

    public void sync(EntityPlayerMP playerMP, Entity entity) {
        final SyncAttrMessage msg = new SyncAttrMessage(entity, this);
        AttributeHandler.INSTANCE.CHANNEL.sendTo(msg, playerMP);
    }

    public void sync(Entity entity) {
        if (FMLCommonHandler.instance().getEffectiveSide().isClient())
            syncServer(entity);
        else
            sync(entity.worldObj, entity);
    }

    public void sync(World world, Entity entity) {
        if (!(world instanceof WorldServer))
            return;

        final SyncAttrMessage msg = new SyncAttrMessage(entity, this);
        final EntityTracker tracker = ((WorldServer) world).getEntityTracker();
        if (tracker != null) {
            for (EntityPlayer player : tracker.getTrackingPlayers(entity)) {
                AttributeHandler.INSTANCE.CHANNEL.sendTo(msg, (EntityPlayerMP) player);
            }
        }
        if (entity instanceof EntityPlayerMP) {
            AttributeHandler.INSTANCE.CHANNEL.sendTo(msg, (EntityPlayerMP) entity);
        }
    }

    public void syncServer(Entity e) {
        final SyncAttrMessage msg = new SyncAttrMessage(e, this);
        AttributeHandler.INSTANCE.CHANNEL.sendToServer(msg);
    }

    void toNBT(Entity entity, NBTTagCompound root) {
        final AttrProp<T> attr = getProp(entity);
        if (attr != null)
            attr.saveNBTData(root);
    }

    void fromNBT(Entity entity, NBTTagCompound root) {
        final AttrProp<T> attr = getProp(entity);
        if (attr != null)
            attr.loadNBTData(root);
    }

    private AttrProp<T> getProp(Entity entity) {
        return (AttrProp<T>) entity.getExtendedProperties(getName());
    }
}
