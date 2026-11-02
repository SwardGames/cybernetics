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
public interface OnOtherHurt
{
	Event<OnOtherHurt> EVENT = Event.create(OnOtherHurt.class, (stack, event) -> {}, Merger::new);

	void otherHurt(ItemStack stack, LivingHurtEvent event);

	record Merger(List<OnOtherHurt> components) implements OnOtherHurt
	{
		@Override
		public void otherHurt(ItemStack stack, LivingHurtEvent event)
		{
			for (OnOtherHurt component : this.components)
			{
				component.otherHurt(stack, event);

				if (event.isCanceled())
				{
					return;
				}
			}
		}
	}
}
