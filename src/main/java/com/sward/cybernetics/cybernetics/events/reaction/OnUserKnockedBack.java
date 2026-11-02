package com.sward.cybernetics.cybernetics.events.reaction;

import com.sward.cybernetics.cybernetics.Event;
import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public interface OnUserKnockedBack
{
	Event<OnUserKnockedBack> EVENT = Event.create(OnUserKnockedBack.class, (stack, event) -> {}, Merger::new);

	void userKnockedBack(ItemStack stack, LivingKnockBackEvent event);

	record Merger(List<OnUserKnockedBack> components) implements OnUserKnockedBack
	{
		@Override
		public void userKnockedBack(ItemStack stack, LivingKnockBackEvent event)
		{
			for (OnUserKnockedBack component : this.components)
			{
				component.userKnockedBack(stack, event);

				if (event.isCanceled())
				{
					return;
				}
			}
		}
	}
}
