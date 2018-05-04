package org.genesis.genetics.Genes;


import java.util.ArrayList;
import java.util.HashMap;

public abstract class Gene
{
    //Gene Types
    public enum GeneType
    {
        MassFactor
    }

    //Genetic Constants
    public static String Letters = "ACGT";
    public static int CodonLength = 3;
    public static int GeneCodonLength = 12;
    public static double CodonCombinations = getCodonCombinations();
    public static double getCodonCombinations() { return Math.pow(Letters.length(), CodonLength); }

    //Translation Constants
    public static String StartCodon = "ATG";
    public static HashMap<GeneType, String> TypeToCodonMap = InitTypeToCodonMap();
    public static HashMap<String, GeneType> CodonToTypeMap = InitCodonToTypeMap();

    //Test Constants
    public static String TestGene = "ATGAAAAAAAAA";

    //Static Initializers
    public static HashMap<GeneType, Gene> GeneExpressionMap = InitExpressionMap();
    public static HashMap<GeneType, Gene> InitExpressionMap() {

        HashMap<GeneType, Gene> result = new HashMap<>();

        result.put(GeneType.MassFactor, new NaturalGene(GeneType.MassFactor, TypeToCodonMap.get(GeneType.MassFactor)).Clone(.5, .5));

        return result;
    }
    public static HashMap<GeneType, String> InitTypeToCodonMap()
    {
        HashMap<GeneType, String> result = new HashMap<GeneType, String>();

        result.put(GeneType.MassFactor, "AAA");

        return result;
    }
    public static HashMap<String, GeneType> InitCodonToTypeMap()
    {
        HashMap<GeneType, String> typeToCodonMap = InitTypeToCodonMap();
        ArrayList<GeneType> types = new ArrayList<GeneType>(typeToCodonMap.keySet());
        ArrayList<String> values = new ArrayList<String>(typeToCodonMap.values());

        HashMap<String, GeneType> result = new HashMap<>();
        for (int i = 0; i < types.size(); i++)
            result.put(values.get(i), types.get(i));

        return result;
    }

    //Static Functions
    public static double ConvertCodonToDouble(String codon)
    {
        double result = 0.0;
        for (int i = 0 ; i < codon.length(); i++)
        {
            String letter = codon.substring(i, i+1);
            int letterIndex = Letters.indexOf(letter);
            double power = Math.pow(Letters.length(), (codon.length() - (i + 1)));
            double value = (letterIndex) * power;

            result += value;
        }

        return result / (CodonCombinations - 1);
    }
    public static String ConvertDoubleToCodon(double value)
    {
        String result = "";
        value *= (CodonCombinations);

        int[] a = new int[CodonLength];
        for (int i = 0; i < a.length; i++)
        {
            int level = (int)Math.pow(Letters.length(), a.length - (i + 1));
            int modValue = (int)(value / level);
            value -= level * modValue;
            result += Letters.substring(modValue, modValue + 1);
        }

        return result;
    }
    public static double Transform(double proportion, double lowerBound, double upperBound)
    {
        return lowerBound + ((upperBound - lowerBound) * proportion);
    }
    public static Gene Translate(String geneString) {

        String typeCodon = geneString.substring(3, 6);
        if (!CodonToTypeMap.containsKey(typeCodon))
            return null;

        double geneValue = ConvertCodonToDouble(geneString.substring(6, 9));
        double geneDominance = ConvertCodonToDouble(geneString.substring(9, 12));

        GeneType geneType = CodonToTypeMap.get(typeCodon);
        Gene newGene = GeneExpressionMap.get(geneType).Clone(geneValue, geneDominance);

        return newGene;
    }

    //Fields
    public GeneType geneType;
    public String codon;
    public double value;
    public double dominance;

    protected Gene(GeneType geneType, String codon, double value, double dominance)
    {
        this.geneType = geneType;
        this.codon = codon;
        this.value = value;
        this.dominance = dominance;
    }

    public abstract Object Express(GeneType geneType);

    public abstract Gene Clone(double value, double dominance);

    public String getGeneString()
    {
        return StartCodon + codon + ConvertDoubleToCodon(value) + ConvertDoubleToCodon(dominance);
    }

}
