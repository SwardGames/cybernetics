package com.sward.cybernetics.items;

import com.google.common.collect.ImmutableMultimap;
import com.sward.cybernetics.data.CyberneticSlotType;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;

import javax.annotation.Nullable;

@FunctionalInterface
public interface ICyberneticItemFactory
{
	CyberneticItem apply(
		String slotGroup,
		CyberneticSlotType slotType,
		@Nullable TagKey<Item> requiredTag,
		@Nullable TagKey<Item> forbiddenTag,
		int capacityCost,
		ImmutableMultimap<Attribute, AttributeModifier> attributes,
		ImmutableMultimap<String, AttributeModifier> curiosSlots,
		Item.Properties properties
	);
}
