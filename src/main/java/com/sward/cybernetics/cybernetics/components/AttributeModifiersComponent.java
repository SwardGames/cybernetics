package com.sward.cybernetics.cybernetics.components;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sward.cybernetics.attributes.AttributeFormatters;
import com.sward.cybernetics.cybernetics.ICyberneticComponent;
import com.sward.cybernetics.cybernetics.events.display.AppendTooltip;
import com.sward.cybernetics.cybernetics.events.implanting.OnImplanted;
import com.sward.cybernetics.cybernetics.events.implanting.OnUnimplanted;
import com.sward.cybernetics.tooltips.CyberneticTooltipCategory;
import com.sward.cybernetics.util.AttributeModifierCodec;
import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Map;

@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public record AttributeModifiersComponent(
	List<AttributeModifierDefinition> modifiersList,
	Multimap<Attribute, AttributeModifier> modifiers
) implements ICyberneticComponent, OnImplanted, OnUnimplanted, AppendTooltip
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
	public void appendTooltip(
		CyberneticTooltipCategory category,
		List<Component> tooltip,
		ItemStack stack,
		@Nullable Player player,
		boolean isAdvanced
	)
	{
		if (category != CyberneticTooltipCategory.WHEN_IMPLANTED)
		{
			return;
		}

		for (Map.Entry<Attribute, AttributeModifier> entry : this.modifiers.entries())
		{
			Component component = AttributeFormatters.format(entry.getKey(), entry.getValue());

			if (component != null)
			{
				tooltip.add(component);
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
