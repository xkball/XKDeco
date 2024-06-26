package com.xkball.xkdeco.transformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.xkball.xkdeco.crossmod.applecore.AppleCoreOverlayTransformer;
import com.xkball.xkdeco.crossmod.tfc.TFCOverLayTransformer;

public class TransformerRegistry {

    static List<Transformer> transformers = new ArrayList<>();

    static {
        transformers.add(new ItemRendererTransformer());
        transformers.add(new TFCOverLayTransformer());
        transformers.add(new AppleCoreOverlayTransformer());
    }

    public static Set<Transformer> getTransformers(String className) {
        return transformers.stream()
            .filter(transformer -> transformer.accept(className))
            .collect(Collectors.toSet());
    }

    public static void init() {
        // 仅用于触发类加载
    }
}
