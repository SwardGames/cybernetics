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
public interface OnOtherKilled
{
	Event<OnOtherKilled> EVENT = Event.create(OnOtherKilled.class, (stack, event) -> {}, Merger::new);

	void otherKilled(ItemStack stack, LivingDeathEvent event);

	record Merger(List<OnOtherKilled> components) implements OnOtherKilled
	{
		@Override
		public void otherKilled(ItemStack stack, LivingDeathEvent event)
		{
			for (OnOtherKilled component : this.components)
			{
				component.otherKilled(stack, event);

				if (event.isCanceled())
				{
					return;
				}
			}
		}
	}
}
