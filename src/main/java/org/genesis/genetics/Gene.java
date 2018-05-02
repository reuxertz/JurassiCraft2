package org.genesis.genetics;


import java.util.HashMap;

public class Gene
{
    //Genetic Constants
    public static String Letters = "ACTG";
    public static int CodonLength = 3;
    public static int GeneCodonLength = 12;

    //Translation Constants
    public static String StartCodon = "ATG";
    public static HashMap<String, Gene> GeneMap = InitGeneTypeMap();

    //Test Constants
    public static String TestGene = "ATGAAAAAAAAA";

    //Static Initializers
    public static HashMap<String, Gene> InitGeneTypeMap()
    {
        HashMap<String, Gene> result = new HashMap<String, Gene>();

        result.put("AAA", new Gene("AAA"));

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
            double power = Math.pow(Letters.length(), i);
            double value = (letterIndex) * power;

            result += value;
        }

        return result;
    }
    public static Gene Translate(String geneString) {

        String geneType = geneString.substring(3, 6);
        if (!Gene.GeneMap.containsKey(geneType))
            return null;

        double geneValue = ConvertCodonToDouble(geneString.substring(6, 9));
        double geneDominance = ConvertCodonToDouble(geneString.substring(9, 12));

        Gene geneTemplate = GeneMap.get(geneType);
        Gene newGene = geneTemplate.Clone(geneValue, geneDominance);

        return newGene;
    }

    //Gene Types
    public enum GeneType
    {
        MassFactor
    }

    //Fields
    protected String Codon;
    protected double Value;
    protected double Dominance;

    public Gene(String codon, double value, double dominance)
    {
        Codon = codon;
        Value = value;
        Dominance = dominance;
    }
    public Gene(String codon)
    {
        this(codon, 0.0, 0.0);
    }

    public Gene Clone(double value, double dominance)
    {
        return new Gene(Codon, value, dominance);
    }

}
