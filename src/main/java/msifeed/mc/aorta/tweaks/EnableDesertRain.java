package msifeed.mc.aorta.tweaks;

import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.world.biome.*;

public class EnableDesertRain {
    public static void apply() {
        final String rainField = (Boolean) Launch.blackboard.getOrDefault("fml.deobfuscatedEnvironment", false)
                ? "enableRain" : "field_76765_S";
        for (BiomeGenBase biome : BiomeGenBase.getBiomeGenArray()) {
            if (biome != null && !isNoRainBiome(biome)) {
                ReflectionHelper.setPrivateValue(BiomeGenBase.class, biome, true, rainField);
                biome.rainfall = 0.4f; // Plains value
            }
        }
    }

    private static boolean isNoRainBiome(BiomeGenBase biome) {
        return biome instanceof BiomeGenHell || biome instanceof BiomeGenEnd;
    }
}
