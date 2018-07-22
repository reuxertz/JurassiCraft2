package org.jurassicraft.server.item;

import com.google.gson.Gson;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.JurassiCraft;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class JournalItem extends Item {
    private final JournalType type;

    JournalItem(JournalType type) {
        this.type = type;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
    	ItemStack stack = player.getHeldItem(hand);
        if (world.isRemote) {
            JurassiCraft.PROXY.openJournal(this.type);
        }
        return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.translateToLocal("journal." + this.type.getIdentifier().getResourcePath() + ".name"));
    }

    public enum JournalType {
        CHEF_ALEJANDRO(new ResourceLocation(JurassiCraft.MODID, "chef_alejandro")),
        DENNIS_NEDRY(new ResourceLocation(JurassiCraft.MODID, "dennis_nedry")),
        DR_GERRY_HARDING(new ResourceLocation(JurassiCraft.MODID, "dr_gerry_harding")),
        DR_HENRY_WU(new ResourceLocation(JurassiCraft.MODID, "dr_henry_wu")),
        DR_LAURA_SORKIN(new ResourceLocation(JurassiCraft.MODID, "dr_laura_sorkin")),
        ED_REGIS(new ResourceLocation(JurassiCraft.MODID, "ed_regis")),
        JOHN_HAMMOND(new ResourceLocation(JurassiCraft.MODID, "john_hammond")),
        RAY_ARNOLD(new ResourceLocation(JurassiCraft.MODID, "ray_arnold")),
        ROBERT_MULDOON(new ResourceLocation(JurassiCraft.MODID, "robert_muldoon"));

        private final ResourceLocation identifier;
        private final ResourceLocation location;

        @SideOnly(Side.CLIENT)
        private Content content;


        JournalType(ResourceLocation identifier) {
            this.identifier = identifier;
            this.location = new ResourceLocation(identifier.getResourceDomain(), "journal_entries/" + identifier.getResourcePath() + ".json");
        }

        @SideOnly(Side.CLIENT)
        public Content getContent() {
            if (this.content == null) {
                try (InputStream input = Minecraft.getMinecraft().getResourceManager().getResource(this.location).getInputStream()) {
                    this.content = new Gson().fromJson(new InputStreamReader(input), Content.class);
                } catch (IOException e) {
                    String[][] entries = new String[][] { new String[] { "Failed to load journal entries" } };
                    return new Content("jurassicraft:error", entries);
                }
            }
            return this.content;
        }

        public ResourceLocation getIdentifier() {
            return this.identifier;
        }

        public ResourceLocation getLocation() {
            return this.location;
        }
    }

    public static class Content {
        private String identifier;
        private String[][] entries;

        Content(String identifier, String[][] entries) {
            this.identifier = identifier;
            this.entries = entries;
        }

        public String getIdentifier() {
            return this.identifier;
        }

        public String[][] getEntries() {
            return this.entries;
        }
    }
}
