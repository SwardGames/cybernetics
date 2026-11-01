package com.sward.cybernetics.cybernetics.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sward.cybernetics.cybernetics.ICyberneticComponent;
import com.sward.cybernetics.cybernetics.events.implanting.CanImplant;
import com.sward.cybernetics.cybernetics.events.display.AppendTooltip;
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
public record RequiredImplantTagComponent(TagKey<Item> tag) implements ICyberneticComponent,
	CanImplant, AppendTooltip
{
	public static final Codec<RequiredImplantTagComponent> CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
			TagKey.codec(ForgeRegistries.ITEMS.getRegistryKey()).fieldOf("tag").forGetter(RequiredImplantTagComponent::tag)
		).apply(instance, RequiredImplantTagComponent::new)
	);

	@Override
	public Codec<? extends ICyberneticComponent> codec()
	{
		return CODEC;
	}

	@Override
	public boolean canImplant(ItemStack stack, LivingEntity entity, Container inv)
	{
		return inv.hasAnyMatching(s -> stack != s && s.is(this.tag));
	}

	@Override
	public int tooltipPriority()
	{
		return 8600;
	}

	@Override
	public void appendTooltip(List<Component> tooltip, ItemStack stack, @Nullable Player player, boolean isAdvanced)
	{
		ResourceLocation location = this.tag.location();

		tooltip.add(
			Component.translatable(
				"tooltip.cybernetics.required_implant",
				Component.translatableWithFallback(
					"tag." + location.getNamespace() + "." + location.getPath().replace('/', '.'),
					"#" + location
				)
			).withStyle(ChatFormatting.BLUE)
		);
	}
}
