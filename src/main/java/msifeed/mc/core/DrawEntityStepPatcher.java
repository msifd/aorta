package msifeed.mc.core;

import org.apache.commons.lang3.ArrayUtils;
import org.objectweb.asm.*;
import org.objectweb.asm.commons.Method;

public class DrawEntityStepPatcher extends ClassVisitor implements Opcodes {
    private final String transformerType = Type.getInternalName(JourneymapTransformer.class);
    private final Method drawMethod = new Method("draw",
            Type.VOID_TYPE,
            ArrayUtils.toArray(
                    Type.DOUBLE_TYPE, Type.DOUBLE_TYPE,
                    Type.getObjectType("journeymap/client/render/map/GridRenderer"),
                    Type.FLOAT_TYPE, Type.DOUBLE_TYPE, Type.DOUBLE_TYPE));
    private final Method isVisibleMethod = new Method("isVisibleOnMap",
            Type.BOOLEAN_TYPE,
            ArrayUtils.toArray(Type.getObjectType("net/minecraft/entity/EntityLivingBase")));

    public DrawEntityStepPatcher(ClassVisitor cv) {
        super(ASM5, cv);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        final MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        if (name.equals(drawMethod.getName()) && desc.equals(drawMethod.getDescriptor()))
            return new CallInjector(mv);
        else
            return mv;
    }

    private class CallInjector extends MethodVisitor {
        private boolean patched = false;

        public CallInjector(MethodVisitor mv) {
            super(ASM5, mv);
        }

        @Override
        public void visitVarInsn(int opcode, int var) {
            super.visitVarInsn(opcode, var);
            if (patched)
                return;
            if (opcode != ASTORE) // Trigger on first ASTORE
                return;

            Label continueLabel = new Label();

            visitVarInsn(ALOAD, var);
            mv.visitMethodInsn(INVOKESTATIC, transformerType, isVisibleMethod.getName(), isVisibleMethod.getDescriptor(), false);
            mv.visitJumpInsn(IFNE, continueLabel);
            mv.visitInsn(RETURN);
            mv.visitLabel(continueLabel);

            patched = true;
        }
    }
}
