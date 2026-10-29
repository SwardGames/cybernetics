package com.sward.cybernetics.items;

import com.google.common.collect.ImmutableMultimap;
import com.sward.cybernetics.data.CyberneticSlotType;
import net.minecraft.ChatFormatting;
import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Map;

import static net.minecraft.world.item.ItemStack.ATTRIBUTE_MODIFIER_FORMAT;

@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CyberneticItem extends Item
{
	public final String slotGroup;

	public CyberneticSlotType slotType;

	@Nullable
	public final TagKey<Item> requiredTag;

	@Nullable
	public final TagKey<Item> forbiddenTag;

	public final int capacityCost;

	public final ImmutableMultimap<Attribute, AttributeModifier> attributes;

	public CyberneticItem(
		String slotGroup,
		CyberneticSlotType slotType,
		@Nullable TagKey<Item> requiredTag,
		@Nullable TagKey<Item> forbiddenTag,
		int capacityCost,
		ImmutableMultimap<Attribute, AttributeModifier> attributes,
		Properties properties
	)
	{
		super(properties);

		this.slotGroup = slotGroup;
		this.slotType = slotType;

		this.requiredTag = requiredTag;
		this.forbiddenTag = forbiddenTag;

		this.capacityCost = capacityCost;
		this.attributes = attributes;
	}

	public boolean validate(ItemStack stack, Container inv)
	{
		if (inv.hasAnyMatching(s -> stack != s && s.is(this)))
		{
			return false;
		}

		if (this.requiredTag != null)
		{
			if (!inv.hasAnyMatching(s -> stack != s && s.is(this.requiredTag)))
			{
				return false;
			}
		}

		if (this.forbiddenTag != null)
		{
			if (inv.hasAnyMatching(s -> stack != s && s.is(this.forbiddenTag)))
			{
				return false;
			}
		}

		return true;
	}

	@Override
	public void appendHoverText(
		ItemStack stack,
		@Nullable Level level,
		List<Component> components,
		TooltipFlag isAdvanced
	)
	{
		components.add(
			Component.translatable(
				"tooltip.cybernetics.slot",
				Component.translatable("cybernetics_group." + this.slotGroup)
			).withStyle(ChatFormatting.GOLD)
		);

		if (this.requiredTag != null)
		{
			ResourceLocation tagLocation = this.requiredTag.location();

			components.add(
				Component.translatable(
					"tooltip.cybernetics.required",
					Component.translatableWithFallback(
						"tag." + tagLocation.getNamespace() + "." + tagLocation.getPath().replace('/', '.'),
						"#" + tagLocation
					)
				).withStyle(ChatFormatting.RED)
			);
		}

		if (this.forbiddenTag != null)
		{
			ResourceLocation tagLocation = this.forbiddenTag.location();

			components.add(
				Component.translatable(
					"tooltip.cybernetics.forbidden",
					Component.translatableWithFallback(
						"tag." + tagLocation.getNamespace() + "." + tagLocation.getPath().replace('/', '.'),
						"#" + tagLocation
					)
				).withStyle(ChatFormatting.RED)
			);
		}

		if (this.capacityCost != 0)
		{
			components.add(CommonComponents.EMPTY);
			components.add(
				Component.translatable("tooltip.cybernetics.capacity_cost", this.capacityCost)
					.withStyle(this.capacityCost > 0 ? ChatFormatting.RED : ChatFormatting.BLUE)
			);
		}

		if (!this.attributes.isEmpty())
		{
			components.add(CommonComponents.EMPTY);
			components.add(Component.translatable("tooltip.cybernetics.modifiers").withStyle(ChatFormatting.GRAY));

			for (Map.Entry<Attribute, AttributeModifier> entry : this.attributes.entries())
			{
				AttributeModifier attributemodifier = entry.getValue();
				double baseAmount = attributemodifier.getAmount();

				double displayedAmount;

				if (attributemodifier.getOperation() != AttributeModifier.Operation.MULTIPLY_BASE && attributemodifier.getOperation() != AttributeModifier.Operation.MULTIPLY_TOTAL)
				{
					if (entry.getKey().equals(Attributes.KNOCKBACK_RESISTANCE))
					{
						displayedAmount = baseAmount * 10D;
					}
					else
					{
						displayedAmount = baseAmount;
					}
				}
				else
				{
					displayedAmount = baseAmount * 100D;
				}

				if (baseAmount > 0D)
				{
					components.add(Component.translatable(
						"attribute.modifier.plus." + attributemodifier.getOperation()
							.toValue(),
						ATTRIBUTE_MODIFIER_FORMAT.format(displayedAmount),
						Component.translatable(entry.getKey().getDescriptionId())
					).withStyle(ChatFormatting.BLUE));
				}
				else if (baseAmount < 0D)
				{
					displayedAmount *= -1;

					components.add(Component.translatable(
						"attribute.modifier.take." + attributemodifier.getOperation()
							.toValue(),
						ATTRIBUTE_MODIFIER_FORMAT.format(displayedAmount),
						Component.translatable(entry.getKey().getDescriptionId())
					).withStyle(ChatFormatting.RED));
				}
			}
		}
	}
}
