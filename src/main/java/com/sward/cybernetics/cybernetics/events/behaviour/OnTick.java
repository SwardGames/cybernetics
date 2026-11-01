package com.sward.cybernetics.cybernetics.events.behaviour;

import com.sward.cybernetics.cybernetics.Event;
import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public interface OnTick
{
	Event<OnTick> EVENT = Event.create(OnTick.class, (stack, entity) -> {}, Merger::new);

	void tick(ItemStack stack, LivingEntity entity);

	record Merger(List<OnTick> components) implements OnTick
	{
		@Override
		public void tick(ItemStack stack, LivingEntity entity)
		{
			for (OnTick component : components)
			{
				component.tick(stack, entity);
			}
		}
	}
}
