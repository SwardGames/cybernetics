package com.sward.cybernetics.tooltips;

import com.sward.cybernetics.cybernetics.CyberneticDefinition;
import com.sward.cybernetics.cybernetics.events.display.AppendTooltip;
import com.sward.cybernetics.cybernetics.CyberneticDefinitions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static com.sward.cybernetics.Cybernetics.ID;

@Mod.EventBusSubscriber(modid = ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CyberneticsTooltips
{
	private static final List<CyberneticTooltipCategory> CATEGORIES = new ArrayList<>();

	public static CyberneticTooltipCategory registerCategory(int priority)
	{
		return registerCategory(priority, null);
	}

	public static CyberneticTooltipCategory registerCategory(int priority, @Nullable Component title)
	{
		CyberneticTooltipCategory category = new CyberneticTooltipCategory(priority, title);

		int i = 0;

		while (i < CATEGORIES.size() && CATEGORIES.get(i).priority() > priority)
		{
			++i;
		}

		CATEGORIES.add(i, category);

		return category;
	}

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

		AppendTooltip event = definition.getEvent(AppendTooltip.EVENT);

		List<Component> categoryLines = new ArrayList<>();
		boolean isFirst = true;

		for (CyberneticTooltipCategory category : CATEGORIES)
		{
			event.appendTooltip(category, categoryLines, stack, entity, isAdvanced);

			if (!categoryLines.isEmpty())
			{
				if (!isFirst || category.title() != null)
				{
					tooltip.add(CommonComponents.EMPTY);
				}

				isFirst = false;

				if (category.title() != null)
				{
					tooltip.add(category.title());
				}

				tooltip.addAll(categoryLines);

				categoryLines.clear();
			}
		}
	}
}
