package nischitweaker.asm;

import meldexun.asmutil2.ASMUtil;
import meldexun.asmutil2.HashMapClassNodeClassTransformer;
import meldexun.asmutil2.IClassTransformerRegistry;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class ZenUtilsConfigurationClassTransformer extends HashMapClassNodeClassTransformer implements IClassTransformer {

    @Override
    protected void registerTransformers(IClassTransformerRegistry registry) {
        registry.add("youyihj.zenutils.impl.core.Configuration", "<clinit>", ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS, method -> {
            MethodInsnNode register = ASMUtil.first(method).methodInsn("register").find();
            ASMUtil.replace(method, register, ASMUtil.listOf(
                    new InsnNode(Opcodes.ACONST_NULL),
                    new MethodInsnNode(Opcodes.INVOKESTATIC, "youyihj/zenutils/impl/config/ConfigAnytimeAnytime", "register", "(Ljava/lang/Class;Lnet/minecraftforge/fml/common/ModMetadata;)V", false)
            ));
        });
    }
}

