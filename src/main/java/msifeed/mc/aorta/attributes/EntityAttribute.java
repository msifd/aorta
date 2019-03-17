package msifeed.mc.aorta.attributes;

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

    public Optional<T> getValue(Entity entity) {
        final AttrProp<T> attr = getProp(entity);
        if (attr == null)
            return Optional.empty();
        return Optional.ofNullable(attr.value);
    }

    public final void set(Entity entity, T value) {
        final AttrProp<T> attr = getProp(entity);
        if (attr != null) {
            attr.value = value;
            broadcast(entity.worldObj, entity);
        }
    }

    public final void update(Entity entity, Consumer<T> fn) {
        final AttrProp<T> attr = getProp(entity);
        if (attr != null) {
            fn.accept(attr.value);
            broadcast(entity.worldObj, entity);
        }
    }

    public void onEntityConstruct(EntityEvent.EntityConstructing event) {
    }

    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
    }

    public void onPlayerStartedTracking(PlayerEvent.StartTracking event) {
    }

    public void onClonePlayer(PlayerEvent.Clone event) {
        if (event.wasDeath) {
            final NBTTagCompound compound = new NBTTagCompound();
            toNBT(event.original, compound);
            fromNBT(event.entityPlayer, compound);
        }
    }

    public void broadcast(EntityPlayerMP playerMP, Entity entity) {
        final SyncAttrMessage msg = new SyncAttrMessage(entity, this);
        AttributeHandler.INSTANCE.CHANNEL.sendTo(msg, playerMP);
    }

    public void broadcast(World world, Entity entity) {
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
