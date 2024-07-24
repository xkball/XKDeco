package com.xkball.xkdeco.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.ToIntFunction;

public class WightLoopList<T>{
    private final Random random = new Random();
    private final List<T> loops = new ArrayList<>();
    private final int[] weightSums;
    private int weightCount;

    public WightLoopList(List<T> loops, ToIntFunction<T> weightFunction) {
        this.loops.addAll(loops);
        int[] weights = new int[loops.size()];
        weightSums = new int[loops.size()];
        weights[0] = weightFunction.applyAsInt(loops.get(0));
        weightSums[0] = weights[0];
        weightCount = weights[0];
        for (int i = 1; i < weights.length; i++) {
            weights[i] = weightFunction.applyAsInt(loops.get(i));
            weightSums[i] = weightSums[i-1] + weights[i];
            weightCount += weights[i];
        }
    }

    public T roll(){
        var i = random.nextInt(weightCount);
        var index = Arrays.binarySearch(weightSums, i);
        if(index < 0){
            return loops.get(Math.min(-(index+1),loops.size()-1));
        }
        return loops.get(index);
    }
}
