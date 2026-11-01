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
public record BasicAttributeFormatter(
	double additiveScale,
	ChatFormatting positiveFormat,
	ChatFormatting negativeFormat
)
	implements AttributeFormatter
{
	public static final BasicAttributeFormatter NORMAL = new BasicAttributeFormatter(1D, BLUE, RED);
	public static final BasicAttributeFormatter INVERTED = new BasicAttributeFormatter(1D, RED, BLUE);

	public static BasicAttributeFormatter scaled(double scale)
	{
		return new BasicAttributeFormatter(scale, BLUE, RED);
	}

	@Override
	public @Nullable Component formatAttribute(Attribute attribute, AttributeModifier modifier)
	{
		double baseAmount = modifier.getAmount();
		AttributeModifier.Operation operation = modifier.getOperation();

		double displayedAmount;

		if (operation == AttributeModifier.Operation.ADDITION)
		{
			displayedAmount = baseAmount * this.additiveScale;
		}
		else
		{
			displayedAmount = baseAmount * 100D;
		}

		if (baseAmount > 0D)
		{
			return Component.translatable(
				"attribute.modifier.plus." + operation.toValue(),
				ATTRIBUTE_MODIFIER_FORMAT.format(displayedAmount),
				Component.translatable(attribute.getDescriptionId())
			).withStyle(this.positiveFormat);
		}
		else if (baseAmount < 0D)
		{
			return Component.translatable(
				"attribute.modifier.take." + operation.toValue(),
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
