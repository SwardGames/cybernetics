package com.sward.cybernetics.items;

import com.google.common.collect.ImmutableMultimap;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;

import javax.annotation.Nullable;
import java.util.UUID;

@FunctionalInterface
public interface ICyberneticItemFactory
{
	CyberneticItem apply(
		UUID uuid,
		TagKey<Item> slotTag,
		@Nullable TagKey<Item> requiredTag,
		@Nullable TagKey<Item> forbiddenTag,
		int capacityCost,
		ImmutableMultimap<Attribute, AttributeModifier> attributes,
		Item.Properties properties
	);
}
