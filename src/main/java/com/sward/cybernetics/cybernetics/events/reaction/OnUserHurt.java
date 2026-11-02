package com.sward.cybernetics.cybernetics.events.reaction;

import com.sward.cybernetics.cybernetics.Event;
import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public interface OnUserHurt
{
	Event<OnUserHurt> EVENT = Event.create(OnUserHurt.class, (stack, event) -> {}, Merger::new);

	void userHurt(ItemStack stack, LivingHurtEvent event);

	record Merger(List<OnUserHurt> components) implements OnUserHurt
	{
		@Override
		public void userHurt(ItemStack stack, LivingHurtEvent event)
		{
			for (OnUserHurt component : this.components)
			{
				component.userHurt(stack, event);

				if (event.isCanceled())
				{
					return;
				}
			}
		}
	}
}
