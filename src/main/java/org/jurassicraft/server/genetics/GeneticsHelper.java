package org.jurassicraft.server.genetics;

import org.jurassicraft.server.dna.DnaUtils;

import java.util.Random;

@Deprecated
public class GeneticsHelper {//TODO: delete this class ?
    public static String randomGenetics(Random random) {
        return DnaUtils.createGenetics(random, 5);
    }
}
