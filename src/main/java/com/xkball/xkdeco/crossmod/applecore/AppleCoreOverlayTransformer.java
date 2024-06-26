package com.xkball.xkdeco.crossmod.applecore;

import cpw.mods.fml.common.Loader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.IincInsnNode;

import com.xkball.xkdeco.transformer.Transformer;

public class AppleCoreOverlayTransformer implements Transformer {

    @Override
    public boolean accept(String className) {
        return "squeek.applecore.client.HUDOverlayHandler".equals(className);
    }

    @Override
    public void transform(ClassNode classNode) {
        if (!Loader.isModLoaded("dualhotbar")) return;
        transformMethodParamIinc(classNode, "drawSaturationOverlay", 4, -20);
        transformMethodParamIinc(classNode, "drawHungerOverlay", 4, -20);
        //transformMethodParamIinc(classNode, "drawExhaustionOverlay", 3, -20);
    }

    private void transformMethodParamIinc(ClassNode classNode, String method, int pid, int iinc) {
        findMethod(classNode, method).ifPresent(methodNode -> {
            var first = methodNode.instructions.getFirst();
            methodNode.instructions.insertBefore(first, new IincInsnNode(pid, iinc));
        });
    }
}
