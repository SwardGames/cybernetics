package com.sward.cybernetics.cybernetics.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sward.cybernetics.cybernetics.ICyberneticComponent;
import com.sward.cybernetics.cybernetics.events.display.AppendWhenImplantedTooltip;
import net.minecraft.ChatFormatting;
import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public record ApplyEffectsWhenHurtComponent(List<MobEffect> effects, int duration)
	implements ICyberneticComponent,  AppendWhenImplantedTooltip
{
	public static final Codec<ApplyEffectsWhenHurtComponent> CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
			ForgeRegistries.MOB_EFFECTS.getCodec().listOf().fieldOf("effects").forGetter(ApplyEffectsWhenHurtComponent::effects),
			Codec.INT.fieldOf("duration").forGetter(ApplyEffectsWhenHurtComponent::duration)
		).apply(instance, ApplyEffectsWhenHurtComponent::new)
	);

	@Override
	public Codec<? extends ICyberneticComponent> codec()
	{
		return CODEC;
	}

	@Override
	public int whenImplantedTooltipPriority()
	{
		return 100;
	}

	@Override
	public void appendWhenImplantedTooltip(
		List<Component> tooltip,
		ItemStack stack,
		@Nullable Player player,
		boolean isAdvanced
	)
	{

	}
}
