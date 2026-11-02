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
public interface AppendWhenImplantedTooltip
{
	Event<AppendWhenImplantedTooltip> EVENT = Event.create(AppendWhenImplantedTooltip.class, new Default(), Merger::new);

	int whenImplantedTooltipPriority();

	void appendWhenImplantedTooltip(List<Component> tooltip, ItemStack stack, @Nullable Player player, boolean isAdvanced);

	record Default() implements AppendWhenImplantedTooltip
	{
		@Override
		public int whenImplantedTooltipPriority()
		{
			return 0;
		}

		@Override
		public void appendWhenImplantedTooltip(
			List<Component> tooltip,
			ItemStack stack,
			@Nullable Player player,
			boolean isAdvanced
		)
		{

		}
	}

	record Merger(List<AppendWhenImplantedTooltip> components) implements AppendWhenImplantedTooltip
	{
		public Merger(List<AppendWhenImplantedTooltip> components)
		{
			this.components = components;
			this.components.sort(Comparator.comparingInt(AppendWhenImplantedTooltip::whenImplantedTooltipPriority).reversed());
		}

		@Override
		public int whenImplantedTooltipPriority()
		{
			return 0;
		}

		@Override
		public void appendWhenImplantedTooltip(
			List<Component> tooltip,
			ItemStack stack,
			@Nullable Player player,
			boolean isAdvanced
		)
		{
//			int prevPriority = this.components.get(0).whenImplantedTooltipPriority();

			for (AppendWhenImplantedTooltip component : this.components)
			{
//				int priority = component.whenImplantedTooltipPriority();
//
//				if (priority / 1000 != prevPriority / 1000)
//				{
//					tooltip.add(CommonComponents.EMPTY);
//				}

				component.appendWhenImplantedTooltip(tooltip, stack, player, isAdvanced);
			}
		}
	}
}
