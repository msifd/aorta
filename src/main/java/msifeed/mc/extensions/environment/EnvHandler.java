package msifeed.mc.extensions.environment;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.MinecraftForge;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public class EnvHandler {
    private static final int WORLD_RESET_TIME = 8 * 24000;

    public void init() {
        FMLCommonHandler.instance().bus().register(this);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.phase != TickEvent.Phase.START)
            return;

        final WorldEnv worldEnv = EnvironmentManager.getEnv(event.world.provider.dimensionId);

        handleSnowdrop((WorldServer) event.world, worldEnv);

        if (FMLCommonHandler.instance().getMinecraftServerInstance().getTickCounter() % 20 == 0) {
            handleTime(event.world, worldEnv);
            handleRain(event.world, worldEnv);

            if (event.world.getTotalWorldTime() >= WORLD_RESET_TIME)
                resetWorldTime(event.world);
        }
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
                setRealTime(world, env.time.timezone);
                break;
            case "scaled":
                setScaledTime(world, env.time.scale);
                break;
            case "fixed":
                setFixedTime(world, env.time.fixedTime);
                break;
        }
    }

    private void setRealTime(World world, ZoneId timezone) {
        final Duration mcTimeFix = Duration.ofHours(6);
        final LocalDateTime now = LocalDateTime.now(timezone).minus(mcTimeFix);

        final long secs = now.toLocalTime().toSecondOfDay();

        final long day = now.toLocalDate().toEpochDay();
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

    private void handleRain(World world, WorldEnv env) {
        final WorldInfo wi = world.getWorldInfo();
        final WorldEnv.Rain r = env.rain;

        wi.setRainTime(Integer.MAX_VALUE);
        wi.setThunderTime(Integer.MAX_VALUE);

        if (wi.isRaining()) {
            r.accumulated -= r.outcome;
            if (r.accumulated <= 0) {
                wi.setRaining(false);
                wi.setThundering(false);
            } else {
                wi.setThundering(r.accumulated > r.thunderThreshold);
            }
        } else {
            r.accumulated += r.income;
            final boolean roll = (r.accumulated >= r.minThreshold) && world.rand.nextInt(r.rainfallDice) == 0;
            if (roll) {
                System.out.println(String.format("AENV: [%d] successful rainfall dice roll (while %d>=%d)",
                        world.provider.dimensionId, r.accumulated, r.minThreshold));
            }
            if (roll || r.accumulated > r.maxThreshold) {
                wi.setRaining(true);
                wi.setThundering(r.accumulated > r.thunderThreshold);
            }
        }
    }

    private void resetWorldTime(World world) {
        final String fieldName = (Boolean) Launch.blackboard.getOrDefault("fml.deobfuscatedEnvironment", false)
                ? "totalTime" : "field_82575_g";
        ReflectionHelper.setPrivateValue(WorldInfo.class, world.getWorldInfo(), 0, fieldName);
    }
}
