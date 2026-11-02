package com.sward.cybernetics.cybernetics.events.reaction;

import com.sward.cybernetics.cybernetics.Event;
import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public interface OnUserKilled
{
	Event<OnUserKilled> EVENT = Event.create(OnUserKilled.class, (stack, event) -> {}, Merger::new);

	void userKilled(ItemStack stack, LivingDeathEvent event);

	record Merger(List<OnUserKilled> components) implements OnUserKilled
	{
		@Override
		public void userKilled(ItemStack stack, LivingDeathEvent event)
		{
			for (OnUserKilled component : this.components)
			{
				component.userKilled(stack, event);

				if (event.isCanceled())
				{
					return;
				}
			}
		}
	}
}
