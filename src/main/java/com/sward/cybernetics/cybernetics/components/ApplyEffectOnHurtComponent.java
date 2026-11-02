package com.sward.cybernetics.cybernetics.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sward.cybernetics.cybernetics.ICyberneticComponent;
import com.sward.cybernetics.cybernetics.events.display.AppendTooltip;
import com.sward.cybernetics.cybernetics.events.reaction.OnUserHurt;
import com.sward.cybernetics.tooltips.CyberneticTooltipCategory;
import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.StringUtil;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public record ApplyEffectOnHurtComponent(MobEffect effect, int duration, int amplifier, boolean hideParticles)
	implements ICyberneticComponent, OnUserHurt, AppendTooltip
{
	public static final Codec<ApplyEffectOnHurtComponent> CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
			ForgeRegistries.MOB_EFFECTS.getCodec().fieldOf("effect").forGetter(ApplyEffectOnHurtComponent::effect),
			Codec.INT.fieldOf("duration").forGetter(ApplyEffectOnHurtComponent::duration),
			Codec.INT.optionalFieldOf("amplifier", 0).forGetter(ApplyEffectOnHurtComponent::amplifier),
			Codec.BOOL.optionalFieldOf("hide_particles", false).forGetter(ApplyEffectOnHurtComponent::hideParticles)
		).apply(instance, ApplyEffectOnHurtComponent::new)
	);

	@Override
	public Codec<? extends ICyberneticComponent> codec()
	{
		return CODEC;
	}

	@Override
	public void userHurt(ItemStack stack, LivingHurtEvent event)
	{
		event.getEntity().addEffect(
			new MobEffectInstance(this.effect, this.duration, this.amplifier, false, !this.hideParticles)
		);
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
		if (category != CyberneticTooltipCategory.WHEN_IMPLANTED_WHEN_HURT)
		{
			return;
		}

		MutableComponent component = Component.translatable(this.effect.getDescriptionId());

		if (this.amplifier > 0)
		{
			component = Component.translatable(
				"potion.withAmplifier",
				component,
				Component.translatable("potion.potency." + this.amplifier)
			);
		}

		if (this.duration == -1)
		{
			component = Component.translatable(
				"potion.withDuration",
				component,
				Component.translatable("effect.duration.infinite")
			);
		}
		else if (this.duration > 20)
		{
			component = Component.translatable(
				"potion.withDuration",
				component,
				Component.literal(StringUtil.formatTickDuration(this.duration))
			);
		}

		tooltip.add(
			Component.translatable(
				"tooltip.cybernetics.apply_effect",
				component
			).withStyle(this.effect.getCategory().getTooltipFormatting())
		);
	}
}
