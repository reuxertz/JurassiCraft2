package org.genesis.genetics;

import java.util.*;

public class Genome
{
    public static Random Random = new Random();

    public static List<Gene> Translate(String genomeSequence)
    {
        List<Gene> result = new ArrayList<>();
        for (int i = 0; i < genomeSequence.length() - (Gene.CodonLength - 1); i++)
        {
            String nextThree = genomeSequence.substring(i, i+3);
            if (!nextThree.equals(Gene.StartCodon))
                continue;

            int remainingLength = genomeSequence.length() - i;
            if (remainingLength < Gene.GeneCodonLength)
                break;

            String geneSubString = genomeSequence.substring(i, Gene.GeneCodonLength);
            Gene newGene = Gene.Translate(geneSubString);

            if (newGene == null)
                continue;

            result.add(newGene);
        }

        return result;
    }

    public static String RandomDeletions(String sequence, double numDeletions)
    {
        for (int i = 0 ; i < numDeletions; i++)
        {
            double curProbability = numDeletions - i;

            if (curProbability > 0)
            {
                if (curProbability < 1 && Random.nextDouble() > curProbability)
                    return sequence;

                int randomPositionIndex = Random.nextInt(sequence.length());

                String preSequence = sequence.substring(0, randomPositionIndex);
                String postSequence = sequence.substring(randomPositionIndex + 1, sequence.length());

                sequence = preSequence + postSequence;
            }
        }

        return sequence;
    }
    public static String RandomInsertions(String sequence, double numInsertions)
    {
        for (int i = 0 ; i < numInsertions; i++)
        {
            double curProbability = numInsertions - i;

            if (curProbability > 0)
            {
                if (curProbability < 1 && Random.nextDouble() > curProbability)
                    return sequence;

                int randomLetterIndex = Random.nextInt(Gene.Letters.length());
                int randomPositionIndex = Random.nextInt(sequence.length());

                String randomLetter = Gene.Letters.substring(randomLetterIndex, randomLetterIndex + 1);

                String preSequence = sequence.substring(0, randomPositionIndex);
                String postSequence = sequence.substring(randomPositionIndex, sequence.length());

                sequence = preSequence + randomLetter + postSequence;
            }
        }

        return sequence;
    }
    public static List<String> Crossover(String sequence1, String sequence2)
    {
        List<String> result = new ArrayList<String>();




        return result;
    }

    public static List<Gene> FilterDominantGenes(List<Gene> allGenes)
    {
        HashMap<String, Gene> resultMap = new HashMap<String, Gene>();

        for (int i = 0; i < allGenes.size(); i++)
        {
            Gene newGene = allGenes.get(i);
            if (!resultMap.containsKey(newGene.Codon))
                resultMap.put(newGene.Codon, allGenes.get(i));
            else
            {
                Gene existingGene = resultMap.get(newGene.Codon);
                if (existingGene.Dominance <= newGene.Dominance)
                {
                    resultMap.remove(existingGene.Codon);
                    resultMap.put(newGene.Codon, newGene);
                }
            }
        }

        return new ArrayList(resultMap.values());
    }

    public String Sequence1;
    public String Sequence2;
    public List<Gene> Sequence1Genes;
    public List<Gene> Sequence2Genes;
    public List<Gene> ExpressedGenes;

    public Genome(String sequence1, String sequence2)
    {
        Sequence1 = sequence1;
        Sequence2 = sequence2;

        Translate();
    }

    public void Translate()
    {
        Sequence1Genes = Genome.Translate(Sequence1);
        Sequence2Genes = Genome.Translate(Sequence2);

        List<Gene> allGenes = new ArrayList<Gene>(Sequence1Genes);
        allGenes.addAll(Sequence2Genes);

        ExpressedGenes = FilterDominantGenes(allGenes);
    }
}
