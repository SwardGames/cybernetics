package com.sward.cybernetics.entities;

import com.sward.cybernetics.attributes.CyberAttributes;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;

public class EffectHelper
{
	public static int getScaledEffectDuration(LivingEntity entity, MobEffectInstance effectInstance)
	{
		Attribute attribute = effectInstance.getEffect().getCategory() == MobEffectCategory.HARMFUL
			? CyberAttributes.NEGATIVE_EFFECT_DURATION.get()
			: CyberAttributes.POSITIVE_EFFECT_DURATION.get();

		if (!entity.getAttributes().hasAttribute(attribute))
		{
			return effectInstance.duration;
		}

		double multiplier = Math.max(entity.getAttributeValue(attribute), 0D);

		if (effectInstance.isInfiniteDuration() && multiplier > 0D)
		{
			return -1;
		}

		return (int) (effectInstance.duration * multiplier);
	}
}
