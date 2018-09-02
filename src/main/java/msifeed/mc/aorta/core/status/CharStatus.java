package msifeed.mc.aorta.core.status;

import net.minecraft.nbt.NBTTagCompound;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class CharStatus {
    public HashMap<String, Short> damage = new HashMap<>();
    public HashMap<String, Short> armor = new HashMap<>();
    public short shields;

    public NBTTagCompound toNBT() {
        final NBTTagCompound compound = new NBTTagCompound();
        compound.setByteArray(Tags.damage, toBytes(this.damage));
        compound.setByteArray(Tags.armor, toBytes(this.armor));
        compound.setShort(Tags.shields, this.shields);
        return compound;
    }

    public void fromNBT(NBTTagCompound compound) {
        damage = fromBytes(compound.getByteArray(Tags.damage));
        armor = fromBytes(compound.getByteArray(Tags.armor));
        shields = compound.getShort(Tags.shields);
    }

    private static byte[] toBytes(HashMap<String, Short> map) {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            final ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeInt(map.size());
            for (Map.Entry<String, Short> e : map.entrySet()) {
                oos.writeUTF(e.getKey());
                oos.writeShort(e.getValue());
            }
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bos.toByteArray();
    }

    private static HashMap<String, Short> fromBytes(byte[] bytes) {
        final ByteArrayInputStream bis = new ByteArrayInputStream(bytes);

        final HashMap<String, Short> map = new HashMap<>();
        try {
            final ObjectInputStream oos = new ObjectInputStream(bis);
            final int size = oos.readInt();
            for (int i = 0; i < size; i++)
                map.put(oos.readUTF(), oos.readShort());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }

    private static class Tags {
        static final String damage = "damage";
        static final String armor = "armor";
        static final String shields = "shields";
    }
}
