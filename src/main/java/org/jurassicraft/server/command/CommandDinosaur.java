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
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.JurassicraftRegisteries;
import org.jurassicraft.server.item.DinosaurProvider;

import javax.annotation.Nullable;
import java.util.List;

public class CommandDinosaur extends CommandBase {
    @Override
    public String getName() {
        return "dinosauritem";
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
            if(stack.getItem() instanceof DinosaurProvider) {
                Dinosaur dinosaur = JurassicraftRegisteries.DINOSAUR_REGISTRY.getValue(new ResourceLocation(args[0]));
                if(!dinosaur.isMissing()) {
                    entityLivingBase.setHeldItem(EnumHand.MAIN_HAND, ((DinosaurProvider)stack.getItem()).getItemStack(dinosaur));
                    sender.sendMessage(new TextComponentString("Compleated"));
                }
            }
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, JurassicraftRegisteries.DINOSAUR_REGISTRY.getKeys()) : Lists.newArrayList();
    }
}
