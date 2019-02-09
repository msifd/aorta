package msifeed.mc.aorta.tweaks;

import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenEnd;
import net.minecraft.world.biome.BiomeGenHell;

public class EnableDesertRain {
    public static void apply() {
        final String rainField = (Boolean) Launch.blackboard.getOrDefault("fml.deobfuscatedEnvironment", false)
                ? "enableRain" : "field_76765_S";
        for (BiomeGenBase biome : BiomeGenBase.getBiomeGenArray()) {
            if (biome != null && !isSpecialBiome(biome) && biome.rainfall == 0) {
                ReflectionHelper.setPrivateValue(BiomeGenBase.class, biome, true, rainField);
//                biome.rainfall = 0.4f; // Plains value
                biome.setTemperatureRainfall(0.8f, 0.4f); // Plains value
            }
        }

    }

    private static boolean isSpecialBiome(BiomeGenBase biome) {
        return biome instanceof BiomeGenHell || biome instanceof BiomeGenEnd;
    }
}
