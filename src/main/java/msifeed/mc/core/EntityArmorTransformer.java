package msifeed.mc.core;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.ListIterator;

public class EntityArmorTransformer implements IClassTransformer, Opcodes {
    private static final String METHOD_NAME_OBF = "b";
    private static final String METHOD_DESC_OBF = "(Lro;F)F";
    private static final String METHOD_NAME_DEOBF = "applyArmorCalculations";
    private static final String METHOD_DESC_DEOBF = "(Lnet/minecraft/util/DamageSource;F)F";

    private static final int VANILLA_ARMOR_DIVIDER = 25;
    private static final int ARMOR_DIVIDER = 40;

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if ("net.minecraft.entity.EntityLivingBase".equals(transformedName))
            return transformEntityLivingBase(basicClass);
        else if ("net.minecraftforge.common.ISpecialArmor$ArmorProperties".equals(transformedName))
            return transformArmorProperties(basicClass);
        return basicClass;
    }

    private static byte[] transformEntityLivingBase(byte[] basicClass) {
        final ClassNode cnode = new ClassNode();
        final ClassReader reader = new ClassReader(basicClass);
        reader.accept(cnode, ClassReader.EXPAND_FRAMES);

        final String mnName = MoreCorePlugin.isDevEnv ? METHOD_NAME_DEOBF : METHOD_NAME_OBF;
        final String mnDesc = MoreCorePlugin.isDevEnv ? METHOD_DESC_DEOBF : METHOD_DESC_OBF;
        final MethodNode mn = cnode.methods.stream()
                .filter(m -> m.name.equals(mnName) && m.desc.equals(mnDesc))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Cant find method " + mnName));
        patchArmorRating(mn);

        final ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        cnode.accept(cw);
        return cw.toByteArray();
    }

    private static byte[] transformArmorProperties(byte[] basicClass) {
        final ClassNode cnode = new ClassNode();
        final ClassReader reader = new ClassReader(basicClass);
        reader.accept(cnode, ClassReader.EXPAND_FRAMES);

        final MethodNode mn = cnode.methods.stream()
                .filter(m -> m.name.equals("ApplyArmor"))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Cant find method ApplyArmor"));
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
            } else if (an instanceof LdcInsnNode) {
                final LdcInsnNode in = (LdcInsnNode) an;
                if (in.cst instanceof Float) {
                    final Float f = (Float) in.cst;
                    if (f == VANILLA_ARMOR_DIVIDER) {
                        in.cst = (float) ARMOR_DIVIDER;
                        break;
                    }
                } else if (in.cst instanceof Double) {
                    final Double d = (Double) in.cst;
                    if (d == VANILLA_ARMOR_DIVIDER) {
                        in.cst = (double) ARMOR_DIVIDER;
                        break;
                    }
                }
            }
        }
    }
}
