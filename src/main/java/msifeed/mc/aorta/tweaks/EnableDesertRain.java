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
        final String rainfallField = (Boolean) Launch.blackboard.getOrDefault("fml.deobfuscatedEnvironment", false)
                ? "rainfall" : "field_76751_G";

        final String listField = (Boolean) Launch.blackboard.getOrDefault("fml.deobfuscatedEnvironment", false)
                ? "biomeList" : "field_76773_a";
        final BiomeGenBase[] biomes = ReflectionHelper.getPrivateValue(BiomeGenBase.class, null, listField);

        for (BiomeGenBase biome : biomes) {
            if (biome == null) continue;

            final float rainfall = ReflectionHelper.getPrivateValue(BiomeGenBase.class, biome, rainfallField);
            if (!isSpecialBiome(biome) && rainfall == 0) {
                ReflectionHelper.setPrivateValue(BiomeGenBase.class, biome, true, rainField);
                ReflectionHelper.setPrivateValue(BiomeGenBase.class, biome, 0.4f, rainfallField);

//                biome.rainfall = 0.4f; // Plains value
                biome.setTemperatureRainfall(0.8f, 0.4f); // Plains value
            }
        }
    }

    private static boolean isSpecialBiome(BiomeGenBase biome) {
        return biome instanceof BiomeGenHell || biome instanceof BiomeGenEnd;
    }
}
