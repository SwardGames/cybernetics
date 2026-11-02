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
public interface OnUserEffectAdded
{
	Event<OnUserEffectAdded> EVENT = Event.create(OnUserEffectAdded.class, (stack, event) -> {}, Merger::new);

	void userEffectAdded(ItemStack stack, MobEffectEvent.Added event);

	record Merger(List<OnUserEffectAdded> components) implements OnUserEffectAdded
	{
		@Override
		public void userEffectAdded(ItemStack stack, MobEffectEvent.Added event)
		{
			for (OnUserEffectAdded component : this.components)
			{
				component.userEffectAdded(stack, event);

				if (event.isCanceled())
				{
					return;
				}
			}
		}
	}
}
