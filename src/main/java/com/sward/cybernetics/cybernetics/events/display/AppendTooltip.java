package com.sward.cybernetics.cybernetics.events.display;

import com.sward.cybernetics.cybernetics.Event;
import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Comparator;
import java.util.List;

@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public interface AppendTooltip
{
	Event<AppendTooltip> EVENT = Event.create(AppendTooltip.class, new Default(), Merger::new);

	int tooltipPriority();

	void appendTooltip(List<Component> tooltip, ItemStack stack, @Nullable Player player, boolean isAdvanced);

	record Default() implements AppendTooltip
	{
		@Override
		public int tooltipPriority()
		{
			return 0;
		}

		@Override
		public void appendTooltip(
			List<Component> tooltip,
			ItemStack stack,
			@org.jetbrains.annotations.Nullable Player player,
			boolean isAdvanced
		)
		{

		}
	}

	record Merger(List<AppendTooltip> components) implements AppendTooltip
	{
		public Merger(List<AppendTooltip> components)
		{
			this.components = components;
			this.components.sort(Comparator.comparingInt(AppendTooltip::tooltipPriority).reversed());
		}

		@Override
		public int tooltipPriority()
		{
			return 0;
		}

		@Override
		public void appendTooltip(
			List<Component> tooltip,
			ItemStack stack,
			@Nullable Player player,
			boolean isAdvanced
		)
		{
			int prevPriority = this.components.get(0).tooltipPriority();

			for (AppendTooltip component : components)
			{
				int priority = component.tooltipPriority();

				if (priority / 1000 != prevPriority / 1000)
				{
					tooltip.add(CommonComponents.EMPTY);
				}

				component.appendTooltip(tooltip, stack, player, isAdvanced);
			}
		}
	}
}
