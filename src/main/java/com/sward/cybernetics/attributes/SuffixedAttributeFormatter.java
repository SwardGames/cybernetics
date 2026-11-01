package com.sward.cybernetics.attributes;

import net.minecraft.ChatFormatting;
import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import static net.minecraft.ChatFormatting.BLUE;
import static net.minecraft.ChatFormatting.RED;
import static net.minecraft.world.item.ItemStack.ATTRIBUTE_MODIFIER_FORMAT;

@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public record SuffixedAttributeFormatter(String suffix, ChatFormatting positiveFormat, ChatFormatting negativeFormat)
	implements AttributeFormatter
{
	public static final SuffixedAttributeFormatter METERS = normal("m");
	public static final SuffixedAttributeFormatter SECONDS = normal("s");
	public static final SuffixedAttributeFormatter TIMES = normal("x");

	public static SuffixedAttributeFormatter normal(String suffix)
	{
		return new SuffixedAttributeFormatter(suffix, BLUE, RED);
	}

	public static SuffixedAttributeFormatter inverted(String suffix)
	{
		return new SuffixedAttributeFormatter(suffix, RED, BLUE);
	}

	@Override
	public @Nullable Component formatAttribute(Attribute attribute, AttributeModifier modifier)
	{
		double baseAmount = modifier.getAmount();
		AttributeModifier.Operation operation = modifier.getOperation();

		double displayedAmount;

		if (operation == AttributeModifier.Operation.ADDITION)
		{
			displayedAmount = baseAmount;
		}
		else
		{
			displayedAmount = baseAmount * 100D;
		}

		if (baseAmount > 0D)
		{
			return Component.translatable(
				"attribute.modifier.plus." + operation.toValue(),
				ATTRIBUTE_MODIFIER_FORMAT.format(displayedAmount) + this.suffix,
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
