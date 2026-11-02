package com.sward.cybernetics.client;

import com.sward.cybernetics.cybernetics.CyberneticDefinition;
import com.sward.cybernetics.cybernetics.events.display.AppendTooltip;
import com.sward.cybernetics.cybernetics.events.display.AppendWhenImplantedTooltip;
import com.sward.cybernetics.cybernetics.CyberneticDefinitions;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

import static com.sward.cybernetics.Cybernetics.ID;

@Mod.EventBusSubscriber(modid = ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class CyberneticsTooltips
{
	@SubscribeEvent
	public static void tooltip(ItemTooltipEvent e)
	{
		ClientLevel level = Minecraft.getInstance().level;

		if (level == null)
		{
			return;
		}

		CyberneticDefinition definition = CyberneticDefinitions.forItem(e.getItemStack().getItem());

		if (definition == null)
		{
			return;
		}

		List<Component> tooltip = e.getToolTip();
		ItemStack stack = e.getItemStack();
		Player entity = e.getEntity();
		boolean isAdvanced = e.getFlags().isAdvanced();

		definition.getEvent(AppendTooltip.EVENT).appendTooltip(tooltip, stack, entity, isAdvanced);

		tooltip.add(CommonComponents.EMPTY);
		tooltip.add(
			Component.translatable(
				"tooltip.cybernetics.slot",
				Component.translatable("cybernetics_slot_type." + definition.getSlot())
			).withStyle(ChatFormatting.BLUE)
		);

		List<Component> modifierTooltip = new ArrayList<>();
		definition.getEvent(AppendWhenImplantedTooltip.EVENT)
			.appendWhenImplantedTooltip(modifierTooltip, stack, entity, isAdvanced);

		if (!modifierTooltip.isEmpty())
		{
			tooltip.add(CommonComponents.EMPTY);
			tooltip.add(Component.translatable("tooltip.cybernetics.modifiers").withStyle(ChatFormatting.GRAY));
			tooltip.addAll(modifierTooltip);
		}
	}
}
