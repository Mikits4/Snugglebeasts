package net.mikitstrees.snugglebeasts.entity.varient;

import java.util.Arrays;
import java.util.Comparator;

public enum LovebirdVariant {
    GREEN(0),
    WHITE(1);

    private static final LovebirdVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.comparingInt(
            LovebirdVariant::getId)).toArray(LovebirdVariant[]::new);
    private final int id;

    LovebirdVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static LovebirdVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}