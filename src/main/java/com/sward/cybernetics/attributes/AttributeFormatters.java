package com.sward.cybernetics.attributes;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AttributeFormatters
{
	private static final Map<Attribute, AttributeFormatter> FORMATTERS = new Object2ObjectOpenHashMap<>();

	public static void register(Attribute attribute, AttributeFormatter formatter)
	{
		FORMATTERS.put(attribute, formatter);
	}

	public static @Nullable Component format(Attribute attribute, AttributeModifier modifier)
	{
		return FORMATTERS.getOrDefault(attribute, BasicAttributeFormatter.NORMAL).formatAttribute(attribute, modifier);
	}

	static
	{
		register(Attributes.KNOCKBACK_RESISTANCE, BasicAttributeFormatter.scaled(10D));
		register(CyberAttributes.MAX_ZOOM.get(), SuffixedAttributeFormatter.TIMES);
		register(CyberAttributes.MOTION_TRACKER_RANGE.get(), SuffixedAttributeFormatter.METERS);
		register(CyberAttributes.TRACKING_DURATION.get(), SuffixedAttributeFormatter.SECONDS);
		register(CyberAttributes.DODGE_CHANCE.get(), PercentageAttributeFormatter.NORMAL);
		register(CyberAttributes.CATCH_ARROW_CHANCE.get(), PercentageAttributeFormatter.NORMAL);
		register(CyberAttributes.POSITIVE_EFFECT_DURATION.get(), PercentageAttributeFormatter.NORMAL);
		register(CyberAttributes.NEGATIVE_EFFECT_DURATION.get(), PercentageAttributeFormatter.INVERTED);
	}
}
