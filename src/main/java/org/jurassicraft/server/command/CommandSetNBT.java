package org.jurassicraft.server.command;

import com.google.common.collect.Lists;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import org.jurassicraft.server.api.StackNBTProvider;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.registries.JurassicraftRegisteries;
import org.jurassicraft.server.api.DinosaurProvider;

import javax.annotation.Nullable;
import java.util.List;

public class CommandSetNBT extends CommandBase {
    @Override
    public String getName() {
        return "jcsetnbt";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "test";//TODO
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        if(sender.getCommandSenderEntity() instanceof EntityLivingBase && args.length != 0) {
            EntityLivingBase entityLivingBase = ((EntityLivingBase)sender.getCommandSenderEntity());
            ItemStack stack = entityLivingBase.getHeldItemMainhand();
            if(stack.getItem() instanceof StackNBTProvider) {
                StackNBTProvider provider = StackNBTProvider.getFromStack(stack);
                if(provider != null) {
                    entityLivingBase.setHeldItem(EnumHand.MAIN_HAND, provider.putValue(stack, provider.getValueFromName(args[0])));
                    sender.sendMessage(new TextComponentString("Compleated"));
                }
            }
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if(sender.getCommandSenderEntity() instanceof EntityLivingBase && args.length == 1) {
            EntityLivingBase entityLivingBase = ((EntityLivingBase) sender.getCommandSenderEntity());
            ItemStack stack = entityLivingBase.getHeldItemMainhand();
            StackNBTProvider provider = StackNBTProvider.getFromStack(stack);
            if(provider != null) {
                return getListOfStringsMatchingLastWord(args, provider.getKeySet());
            }
        }
        return Lists.newArrayList();
    }
}
