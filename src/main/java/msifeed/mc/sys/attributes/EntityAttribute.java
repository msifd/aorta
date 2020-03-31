package msifeed.mc.sys.attributes;

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
    public abstract T loadNBTData(T value, NBTTagCompound root);

    public boolean shouldSync(T value) {
        return true;
    }

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
            if (shouldSync(attr.value))
                broadcast(entity.worldObj, entity);
        }
    }

    public final void update(Entity entity, Consumer<T> fn) {
        final AttrProp<T> attr = getProp(entity);
        if (attr != null) {
            fn.accept(attr.value);
            if (shouldSync(attr.value))
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
            final NBTTagCompound nbt = toNBT(event.original);
            if (nbt != null)
                loadNBT(event.entityPlayer, nbt);
            else
                remove(event.entityPlayer);
        }
    }

    protected void broadcast(EntityPlayerMP playerMP, Entity entity) {
        if (playerMP.worldObj.isRemote)
            return;
        AttributeHandler.CHANNEL.sendTo(SyncAttrMessage.create(entity, this), playerMP);
    }

    protected void broadcast(World world, Entity entity) {
        if (world.isRemote || !(world instanceof WorldServer))
            return;

        final SyncAttrMessage msg = SyncAttrMessage.create(entity, this);
        final EntityTracker tracker = ((WorldServer) world).getEntityTracker();
        if (tracker != null) {
            for (EntityPlayer player : tracker.getTrackingPlayers(entity)) {
                AttributeHandler.CHANNEL.sendTo(msg, (EntityPlayerMP) player);
            }
        }
        if (entity instanceof EntityPlayerMP) {
            AttributeHandler.CHANNEL.sendTo(msg, (EntityPlayerMP) entity);
        }
    }

    NBTTagCompound toNBT(Entity entity) {
        final AttrProp<T> attr = getProp(entity);
        if (attr == null || attr.value == null)
            return null;
        final NBTTagCompound nbt = new NBTTagCompound();
        attr.saveNBTData(nbt);
        return nbt;
    }

    T loadNBT(Entity entity, NBTTagCompound root) {
        final AttrProp<T> attr = getProp(entity);
        if (attr != null) {
            attr.loadNBTData(root);
            return attr.value;
        }
        return null;
    }

    void remove(Entity entity) {
        final AttrProp<T> attr = getProp(entity);
        attr.value = null;
    }

    private AttrProp<T> getProp(Entity entity) {
        return (AttrProp<T>) entity.getExtendedProperties(getName());
    }
}
