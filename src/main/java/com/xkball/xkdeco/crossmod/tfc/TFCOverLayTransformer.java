package com.xkball.xkdeco.crossmod.tfc;

import java.util.concurrent.atomic.AtomicReference;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.IincInsnNode;

import com.xkball.xkdeco.transformer.Transformer;

import cpw.mods.fml.common.Loader;

public class TFCOverLayTransformer implements Transformer {

    @Override
    public boolean accept(String className) {
        return "com.bioxx.tfc.Handlers.Client.RenderOverlayHandler".equals(className);
    }

    @Override
    public void transform(ClassNode classNode) {
        if (!Loader.isModLoaded("dualhotbar")) return;
        findMethod(classNode, "drawTexturedModalRect").ifPresent(
            methodNode -> methodNode.instructions.insert(methodNode.instructions.getFirst(), new IincInsnNode(2, -18)));
        findMethod(classNode, "render").ifPresent(methodNode -> {
            var entry = findLineWithOpcode(methodNode, Opcodes.BIPUSH);
            AtomicReference<AbstractInsnNode> insn = new AtomicReference<>();
            entry.stream()
                .filter(e -> e.first.line == 224)
                .findFirst()
                .ifPresent(
                    e -> insn.set(
                        e.second.getNext()
                            .getNext()
                            .getNext()
                            .getNext()
                            .getNext()
                            .getNext()
                            .getNext()
                            .getNext()));
            methodNode.instructions.insert(insn.get(), new IincInsnNode(7, -20));
        });
    }
}
