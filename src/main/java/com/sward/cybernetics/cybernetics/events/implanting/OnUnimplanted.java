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
public interface OnUnimplanted
{
	Event<OnUnimplanted> EVENT = Event.create(OnUnimplanted.class, (stack, entity) -> {}, Merger::new);

	void unimplanted(ItemStack stack, LivingEntity entity);

	record Merger(List<OnUnimplanted> components) implements OnUnimplanted
	{
		@Override
		public void unimplanted(ItemStack stack, LivingEntity entity)
		{
			for (OnUnimplanted component : components)
			{
				component.unimplanted(stack, entity);
			}
		}
	}
}
