package msifeed.mc.mellow.widgets.scene;

import msifeed.mc.mellow.utils.Point;
import msifeed.mc.mellow.widgets.Widget;
import net.minecraft.client.Minecraft;
import net.minecraft.profiler.Profiler;

import java.util.stream.Stream;

public class ProfilingScene extends Scene {
    @Override
    public void update() {
        final Profiler pr = Minecraft.getMinecraft().mcProfiler;
        pr.startSection("MellowUpdate");
        super.update();
        pr.endSection();
    }

    @Override
    public void render() {
        final Profiler pr = Minecraft.getMinecraft().mcProfiler;
        pr.startSection("MellowRender");
        super.render();
        pr.endSection();
    }

    @Override
    public Stream<Widget> lookupWidget(Point p) {
        final Profiler pr = Minecraft.getMinecraft().mcProfiler;
        pr.startSection("MellowLookup");
        final Stream<Widget> lookup = super.lookupWidget(p);
        pr.endSection();
        return lookup;
    }
}
