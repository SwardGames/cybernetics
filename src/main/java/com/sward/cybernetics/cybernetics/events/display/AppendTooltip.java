package com.sward.cybernetics.cybernetics.events.display;

import com.sward.cybernetics.cybernetics.Event;
import com.sward.cybernetics.tooltips.CyberneticTooltipCategory;
import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public interface AppendTooltip
{
	Event<AppendTooltip> EVENT = Event.create(AppendTooltip.class, (category, tooltip, stack, player, isAdvanced) -> {}, Merger::new);

	void appendTooltip(CyberneticTooltipCategory category, List<Component> tooltip, ItemStack stack, @Nullable Player player, boolean isAdvanced);

	record Merger(List<AppendTooltip> components) implements AppendTooltip
	{
		@Override
		public void appendTooltip(
			CyberneticTooltipCategory category,
			List<Component> tooltip,
			ItemStack stack,
			@Nullable Player player,
			boolean isAdvanced
		)
		{
			for (AppendTooltip component : this.components)
			{
				component.appendTooltip(category, tooltip, stack, player, isAdvanced);
			}
		}
	}
}
