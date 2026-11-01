package com.sward.cybernetics.cybernetics.components;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sward.cybernetics.cybernetics.ICyberneticComponent;
import com.sward.cybernetics.cybernetics.events.implanting.OnImplanted;
import com.sward.cybernetics.cybernetics.events.display.AppendModifierTooltip;
import com.sward.cybernetics.cybernetics.events.implanting.OnUnimplanted;
import com.sward.cybernetics.util.AttributeModifierCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static net.minecraft.world.item.ItemStack.ATTRIBUTE_MODIFIER_FORMAT;

@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public record AttributeModifiersComponent(
	List<AttributeModifierDefinition> modifiersList,
	Multimap<Attribute, AttributeModifier> modifiers
) implements ICyberneticComponent, OnImplanted, OnUnimplanted, AppendModifierTooltip
{
	public static final Codec<AttributeModifiersComponent> CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
			AttributeModifierDefinition.CODEC.listOf()
				.fieldOf("modifiers")
				.forGetter(AttributeModifiersComponent::modifiersList)
		).apply(instance, AttributeModifiersComponent::new)
	);

	private AttributeModifiersComponent(List<AttributeModifierDefinition> modifiersList)
	{
		this(modifiersList, makeMultimap(modifiersList));
	}

	@Override
	public Codec<? extends ICyberneticComponent> codec()
	{
		return CODEC;
	}

	@Override
	public void implanted(ItemStack stack, LivingEntity entity)
	{
		entity.getAttributes().addTransientAttributeModifiers(this.modifiers);
	}

	@Override
	public void unimplanted(ItemStack stack, LivingEntity entity)
	{
		entity.getAttributes().removeAttributeModifiers(this.modifiers);
	}

	@Override
	public int modifierTooltipPriority()
	{
		return 2000;
	}

	@Override
	public void appendModifierTooltip(List<Component> tooltip, ItemStack stack, @Nullable Player player, boolean isAdvanced)
	{
		for (AttributeModifierDefinition definition : this.modifiersList)
		{
			Attribute attribute = definition.attribute;
			double baseAmount = definition.amount;
			Operation operation = definition.operation;

			double displayedAmount;

			if (operation != Operation.MULTIPLY_BASE && operation != Operation.MULTIPLY_TOTAL)
			{
				if (attribute.equals(Attributes.KNOCKBACK_RESISTANCE))
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
				tooltip.add(Component.translatable(
					"attribute.modifier.plus." + operation.toValue(),
					ATTRIBUTE_MODIFIER_FORMAT.format(displayedAmount),
					Component.translatable(attribute.getDescriptionId())
				).withStyle(ChatFormatting.BLUE));
			}
			else if (baseAmount < 0D)
			{
				displayedAmount *= -1;

				tooltip.add(Component.translatable(
					"attribute.modifier.take." + operation.toValue(),
					ATTRIBUTE_MODIFIER_FORMAT.format(displayedAmount),
					Component.translatable(attribute.getDescriptionId())
				).withStyle(ChatFormatting.RED));
			}
		}
	}

	private static Multimap<Attribute, AttributeModifier> makeMultimap(List<AttributeModifierDefinition> modifiersList)
	{
		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

		for (AttributeModifierDefinition modifier : modifiersList)
		{
			builder.put(
				modifier.attribute,
				new AttributeModifier(
					"cybernetics.component.attribute_modifier." + modifier.attribute.getDescriptionId(),
					modifier.amount,
					modifier.operation
				)
			);
		}

		return builder.build();
	}

	private record AttributeModifierDefinition(
		Attribute attribute,
		double amount,
		AttributeModifier.Operation operation
	)
	{
		public static final Codec<AttributeModifierDefinition> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
				ForgeRegistries.ATTRIBUTES.getCodec()
					.fieldOf("attribute")
					.forGetter(AttributeModifierDefinition::attribute),
				Codec.DOUBLE
					.fieldOf("amount")
					.forGetter(AttributeModifierDefinition::amount),
				AttributeModifierCodec.OPERATION
					.optionalFieldOf("operation", Operation.ADDITION)
					.forGetter(AttributeModifierDefinition::operation)
			).apply(instance, AttributeModifierDefinition::new)
		);
	}
}
