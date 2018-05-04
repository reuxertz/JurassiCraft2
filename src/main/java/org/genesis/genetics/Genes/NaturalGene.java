package org.genesis.genetics.Genes;

public class NaturalGene extends Gene {

    public NaturalGene(GeneType geneType, String codon)
    {
        super(geneType, codon, 0, 0);
    }

    protected NaturalGene(GeneType geneType, String codon, double value, double dominance)
    {
        super(geneType, codon, value, dominance);
    }

    public Gene Clone(double value, double dominance)
    {
        return new NaturalGene(geneType, codon, value, dominance);
    }

    public Object Express(GeneType geneType)
    {
        return 0.0;
    }


}
