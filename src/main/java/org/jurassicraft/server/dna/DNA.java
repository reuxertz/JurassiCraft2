package org.jurassicraft.server.dna;

import java.util.function.Consumer;

public class DNA {
    private final int[] values = new int[GeneType.values().length];
    private final Consumer<String> dnaSetter;

    public DNA(String dna) {
        this(dna, d -> {});
    }

    public DNA(String dna, Consumer<String> dnaSetter) {
        this.dnaSetter = dnaSetter;
        if(dna.length() < values.length * DnaUtils.DIGITS) {
            dna += DnaUtils.CHARS.get(0); //Maybe make this random random ?
        }
        dnaSetter.accept(dna);
        for (int i = 0; i < values.length; i+=DnaUtils.DIGITS) {
            StringBuilder str = new StringBuilder();
            for (int i1 = 0; i1 < DnaUtils.DIGITS; i1++) {
                str.append(dna.charAt(i + i1));
            }
            values[i / 2] = DnaUtils.getInt(str.toString());
        }
    }

    public int getRawValue(GeneType geneType) {
        return values[geneType.ordinal()];
    }

    public int getValue(GeneType geneType) {
        int range = (DnaUtils.MAX_VALUE + 1) / 2;
        int raw = getRawValue(geneType);
        return raw - range + (raw >= range ? 1 : 0);
    }

    public float getValueFloat(GeneType geneType) {
        if(geneType == GeneType.SIZE) {
            return 1f;
        }
        return getValue(geneType) / ((DnaUtils.MAX_VALUE + 1) / 2);
    }

    public void setValue(GeneType geneType, int value) {
        this.values[geneType.ordinal()] = value;
        this.dnaSetter.accept(this.getString());
    }

    public String getString() {
        StringBuilder out = new StringBuilder();
        for (int value : this.values) {
            out.append(DnaUtils.getDna(value));
        }
        return out.toString();
    }
}
