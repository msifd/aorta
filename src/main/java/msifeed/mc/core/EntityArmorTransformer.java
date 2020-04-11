package msifeed.mc.core;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

import java.util.ListIterator;

public class EntityArmorTransformer implements IClassTransformer, Opcodes {
    private static final String METHOD_OBF = "func_70655_b";
    private static final String METHOD_DEOBF = "applyArmorCalculations";

    private static final int VANILLA_ARMOR_DIVIDER = 25;
    private static final int ARMOR_DIVIDER = 40;

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if ("net.minecraft.entity.EntityLivingBase".equals(transformedName))
            return transformEntityLivingBase(basicClass);
        return basicClass;
    }

    private static byte[] transformEntityLivingBase(byte[] basicClass) {
        final ClassNode cnode = new ClassNode();
        final ClassReader reader = new ClassReader(basicClass);
        reader.accept(cnode, ClassReader.EXPAND_FRAMES);

        final String mnName = MoreCorePlugin.isDevEnv ? METHOD_DEOBF : METHOD_OBF;
        final MethodNode mn = cnode.methods.stream()
                .filter(m -> m.name.equals(mnName))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Cant find method " + mnName));
        patchArmorRating(mn);

        final ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        cnode.accept(cw);
        return cw.toByteArray();
    }

    private static void patchArmorRating(MethodNode mn) {
        final ListIterator<AbstractInsnNode> it = mn.instructions.iterator();

        while (it.hasNext()) {
            final AbstractInsnNode an = it.next();
            if (an instanceof IntInsnNode) {
                final IntInsnNode in = (IntInsnNode) an;
                if (in.operand == VANILLA_ARMOR_DIVIDER) {
                    in.operand = ARMOR_DIVIDER;
                    break;
                }
            }
        }

        while (it.hasNext()) {
            final AbstractInsnNode an = it.next();
            if (an instanceof LdcInsnNode) {
                final LdcInsnNode ldc = (LdcInsnNode) an;
                if (ldc.cst instanceof Float && ((Float) ldc.cst).intValue() == VANILLA_ARMOR_DIVIDER) {
                    ldc.cst = new Float(ARMOR_DIVIDER);
                    break;
                }
            }
        }
    }
}
