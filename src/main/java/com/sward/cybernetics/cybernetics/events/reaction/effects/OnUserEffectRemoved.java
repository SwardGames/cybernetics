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
public interface OnUserEffectRemoved
{
	Event<OnUserEffectRemoved> EVENT = Event.create(OnUserEffectRemoved.class, (stack, event) -> {}, Merger::new);

	void userEffectRemoved(ItemStack stack, MobEffectEvent.Remove event);

	record Merger(List<OnUserEffectRemoved> components) implements OnUserEffectRemoved
	{
		@Override
		public void userEffectRemoved(ItemStack stack, MobEffectEvent.Remove event)
		{
			for (OnUserEffectRemoved component : this.components)
			{
				component.userEffectRemoved(stack, event);

				if (event.isCanceled())
				{
					return;
				}
			}
		}
	}
}
