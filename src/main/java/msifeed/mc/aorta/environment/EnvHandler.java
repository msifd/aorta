package msifeed.mc.aorta.environment;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.WorldInfo;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;

public class EnvHandler {
    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.phase != TickEvent.Phase.START)
            return;

        final WorldEnv worldEnv = EnvironmentManager.getEnv(event.world.provider.dimensionId);

        handleSnowdrop((WorldServer) event.world, worldEnv);

        if (FMLCommonHandler.instance().getMinecraftServerInstance().getTickCounter() % 20 == 0)
            handleTime(event.world, worldEnv);
    }

    protected void handleSnowdrop(WorldServer world, WorldEnv worldEnv) {
        if (worldEnv.snow != worldEnv.meltSnow)
            dropOrMeltSnow(world, worldEnv);
    }

    protected void dropOrMeltSnow(WorldServer world, WorldEnv worldEnv) {
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

    protected void handleTime(World world, WorldEnv env) {
        switch (env.time.mode) {
            case "real":
                setRealTime(world, env.time.offsetHours);
                break;
            case "scaled":
                setScaledTime(world, env.time.scale);
                break;
            case "fixed":
                setFixedTime(world, env.time.fixedTime);
                break;
        }
    }

    private void setRealTime(World world, int offsetHours) {
        final Duration mcTimeFix = Duration.ofHours(6);
        final LocalTime utcTime = LocalTime.now(ZoneOffset.UTC);
        final LocalTime now = utcTime.minus(mcTimeFix).plusHours(offsetHours);
        final long secs = now.toSecondOfDay();

        final long day = LocalDate.now(ZoneOffset.UTC).toEpochDay();
        final long moonPhaseTime = (day % 8) * 24000;

        final long time = (secs * 23999) / 86400;
        world.setWorldTime(time + moonPhaseTime);
    }

    private void setScaledTime(World world, double scale) {
        final WorldInfo wi = world.getWorldInfo();
        final long tt = wi.getWorldTotalTime();
        wi.setWorldTime((long) (tt * scale));
    }

    private void setFixedTime(World world, long time) {
        world.getWorldInfo().setWorldTime(time);
    }
}
