package msifeed.mc.core;

import cpw.mods.fml.common.SidedProxy;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

public class JourneymapTransformer implements IClassTransformer, Opcodes {
    @SidedProxy(
            clientSide = "msifeed.mc.core.JourneymapProxyClient",
            serverSide = "msifeed.mc.core.JourneymapProxy"
    )
    public static JourneymapProxy PROXY;

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (name.equals("journeymap.client.render.draw.DrawEntityStep"))
            return transformDrawEntityStep(basicClass);
        return basicClass;
    }

    public static boolean isVisibleOnMap(EntityLivingBase entity) {
        return PROXY.isVisibleOnMap(entity);
    }

    private static byte[] transformDrawEntityStep(byte[] basicClass) {
        final ClassReader reader = new ClassReader(basicClass);
        final ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS);
        final ClassVisitor patcher = new DrawEntityStepPatcher(writer);

        reader.accept(patcher, 0);

        return writer.toByteArray();
    }

}
