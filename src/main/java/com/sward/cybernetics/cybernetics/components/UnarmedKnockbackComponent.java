package com.sward.cybernetics.cybernetics.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sward.cybernetics.cybernetics.ICyberneticComponent;
import com.sward.cybernetics.cybernetics.events.display.AppendWhenImplantedTooltip;
import com.sward.cybernetics.cybernetics.events.reaction.OnInflictingKnockback;
import net.minecraft.ChatFormatting;
import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableDouble;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static net.minecraft.world.item.ItemStack.ATTRIBUTE_MODIFIER_FORMAT;

@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public record UnarmedKnockbackComponent(float strength)
	implements ICyberneticComponent, OnInflictingKnockback, AppendWhenImplantedTooltip
{
	public static final Codec<UnarmedKnockbackComponent> CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
			Codec.FLOAT.fieldOf("strength").forGetter(UnarmedKnockbackComponent::strength)
		).apply(instance, UnarmedKnockbackComponent::new)
	);

	@Override
	public Codec<? extends ICyberneticComponent> codec()
	{
		return CODEC;
	}

	@Override
	public void inflictingKnockback(ItemStack stack, LivingEntity entity, MutableDouble knockback)
	{
		if (entity.getMainHandItem().isEmpty())
		{
			knockback.add(strength);
		}
	}

	@Override
	public int whenImplantedTooltipPriority()
	{
		return 500;
	}

	@Override
	public void appendWhenImplantedTooltip(
		List<Component> tooltip,
		ItemStack stack,
		@Nullable Player player,
		boolean isAdvanced
	)
	{
		tooltip.add(
			Component.translatable(
				"tooltip.cybernetics.knockback",
				ATTRIBUTE_MODIFIER_FORMAT.format(this.strength)
			).withStyle(ChatFormatting.BLUE)
		);
	}
}
