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
public interface AppendModifierTooltip
{
	Event<AppendModifierTooltip> EVENT = Event.create(AppendModifierTooltip.class, new Default(), Merger::new);

	int modifierTooltipPriority();

	void appendModifierTooltip(List<Component> tooltip, ItemStack stack, @Nullable Player player, boolean isAdvanced);

	record Default() implements AppendModifierTooltip
	{
		@Override
		public int modifierTooltipPriority()
		{
			return 0;
		}

		@Override
		public void appendModifierTooltip(
			List<Component> tooltip,
			ItemStack stack,
			@org.jetbrains.annotations.Nullable Player player,
			boolean isAdvanced
		)
		{

		}
	}

	record Merger(List<AppendModifierTooltip> components) implements AppendModifierTooltip
	{
		public Merger(List<AppendModifierTooltip> components)
		{
			this.components = components;
			this.components.sort(Comparator.comparingInt(AppendModifierTooltip::modifierTooltipPriority).reversed());
		}

		@Override
		public int modifierTooltipPriority()
		{
			return 0;
		}

		@Override
		public void appendModifierTooltip(
			List<Component> tooltip,
			ItemStack stack,
			@Nullable Player player,
			boolean isAdvanced
		)
		{
			int prevPriority = this.components.get(0).modifierTooltipPriority();

			for (AppendModifierTooltip component : this.components)
			{
				int priority = component.modifierTooltipPriority();

				if (priority / 1000 != prevPriority / 1000)
				{
					tooltip.add(CommonComponents.EMPTY);
				}

				component.appendModifierTooltip(tooltip, stack, player, isAdvanced);
			}
		}
	}
}
