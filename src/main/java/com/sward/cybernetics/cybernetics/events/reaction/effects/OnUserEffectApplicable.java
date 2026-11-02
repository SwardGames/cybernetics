package com.sward.cybernetics.cybernetics.events.reaction.effects;

import com.sward.cybernetics.cybernetics.Event;
import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.MobEffectEvent;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public interface OnUserEffectApplicable
{
	Event<OnUserEffectApplicable> EVENT = Event.create(OnUserEffectApplicable.class, (stack, event) -> {}, Merger::new);

	void userEffectApplicable(ItemStack stack, MobEffectEvent.Applicable event);

	record Merger(List<OnUserEffectApplicable> components) implements OnUserEffectApplicable
	{
		@Override
		public void userEffectApplicable(ItemStack stack, MobEffectEvent.Applicable event)
		{
			for (OnUserEffectApplicable component : this.components)
			{
				component.userEffectApplicable(stack, event);

				if (event.isCanceled())
				{
					return;
				}
			}
		}
	}
}
