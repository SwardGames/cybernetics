package com.sward.cybernetics.cybernetics.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sward.cybernetics.cybernetics.ICyberneticComponent;
import com.sward.cybernetics.cybernetics.events.display.AppendWhenImplantedTooltip;
import net.minecraft.ChatFormatting;
import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public record WhenImplantedTooltipComponent(String text)
	implements ICyberneticComponent, AppendWhenImplantedTooltip
{
	public static final Codec<WhenImplantedTooltipComponent> CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
			Codec.STRING.fieldOf("text").forGetter(WhenImplantedTooltipComponent::text)
		).apply(instance, WhenImplantedTooltipComponent::new)
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
		tooltip.add(Component.translatable(this.text).withStyle(ChatFormatting.BLUE));
	}
}
