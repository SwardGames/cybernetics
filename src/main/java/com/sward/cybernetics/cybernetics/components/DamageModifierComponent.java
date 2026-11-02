package com.sward.cybernetics.cybernetics.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sward.cybernetics.cybernetics.ICyberneticComponent;
import com.sward.cybernetics.cybernetics.events.display.AppendTooltip;
import com.sward.cybernetics.cybernetics.events.reaction.OnUserHurt;
import com.sward.cybernetics.tooltips.CyberneticTooltipCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;

import static net.minecraft.world.item.ItemStack.ATTRIBUTE_MODIFIER_FORMAT;

@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public record DamageModifierComponent(float amount, Optional<TagKey<DamageType>> damageTypeTag)
	implements ICyberneticComponent,
	OnUserHurt, AppendTooltip
{
	public static final Codec<DamageModifierComponent> CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
			Codec.FLOAT.fieldOf("amount").forGetter(DamageModifierComponent::amount),
			TagKey.codec(Registries.DAMAGE_TYPE)
				.optionalFieldOf("damage_type_tag")
				.forGetter(DamageModifierComponent::damageTypeTag)
		).apply(instance, DamageModifierComponent::new)
	);

	@Override
	public Codec<? extends ICyberneticComponent> codec()
	{
		return CODEC;
	}

	@Override
	public void userHurt(ItemStack stack, LivingHurtEvent event)
	{
		if (event.getSource().is(DamageTypeTags.BYPASSES_EFFECTS))
		{
			return;
		}

		if (event.getSource().is(DamageTypeTags.BYPASSES_ENCHANTMENTS))
		{
			return;
		}

		if (this.damageTypeTag.isPresent() && !event.getSource().is(this.damageTypeTag.get()))
		{
			return;
		}

		event.setAmount(event.getAmount() * (1F + this.amount));
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

		boolean isReduction = this.amount < 0D;

		if (this.damageTypeTag.isEmpty())
		{
			tooltip.add(
				Component.translatable(
					"tooltip.cybernetics.damage_modifier.generic." + isReduction,
					ATTRIBUTE_MODIFIER_FORMAT.format(Math.abs(this.amount * 100D))
				).withStyle(isReduction ? ChatFormatting.BLUE : ChatFormatting.RED)
			);
		}
		else
		{
			tooltip.add(
				Component.translatable(
					"tooltip.cybernetics.damage_modifier." + isReduction,
					ATTRIBUTE_MODIFIER_FORMAT.format(Math.abs(this.amount * 100D)),
					Component.translatable("tag." + this.damageTypeTag.get().location().toLanguageKey().replace('/', '.'))
				).withStyle(isReduction ? ChatFormatting.BLUE : ChatFormatting.RED)
			);
		}
	}
}
