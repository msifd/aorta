package msifeed.mc.aorta.weather;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import msifeed.mc.aorta.weather.client.WeatherRenderer;
import net.minecraft.block.Block;
import net.minecraft.command.CommandHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenEnd;
import net.minecraft.world.biome.BiomeGenHell;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;

import java.util.HashMap;
import java.util.List;

public enum WeatherManager {
    INSTANCE;

    private HashMap<Integer, WeatherStatus> statuses = new HashMap<>();
    private WeatherRenderer weatherRenderer = new WeatherRenderer();

    public static void init() {
        FMLCommonHandler.instance().bus().register(INSTANCE);
        MinecraftForge.EVENT_BUS.register(INSTANCE.weatherRenderer);
    }

    public static void registerCommands(CommandHandler commandHandler) {
        commandHandler.registerCommand(new WeatherCommand());
    }

    public boolean isSnowing(int dim) {
        final WeatherStatus s = statuses.get(dim);
        return s != null && s.isSnowing();
    }

    public void toggleSnow(World world) {
        final int dim = world.provider.dimensionId;
        final WeatherStatus weatherStatus = getStatus(dim);
        weatherStatus.snowfall = weatherStatus.isSnowing() ? 0 : 1;
    }

    public void toggleWinter(World world) {
        final int dim = world.provider.dimensionId;
        final WeatherStatus weatherStatus = getStatus(dim);

        weatherStatus.winter = !weatherStatus.winter;
        world.playerEntities.forEach(o -> {
            ((EntityPlayer) o).addChatMessage(new ChatComponentText("winter: " + weatherStatus.winter));
        });
    }

    private WeatherStatus getStatus(int dim) {
        if (!statuses.containsKey(dim))
            statuses.put(dim, new WeatherStatus());
        return statuses.get(dim);
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.side != Side.SERVER || event.phase != TickEvent.Phase.START)
            return;

        final WorldServer world = (WorldServer) event.world;
        final WeatherStatus weatherStatus = statuses.get(world.provider.dimensionId);
        if (weatherStatus == null)
            return;
        final boolean winter = weatherStatus.winter;

        final List<Chunk> chunks = world.theChunkProviderServer.loadedChunks;
        if (chunks.isEmpty())
            return;

        final int baseRate = 16;
        final float skylightRate = winter ? 1 : 0.5f + (7 - world.skylightSubtracted) / 14f;
        final int passes = (int) (baseRate * skylightRate) + chunks.size() / 64;
        for (int i = 0; i < passes; ++i) {
            final Chunk c = chunks.get(world.rand.nextInt(chunks.size()));
            final int x = world.rand.nextInt(16);
            final int z = world.rand.nextInt(16);
            final int y = c.getPrecipitationHeight(x, z);

            final int xReal = c.xPosition * 16 + x;
            final int zReal = c.zPosition * 16 + z;

            if (winter) {
                world.setBlock(xReal, y, zReal, Blocks.snow_layer);
            } else {
                final Block b = c.getBlock(x, y, z);
                if (b == Blocks.snow_layer)
                    world.setBlockToAir(xReal, y, zReal);
            }
        }
    }

    private static boolean isRegularBiome(BiomeGenBase biome) {
        return !(biome instanceof BiomeGenHell || biome instanceof BiomeGenEnd);
    }
}
