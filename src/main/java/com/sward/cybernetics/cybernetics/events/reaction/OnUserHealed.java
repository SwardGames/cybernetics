package com.sward.cybernetics.cybernetics.events.reaction;

import com.sward.cybernetics.cybernetics.Event;
import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHealEvent;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public interface OnUserHealed
{
	Event<OnUserHealed> EVENT = Event.create(OnUserHealed.class, (stack, event) -> {}, Merger::new);

	void userHealed(ItemStack stack, LivingHealEvent event);

	record Merger(List<OnUserHealed> components) implements OnUserHealed
	{
		@Override
		public void userHealed(ItemStack stack, LivingHealEvent event)
		{
			for (OnUserHealed component : this.components)
			{
				component.userHealed(stack, event);

				if (event.isCanceled())
				{
					return;
				}
			}
		}
	}
}
