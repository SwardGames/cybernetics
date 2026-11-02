package com.sward.cybernetics.tooltips;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;

public record CyberneticTooltipCategory(int priority, @Nullable Component title)
{
	public static final CyberneticTooltipCategory FLAVOR = CyberneticsTooltips.registerCategory(10000);

	public static final CyberneticTooltipCategory REQUIREMENTS = CyberneticsTooltips.registerCategory(8000);

	public static final CyberneticTooltipCategory WHEN_IMPLANTED = CyberneticsTooltips.registerCategory(
		5000,
		Component.translatable("tooltip.cybernetics.when_implanted").withStyle(ChatFormatting.BLUE)
	);

	public static final CyberneticTooltipCategory WHEN_IMPLANTED_WHEN_HURT = CyberneticsTooltips.registerCategory(
		4000,
		Component.translatable("tooltip.cybernetics.when_implanted_and_hurt").withStyle(ChatFormatting.BLUE)
	);

	public static void register()
	{

	}
}
