package msifeed.mc.aorta.environment;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import msifeed.mc.aorta.environment.client.WeatherRenderer;
import net.minecraft.block.Block;
import net.minecraft.command.CommandHandler;
import net.minecraft.init.Blocks;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenEnd;
import net.minecraft.world.biome.BiomeGenHell;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;

import java.util.HashMap;
import java.util.List;

public enum EnvironmentManager {
    INSTANCE;

    private HashMap<Integer, WorldEnv> statuses = new HashMap<>();
    private WeatherRenderer weatherRenderer = new WeatherRenderer();

    public static void init() {
        FMLCommonHandler.instance().bus().register(INSTANCE);
        MinecraftForge.EVENT_BUS.register(INSTANCE.weatherRenderer);
    }

    public static void registerCommands(CommandHandler commandHandler) {
        commandHandler.registerCommand(new EnvironmentCommand());
    }

    public static WorldEnv getStatus(int dim) {
        WorldEnv s = INSTANCE.statuses.get(dim);
        if (s == null) {
            s = new WorldEnv();
            INSTANCE.statuses.put(dim, s);
        }
        return s;
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.side != Side.SERVER || event.phase != TickEvent.Phase.START)
            return;

        final WorldServer world = (WorldServer) event.world;
        final WorldEnv worldEnv = getStatus(world.provider.dimensionId);
        if (worldEnv.snow != worldEnv.meltSnow)
            dropOrMeltSnow(world, worldEnv);
    }

    private void dropOrMeltSnow(WorldServer world, WorldEnv worldEnv) {
        if (worldEnv.snow == worldEnv.meltSnow)
            return;

        final List<Chunk> chunks = world.theChunkProviderServer.loadedChunks;
        if (chunks.isEmpty())
            return;

        final float rate = 1 / 20f;
        final int maxStackedSnow = 7;


        final int passes = (int) (chunks.size() * rate);
//        final int baseRate = 16;
//        final int passes = baseRate + chunks.size() / 64;
        for (int i = 0; i < passes; ++i) {
            final Chunk c = chunks.get(world.rand.nextInt(chunks.size()));
            final int x = world.rand.nextInt(16);
            final int z = world.rand.nextInt(16);
            final int y = c.getPrecipitationHeight(x, z);

            final int xReal = c.xPosition * 16 + x;
            final int zReal = c.zPosition * 16 + z;

            final Block b = c.getBlock(x, y, z);
            if (worldEnv.snow) {
                if (b == Blocks.snow_layer) {
                    if (worldEnv.stackSnow) {
                        final int newMeta = world.getBlockMetadata(xReal, y, zReal) + 1;
                        if (newMeta < maxStackedSnow)
                            world.setBlockMetadataWithNotify(xReal, y, zReal, newMeta, 2);
                    }
                }
                else {
                    world.setBlock(xReal, y, zReal, Blocks.snow_layer);
                }
            }
            if (worldEnv.meltSnow && b == Blocks.snow_layer) {
                final int newMeta = world.getBlockMetadata(xReal, y, zReal) - 1;
                if (newMeta >= 0)
                    world.setBlockMetadataWithNotify(xReal, y, zReal, newMeta, 2);
                else
                    world.setBlockToAir(xReal, y, zReal);
            }
        }
    }

    private static boolean isRegularBiome(BiomeGenBase biome) {
        return !(biome instanceof BiomeGenHell || biome instanceof BiomeGenEnd);
    }
}
