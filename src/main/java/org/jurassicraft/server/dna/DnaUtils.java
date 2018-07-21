package org.jurassicraft.server.dna;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Random;

public class DnaUtils {

	public static List<Character> CHARS = Lists.newArrayList('A', 'C', 'G', 'T');
	public static int DIGITS = 3;
	public static int MAX_VALUE = (int) Math.pow(CHARS.size(), DIGITS);

	public static int M = 63 / 2;

	public static int getInt(String dnaSection) {
		StringBuilder out = new StringBuilder();
		for (char c : dnaSection.toUpperCase().toCharArray()) {
			out.append(CHARS.indexOf(c));
		}
		return Integer.valueOf(out.toString(), CHARS.size());
	}

	public static String getDna(int dnaValue) {
		if (dnaValue >= MAX_VALUE) {
			dnaValue = MAX_VALUE - 1;
		}
		StringBuilder output = new StringBuilder();
		int residue;
		int div = dnaValue;
		do {
			residue = div % CHARS.size();
			div = Math.floorDiv(div, CHARS.size());
			output.append(CHARS.get(residue));
		} while (div != 0);
		StringBuilder reversed = new StringBuilder();
		for (int i = output.length(); i > 0; i--) {
			reversed.append(output.charAt(i - 1));
		}
		while (reversed.length() < DIGITS) {
			reversed.insert(0, CHARS.get(0));
		}
		return reversed.toString();
	}

	public static String createGenetics(Random rand, int deviancy) {
		StringBuilder out = new StringBuilder();
		int range = (DnaUtils.MAX_VALUE + 1) / 2;
		for (int i = 0; i < GeneType.values().length; i++) {
			int typeDeviancy = rand.nextInt(deviancy);
			int geneNum;
			if (rand.nextBoolean()) {
				geneNum = range + typeDeviancy;
			} else {
				geneNum = range - typeDeviancy - 1;
			}
			out.append(getDna(geneNum));
		}
		return out.toString();
	}
}
