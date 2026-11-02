package com.sward.cybernetics.compat.curios.components;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sward.cybernetics.AddonsPresent;
import com.sward.cybernetics.cybernetics.ICyberneticComponent;
import com.sward.cybernetics.cybernetics.events.display.AppendTooltip;
import com.sward.cybernetics.cybernetics.events.implanting.OnImplanted;
import com.sward.cybernetics.cybernetics.events.implanting.OnUnimplanted;
import com.sward.cybernetics.tooltips.CyberneticTooltipCategory;
import com.sward.cybernetics.util.AttributeModifierCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static net.minecraft.world.item.ItemStack.ATTRIBUTE_MODIFIER_FORMAT;

@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public record CuriosSlotsComponent(
	List<SlotModifierDefinition> slotsList,
	Multimap<String, AttributeModifier> slotModifiers
) implements ICyberneticComponent, OnImplanted, OnUnimplanted, AppendTooltip
{
	public static final Codec<CuriosSlotsComponent> CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
			SlotModifierDefinition.CODEC.listOf()
				.fieldOf("slots")
				.forGetter(CuriosSlotsComponent::slotsList)
		).apply(instance, CuriosSlotsComponent::new)
	);

	private CuriosSlotsComponent(List<SlotModifierDefinition> modifiersList)
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
		if (!AddonsPresent.CURIOS)
		{
			return;
		}

		CuriosApi.getCuriosInventory(entity).ifPresent(i -> i.addTransientSlotModifiers(this.slotModifiers));
	}

	@Override
	public void unimplanted(ItemStack stack, LivingEntity entity)
	{
		if (!AddonsPresent.CURIOS)
		{
			return;
		}

		CuriosApi.getCuriosInventory(entity).ifPresent(i -> i.removeSlotModifiers(this.slotModifiers));
	}

	@Override
	public void appendTooltip(CyberneticTooltipCategory category, List<Component> tooltip, ItemStack stack, @Nullable Player player, boolean isAdvanced)
	{
		if (category != CyberneticTooltipCategory.WHEN_IMPLANTED)
		{
			return;
		}

		for (SlotModifierDefinition definition : this.slotsList)
		{
			String slot = definition.slot;
			double baseAmount = definition.amount;
			Operation operation = definition.operation;

			double displayedAmount;

			if (operation != Operation.MULTIPLY_BASE && operation != Operation.MULTIPLY_TOTAL)
			{
				displayedAmount = baseAmount;
			}
			else
			{
				displayedAmount = baseAmount * 100D;
			}

			if (baseAmount > 0D)
			{
				tooltip.add(Component.translatable(
					"curios.modifiers.slots.plus." + operation.toValue(),
					ATTRIBUTE_MODIFIER_FORMAT.format(displayedAmount),
					Component.translatable("curios.identifier." + slot)
				).withStyle(ChatFormatting.BLUE));
			}
			else if (baseAmount < 0D)
			{
				displayedAmount *= -1;

				tooltip.add(Component.translatable(
					"curios.modifiers.slots.take." + operation.toValue(),
					ATTRIBUTE_MODIFIER_FORMAT.format(displayedAmount),
					Component.translatable("curios.identifier." + slot)
				).withStyle(ChatFormatting.RED));
			}
		}
	}

	private static Multimap<String, AttributeModifier> makeMultimap(List<SlotModifierDefinition> modifiersList)
	{
		ImmutableMultimap.Builder<String, AttributeModifier> builder = ImmutableMultimap.builder();

		for (SlotModifierDefinition modifier : modifiersList)
		{
			builder.put(
				modifier.slot,
				new AttributeModifier(
					"cybernetics.component.curios_slot." + modifier.slot,
					modifier.amount,
					modifier.operation
				)
			);
		}

		return builder.build();
	}

	private record SlotModifierDefinition(String slot, int amount, AttributeModifier.Operation operation)
	{
		public static final Codec<SlotModifierDefinition> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
				Codec.STRING
					.fieldOf("slot")
					.forGetter(SlotModifierDefinition::slot),
				Codec.INT
					.fieldOf("amount")
					.forGetter(SlotModifierDefinition::amount),
				AttributeModifierCodec.OPERATION
					.optionalFieldOf("operation", Operation.ADDITION)
					.forGetter(SlotModifierDefinition::operation)
			).apply(instance, SlotModifierDefinition::new)
		);
	}
}
