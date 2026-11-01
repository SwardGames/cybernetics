package com.sward.cybernetics.attributes;

import net.minecraft.ChatFormatting;
import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import static net.minecraft.ChatFormatting.*;
import static net.minecraft.world.item.ItemStack.ATTRIBUTE_MODIFIER_FORMAT;

@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public record PercentageAttributeFormatter(ChatFormatting positiveFormat, ChatFormatting negativeFormat)
	implements AttributeFormatter
{
	public static final PercentageAttributeFormatter NORMAL = new PercentageAttributeFormatter(BLUE, RED);
	public static final PercentageAttributeFormatter INVERTED = new PercentageAttributeFormatter(RED, BLUE);

	@Override
	public @Nullable Component formatAttribute(Attribute attribute, AttributeModifier modifier)
	{
		double baseAmount = modifier.getAmount();
		AttributeModifier.Operation operation = modifier.getOperation();

		double displayedAmount = baseAmount * 100D;

		if (baseAmount > 0D)
		{
			return Component.translatable(
				"attribute.modifier.plus.2",
				ATTRIBUTE_MODIFIER_FORMAT.format(displayedAmount),
				Component.translatable(attribute.getDescriptionId())
			).withStyle(this.positiveFormat);
		}
		else if (baseAmount < 0D)
		{
			return Component.translatable(
				"attribute.modifier.take.2",
				ATTRIBUTE_MODIFIER_FORMAT.format(-displayedAmount),
				Component.translatable(attribute.getDescriptionId())
			).withStyle(this.negativeFormat);
		}
		else
		{
			return null;
		}
	}
}
