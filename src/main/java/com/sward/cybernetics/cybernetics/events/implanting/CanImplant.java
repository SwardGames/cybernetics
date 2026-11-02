package com.sward.cybernetics.cybernetics.events.implanting;

import com.sward.cybernetics.cybernetics.Event;
import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.Container;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public interface CanImplant
{
	Event<CanImplant> EVENT = Event.create(CanImplant.class, (stack, entity, inv) -> true, Merger::new);

	boolean canImplant(ItemStack stack, LivingEntity entity, Container inv);

	record Merger(List<CanImplant> components) implements CanImplant
	{
		@Override
		public boolean canImplant(ItemStack stack, LivingEntity entity, Container inv)
		{
			for (CanImplant component : this.components)
			{
				if (!component.canImplant(stack, entity, inv))
				{
					return false;
				}
			}

			return true;
		}
	}
}
