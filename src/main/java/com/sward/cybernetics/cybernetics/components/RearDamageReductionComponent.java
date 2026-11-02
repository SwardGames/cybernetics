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
import net.minecraft.network.chat.Component;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static net.minecraft.world.item.ItemStack.ATTRIBUTE_MODIFIER_FORMAT;

@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public record RearDamageReductionComponent(float amount) implements ICyberneticComponent,
	OnUserHurt, AppendTooltip
{
	public static final Codec<RearDamageReductionComponent> CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
			Codec.FLOAT.fieldOf("amount").forGetter(RearDamageReductionComponent::amount)
		).apply(instance, RearDamageReductionComponent::new)
	);

	@Override
	public Codec<? extends ICyberneticComponent> codec()
	{
		return CODEC;
	}

	@Override
	public void userHurt(ItemStack stack, LivingHurtEvent event)
	{
		DamageSource source = event.getSource();

		if (source.is(DamageTypeTags.BYPASSES_RESISTANCE))
		{
			return;
		}

		Vec3 from = source.getSourcePosition();

		if (from == null)
		{
			return;
		}

		LivingEntity user = event.getEntity();

		double dot = user.position().subtract(from).dot(Vec3.directionFromRotation(0F, user.getYRot()));

		if (dot > 0F)
		{
			event.setAmount(event.getAmount() * (1F - this.amount));
		}
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

		tooltip.add(
			Component.translatable(
				"tooltip.cybernetics.rear_damage_reduction",
				ATTRIBUTE_MODIFIER_FORMAT.format(this.amount * 100D)
			).withStyle(ChatFormatting.BLUE)
		);
	}
}
