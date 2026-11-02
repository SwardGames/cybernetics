package com.sward.cybernetics.cybernetics.events.reaction;

import com.sward.cybernetics.cybernetics.Event;
import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableDouble;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public interface OnInflictingKnockback
{
	Event<OnInflictingKnockback> EVENT = Event.create(OnInflictingKnockback.class, (stack, entity, knockback) -> {}, Merger::new);

	void inflictingKnockback(ItemStack stack, LivingEntity entity, MutableDouble knockback);

	record Merger(List<OnInflictingKnockback> components) implements OnInflictingKnockback
	{
		@Override
		public void inflictingKnockback(ItemStack stack, LivingEntity entity, MutableDouble knockback)
		{
			for (OnInflictingKnockback component : this.components)
			{
				component.inflictingKnockback(stack, entity, knockback);
			}
		}
	}
}
