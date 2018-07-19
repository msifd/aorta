package msifeed.mc.aorta.tweaks;

import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenDesert;

public class EnableDesertRain {
    public static void apply() {
        final String rainField = (Boolean) Launch.blackboard.getOrDefault("fml.deobfuscatedEnvironment", false)
                ? "enableRain" : "field_76765_S";
        for (BiomeGenBase biome : BiomeGenBase.getBiomeGenArray())
            if (biome instanceof BiomeGenDesert)
                ReflectionHelper.setPrivateValue(BiomeGenBase.class, biome, true, rainField);
    }
}
