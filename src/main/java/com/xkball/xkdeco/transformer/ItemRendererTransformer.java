package com.xkball.xkdeco.transformer;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class ItemRendererTransformer implements Transformer {

    public static final String name = "net.minecraft.client.renderer.ItemRenderer";
    public static String remappedName = "bly";

    @Override
    public boolean accept(String className) {
        return name.equals(className) || remappedName.equals(className) || name.equals(className.replace('/', '.'));
    }

    @Override
    public void transform(ClassNode classNode) {
        // System.out.println("xkdeco_t_" + remappedName);
        // for (MethodNode methodNode : classNode.methods) {
        // System.out.println(methodNode.name);
        // System.out.println(
        // FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(classNode.name, methodNode.name, methodNode.desc));
        // }
        var methodNodes = findMethod(classNode, "renderItemInFirstPerson", "func_78440_a");
        methodNodes.ifPresent(methodNode -> {
            // System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            var ifNonNullList = findLineWithOpcode(methodNode, Opcodes.IFNULL);
            var stackNonNullCount = 0;
            for (var line : ifNonNullList) {
                if (line.second.getPrevious() instanceof VarInsnNode vin && vin.var == 8) {
                    stackNonNullCount++;
                    if (stackNonNullCount == 4) {
                        methodNode.instructions.insert(
                            vin,
                            new MethodInsnNode(
                                Opcodes.INVOKESTATIC,
                                "com/xkball/xkdeco/client/render/FirstPersonRender",
                                "renderFirstPerson",
                                "(Lnet/minecraft/item/ItemStack;F)Ljava/lang/Object;",
                                false));
                        methodNode.instructions.insert(vin, new VarInsnNode(Opcodes.FLOAD, 2));
                        break;
                    }
                }
            }
        });
    }
}
