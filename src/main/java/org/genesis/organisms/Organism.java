package org.genesis.organisms;

import net.minecraft.nbt.NBTTagCompound;
import org.genesis.genetics.Genome;

public class Organism
{
    public Genome genome;

    public Organism(String sequence)
    {
        this(new Genome(sequence));
    }
    public Organism(Genome genome)
    {
        this.genome = genome;
    }

    public void writeToNBT(NBTTagCompound nbt)
    {
        genome.writeToNBT(nbt);
    }

    public static Organism readFromNBT(NBTTagCompound nbt)
    {
        Genome genome = Genome.readFromNBT(nbt);

        return new Organism(genome);
    }
}
