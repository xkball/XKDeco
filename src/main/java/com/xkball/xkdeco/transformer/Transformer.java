package com.xkball.xkdeco.transformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodNode;

import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

@SuppressWarnings("unused")
public interface Transformer {

    boolean accept(String className);

    void transform(ClassNode classNode);

    default byte[] transform(byte[] bytes) {
        var classNode = new ClassNode();
        var classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        transform(classNode);
        var classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }

    default Optional<MethodNode> findMethod(ClassNode classNode, String... name) {
        var set = Arrays.stream(name)
            .collect(Collectors.toSet());
        return classNode.methods.stream()
            .filter(m -> set.contains(FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(classNode.name, m.name, m.desc)))
            .findFirst();
    }

    // default Optional<MethodNode> findMethod(ClassNode classNode, String name, String desc) {
    // return classNode.methods.stream()
    // .filter(m -> m.name.equals(name) && m.desc.equals(desc))
    // .findFirst();
    // }

    // default Set<JumpInsnNode> findJump(MethodNode methodNode, int opcode){
    // return Arrays.stream(methodNode.instructions.toArray()).filter(insn -> insn instanceof JumpInsnNode jp &&
    // jp.getOpcode() == opcode)
    // .map(insn -> (JumpInsnNode) insn).collect(Collectors.toSet());
    // }
    default Optional<LineNumberNode> findLine(MethodNode methodNode, int lineNum) {
        return Arrays.stream(methodNode.instructions.toArray())
            .filter(insn -> insn instanceof LineNumberNode lnn && lnn.line == lineNum)
            .map(insn -> (LineNumberNode) insn)
            .findFirst();
    }

    default List<Pair<LineNumberNode, AbstractInsnNode>> findLineWithOpcode(MethodNode methodNode, int opcode) {
        var result = new ArrayList<Pair<LineNumberNode, AbstractInsnNode>>();
        LineNumberNode current = null;
        var it = methodNode.instructions.iterator();
        while (it.hasNext()) {
            var c = it.next();
            if (c instanceof LineNumberNode) {
                current = (LineNumberNode) c;
            } else if (c.getOpcode() == opcode && current != null) {
                result.add(new Pair<>(current, c));
                current = null;
            }
        }
        return result;
    }

    class Pair<T, U> {

        public T first;
        public U second;

        Pair(T first, U second) {
            this.first = first;
            this.second = second;
        }
    }
}
