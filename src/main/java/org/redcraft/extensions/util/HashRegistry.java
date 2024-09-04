package org.redcraft.extensions.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class HashRegistry<T> {
    Map<Integer,T> map;
    Function<T,Integer> hashFunction;

    public HashRegistry(Function<T,Integer> hashFunction) {
        map = new HashMap<>();
        this.hashFunction = hashFunction;
    }

    public void remove(int hash) {
        map.remove(hash);
    }

    public void remove(@NotNull T t) {
        map.remove(hashFunction.apply(t));
    }

    public void register(@NotNull T t) {
        int hash = hashFunction.apply(t);
        if(map.containsKey(hash)) throw new RuntimeException("Object is already registered!");
        map.put(hash,t);
    }

    @Nullable
    public T get(int hash) {
        return map.get(hash);
    }
}
