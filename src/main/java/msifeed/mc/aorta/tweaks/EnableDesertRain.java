package msifeed.mc.aorta.tweaks;

import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenDesert;
import net.minecraft.world.biome.BiomeGenSavanna;

public class EnableDesertRain {
    public static void apply() {
        final String rainField = (Boolean) Launch.blackboard.getOrDefault("fml.deobfuscatedEnvironment", false)
                ? "enableRain" : "field_76765_S";
        for (BiomeGenBase biome : BiomeGenBase.getBiomeGenArray()) {
            if (isDesertBiome(biome)) {
                ReflectionHelper.setPrivateValue(BiomeGenBase.class, biome, true, rainField);
                biome.rainfall = 0.4f; // Plains value
            }
        }
    }

    private static boolean isDesertBiome(BiomeGenBase biome) {
        return biome instanceof BiomeGenDesert || biome instanceof BiomeGenSavanna;
    }
}
