package msifeed.mc.aorta.environment;

import msifeed.mc.aorta.Aorta;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;

public class WorldEnvMapData extends WorldSavedData {
    static final String DATA_NAME = Aorta.MODID + ".env";

    long rainAccumulated = 0;

    public WorldEnvMapData() {
        super(DATA_NAME);
    }

    public WorldEnvMapData(WorldEnv env) {
        super(DATA_NAME);
        this.rainAccumulated = env.rain.accumulated;
        this.markDirty();
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        rainAccumulated = compound.getLong("racc");
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        compound.setLong("racc", rainAccumulated);
    }

}
