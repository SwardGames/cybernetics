package com.sward.cybernetics.cybernetics.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sward.cybernetics.cybernetics.ICyberneticComponent;
import com.sward.cybernetics.cybernetics.events.implanting.CanImplant;
import com.sward.cybernetics.cybernetics.events.display.AppendTooltip;
import com.sward.cybernetics.tooltips.CyberneticTooltipCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public record IncompatibleImplantTagComponent(TagKey<Item> tag) implements ICyberneticComponent,
	CanImplant, AppendTooltip
{
	public static final Codec<IncompatibleImplantTagComponent> CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
			TagKey.codec(ForgeRegistries.ITEMS.getRegistryKey()).fieldOf("tag").forGetter(
				IncompatibleImplantTagComponent::tag)
		).apply(instance, IncompatibleImplantTagComponent::new)
	);

	@Override
	public Codec<? extends ICyberneticComponent> codec()
	{
		return CODEC;
	}

	@Override
	public boolean canImplant(ItemStack stack, LivingEntity entity, Container inv)
	{
		return !inv.hasAnyMatching(s -> stack != s && s.is(this.tag));
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
		if (category != CyberneticTooltipCategory.REQUIREMENTS)
		{
			return;
		}

		ResourceLocation location = this.tag.location();

		tooltip.add(
			Component.translatable(
				"tooltip.cybernetics.incompatible_implant",
				Component.translatableWithFallback(
					"tag." + location.toLanguageKey().replace('/', '.'),
					"#" + location
				)
			).withStyle(ChatFormatting.RED)
		);
	}
}
