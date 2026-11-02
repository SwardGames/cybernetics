package com.sward.cybernetics.cybernetics.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sward.cybernetics.cybernetics.ICyberneticComponent;
import com.sward.cybernetics.cybernetics.events.display.AppendTooltip;
import com.sward.cybernetics.tooltips.CyberneticTooltipCategory;
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
public record DescriptionComponent(String text) implements ICyberneticComponent, AppendTooltip
{
	public static final Codec<DescriptionComponent> CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
			Codec.STRING.fieldOf("text").forGetter(DescriptionComponent::text)
		).apply(instance, DescriptionComponent::new)
	);

	@Override
	public Codec<? extends ICyberneticComponent> codec()
	{
		return CODEC;
	}

	@Override
	public void appendTooltip(CyberneticTooltipCategory category, List<Component> tooltip, ItemStack stack, @Nullable Player player, boolean isAdvanced)
	{
		if (category == CyberneticTooltipCategory.FLAVOR)
		{
			tooltip.add(Component.translatable(this.text).withStyle(ChatFormatting.GRAY));
		}
	}
}
