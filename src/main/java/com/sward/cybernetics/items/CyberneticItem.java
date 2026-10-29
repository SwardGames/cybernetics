package com.sward.cybernetics.items;

import com.google.common.collect.ImmutableMultimap;
import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.UUID;

@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CyberneticItem extends Item
{
	public final UUID uuid;

	public final TagKey<Item> slotTag;

	@Nullable
	public final TagKey<Item> requiredTag;

	@Nullable
	public final TagKey<Item> forbiddenTag;

	public final int capacityCost;
	
	public final ImmutableMultimap<Attribute, AttributeModifier> attributes;

	public CyberneticItem(
		UUID uuid,
		TagKey<Item> slotTag,
		@Nullable TagKey<Item> requiredTag,
		@Nullable TagKey<Item> forbiddenTag,
		int capacityCost,
		ImmutableMultimap<Attribute, AttributeModifier> attributes,
		Properties properties
	)
	{
		super(properties);

		this.uuid = uuid;

		this.slotTag = slotTag;
		this.requiredTag = requiredTag;
		this.forbiddenTag = forbiddenTag;

		this.capacityCost = capacityCost;
		this.attributes = attributes;
	}

	public boolean validate(ItemStack stack, Container inv)
	{
		if (inv.hasAnyMatching(s -> stack != s && s.is(this)))
		{
			return false;
		}

		if (this.requiredTag != null)
		{
			if (!inv.hasAnyMatching(s -> stack != s && s.is(this.requiredTag)))
			{
				return false;
			}
		}

		if (this.forbiddenTag != null)
		{
			if (inv.hasAnyMatching(s -> stack != s && s.is(this.forbiddenTag)))
			{
				return false;
			}
		}

		return true;
	}
}
