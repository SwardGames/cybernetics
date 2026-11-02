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
public interface OnUserEffectExpired
{
	Event<OnUserEffectExpired> EVENT = Event.create(OnUserEffectExpired.class, (stack, event) -> {}, Merger::new);

	void userEffectExpired(ItemStack stack, MobEffectEvent.Expired event);

	record Merger(List<OnUserEffectExpired> components) implements OnUserEffectExpired
	{
		@Override
		public void userEffectExpired(ItemStack stack, MobEffectEvent.Expired event)
		{
			for (OnUserEffectExpired component : this.components)
			{
				component.userEffectExpired(stack, event);

				if (event.isCanceled())
				{
					return;
				}
			}
		}
	}
}
