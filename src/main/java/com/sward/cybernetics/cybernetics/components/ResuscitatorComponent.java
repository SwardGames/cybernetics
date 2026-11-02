package com.sward.cybernetics.cybernetics.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sward.cybernetics.cybernetics.ICyberneticComponent;
import com.sward.cybernetics.cybernetics.events.display.AppendTooltip;
import com.sward.cybernetics.cybernetics.events.reaction.OnUserKilled;
import com.sward.cybernetics.tooltips.CyberneticTooltipCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static net.minecraft.world.item.ItemStack.ATTRIBUTE_MODIFIER_FORMAT;

@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public record ResuscitatorComponent(int cooldown, int restoredHealth) implements ICyberneticComponent,
	OnUserKilled, AppendTooltip
{
	public static final Codec<ResuscitatorComponent> CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
			Codec.INT.fieldOf("cooldown").forGetter(ResuscitatorComponent::cooldown),
			Codec.INT.optionalFieldOf("restored_health", 1).forGetter(ResuscitatorComponent::restoredHealth)
		).apply(instance, ResuscitatorComponent::new)
	);

	@Override
	public Codec<? extends ICyberneticComponent> codec()
	{
		return CODEC;
	}

	@Override
	public void userKilled(ItemStack stack, LivingDeathEvent event)
	{
		if (event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY))
		{
			return;
		}

		if (event.getEntity() instanceof Player p)
		{
			if (!p.getCooldowns().isOnCooldown(stack.getItem()))
			{
				p.getCooldowns().addCooldown(stack.getItem(), this.cooldown);

				p.setHealth(this.restoredHealth);

				event.cancel();
			}
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
				"tooltip.cybernetics.resuscitator",
				ATTRIBUTE_MODIFIER_FORMAT.format(this.restoredHealth / 2D),
				ATTRIBUTE_MODIFIER_FORMAT.format(this.cooldown / 20D)
			).withStyle(ChatFormatting.BLUE)
		);
	}
}
