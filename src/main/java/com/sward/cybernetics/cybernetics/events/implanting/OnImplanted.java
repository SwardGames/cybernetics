package com.sward.cybernetics.cybernetics.events.implanting;

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
public interface OnImplanted
{
	Event<OnImplanted> EVENT = Event.create(OnImplanted.class, (stack, entity) -> {}, Merger::new);

	void implanted(ItemStack stack, LivingEntity entity);

	record Merger(List<OnImplanted> components) implements OnImplanted
	{
		@Override
		public void implanted(ItemStack stack, LivingEntity entity)
		{
			for (OnImplanted component : components)
			{
				component.implanted(stack, entity);
			}
		}
	}
}
